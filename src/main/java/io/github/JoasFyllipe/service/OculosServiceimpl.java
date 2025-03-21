package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.dto.OculosResponseDTO;
import io.github.JoasFyllipe.exceptions.OculosNotFoundException;
import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Genero;
import io.github.JoasFyllipe.model.Modelo;
import io.github.JoasFyllipe.model.Oculos;
import io.github.JoasFyllipe.repository.OculosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OculosServiceimpl implements OculosService{

    @Inject
    OculosRepository oculosRepository;

    @Override
    @Transactional
    public OculosResponseDTO create(OculosDTO oculos) {
        Oculos novoOculos = new Oculos();
        novoOculos.setNome(oculos.getNome());
        novoOculos.setValor(oculos.getValor());
        novoOculos.setQuantidadeEstoque(oculos.getQuantidadeEstoque());

        novoOculos.setCorArmacao(CorArmacao.valueOf(oculos.getIdCorArmacao()));
        novoOculos.setGenero(Genero.valueOf(oculos.getIdGenero()));
        novoOculos.setModelo(Modelo.valueOf(oculos.getIdModelo()));

        oculosRepository.persist(novoOculos);

        return OculosResponseDTO.valueOf(novoOculos);
    }

    @Override
    public List<OculosResponseDTO> findAll() {
        return oculosRepository.findAll().stream().map(o -> OculosResponseDTO.valueOf(o)).toList();
    }

    @Override
    @Transactional
    public void update(Long id, OculosDTO oculosDTO) {
        Oculos oculos = oculosRepository.findById(id);

        if(oculos != null) {
            oculos.setNome(oculosDTO.getNome());
            oculos.setValor(oculosDTO.getValor());
            oculos.setCorArmacao(CorArmacao.valueOf(oculosDTO.getIdCorArmacao()));
            oculos.setGenero(Genero.valueOf(oculosDTO.getIdGenero()));
            oculos.setModelo(Modelo.valueOf(oculosDTO.getIdModelo()));

        }
        else{
            throw new OculosNotFoundException("Óculos não encotnrado com o ID: "+id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        oculosRepository.deleteById(id);
    }

    public List<OculosResponseDTO> findByCor(String corOuId) {
        CorArmacao corArmacao;

        try{
            int id = Integer.parseInt(corOuId);
            corArmacao = CorArmacao.fromId(id);
        }
        catch (NumberFormatException e){
            corArmacao = CorArmacao.fromNome(corOuId);
        }
        return  oculosRepository.findByCor(corArmacao).stream().map(OculosResponseDTO::valueOf).toList();
    }

    @Override
    public List<OculosResponseDTO> findByGenero(String corOuId) {
        Genero genero;

        try{
            int id = Integer.parseInt(corOuId);
            genero = Genero.fromId(id);
        }
        catch (NumberFormatException e){
            genero = Genero.fromNome(corOuId);
        }
        return oculosRepository.findByGenero(genero).stream().map(OculosResponseDTO::valueOf).toList();
    }

    @Override
    public List<OculosResponseDTO> findByModelo(String corOuId) {
        Modelo modelo;

        try {
            int id = Integer.parseInt(corOuId);
            modelo = Modelo.fromId(id);
        } catch (NumberFormatException e) {
            modelo = Modelo.fromNome(corOuId);
        }
        return oculosRepository.findByModelo(modelo).stream().map(OculosResponseDTO::valueOf).toList();
    }

}
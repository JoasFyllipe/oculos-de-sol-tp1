package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.MarcaResponseDTO;
import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.dto.OculosResponseDTO;
import io.github.JoasFyllipe.exceptions.OculosNotFoundException;
import io.github.JoasFyllipe.model.*;
import io.github.JoasFyllipe.repository.MarcaRepository;
import io.github.JoasFyllipe.repository.OculosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OculosServiceimpl implements OculosService{

    @Inject
    OculosRepository oculosRepository;

    @Inject
    MarcaRepository marcaRepository;

    @Override
    @Transactional
    public OculosResponseDTO create(OculosDTO oculos) {
        Oculos novoOculos = new Oculos();
        novoOculos.setNome(oculos.nome());
        novoOculos.setValor(oculos.valor());
        novoOculos.setQuantidadeEstoque(oculos.quantidadeEstoque());

        novoOculos.setCorArmacao(CorArmacao.valueOf(oculos.idCorArmacao()));
        novoOculos.setGenero(Genero.valueOf(oculos.idGenero()));
        novoOculos.setModelo(Modelo.valueOf(oculos.idModelo()));

        Marca marca = marcaRepository.findById(Long.valueOf(oculos.idMarca()));

        novoOculos.setMarca(marca);

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
            oculos.setNome(oculosDTO.nome());
            oculos.setValor(oculosDTO.valor());
            oculos.setCorArmacao(CorArmacao.valueOf(oculosDTO.idCorArmacao()));
            oculos.setGenero(Genero.valueOf(oculosDTO.idGenero()));
            oculos.setModelo(Modelo.valueOf(oculosDTO.idModelo()));
            
            Marca marca = marcaRepository.findById(Long.valueOf(oculosDTO.idMarca()));
            oculos.setMarca(marca);

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

    @Override
    public List<OculosResponseDTO> findByMarca(String marcaOuId) {
        Marca marca;

        try {
            Long id = Long.parseLong(marcaOuId);
            marca = Marca.fromId(id, marcaRepository.listAll());
        } catch (NumberFormatException e) {
            marca = Marca.fromNome(marcaOuId, marcaRepository.listAll());
        }
        return oculosRepository.findByMarca(marca).stream().map(OculosResponseDTO::valueOf).toList();
    }

    @Override
    public OculosResponseDTO findById(Long id) {
        OculosResponseDTO oculos = OculosResponseDTO.valueOf(oculosRepository.findById(id));

        if(oculos == null){
            throw new OculosNotFoundException("Oculos não encontrado para o Id: "+ id);
        }
        return oculos;
    }

}
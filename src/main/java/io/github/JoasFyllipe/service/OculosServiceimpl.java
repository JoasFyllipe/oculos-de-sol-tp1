package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Genero;
import io.github.JoasFyllipe.model.Modelo;
import io.github.JoasFyllipe.model.Oculos;
import io.github.JoasFyllipe.repository.OculosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OculosServiceimpl implements OculosService{

    @Inject
    OculosRepository oculosRepository;

    @Override
    @Transactional
    public Oculos create(OculosDTO oculos) {
        Oculos novoOculos = new Oculos();
        novoOculos.setNome(oculos.getNome());
        novoOculos.setValor(oculos.getValor());
        novoOculos.setQuantidadeEstoque(oculos.getQuantidadeEstoque());

        CorArmacao corArmacao = null;
        for(CorArmacao c: CorArmacao.values()){
            if(c.getID() == oculos.getIdCorArmacao())
                corArmacao = c;
        }
        Genero genero = null;
        for(Genero g: Genero.values()){
            if(g.getID() == oculos.getIdGenero())
                genero = g;
        }
        Modelo modelo = null;
        for(Modelo m: Modelo.values()){
            if(m.getID() == oculos.getIdModelo())
                modelo = m;
        }
        novoOculos.setCorArmacao(corArmacao);
        novoOculos.setGenero(genero);
        novoOculos.setModelo(modelo);

        oculosRepository.persist(novoOculos);

        return novoOculos;
    }

    @Override
    public List<Oculos> findAll() {
        return oculosRepository.findAll().list();
    }

    @Override
    @Transactional
    public void update(Long id, OculosDTO oculosDTO) {

    }

    @Override
    public void delete(Long id) {
        oculosRepository.deleteById(id);
    }


    public List<OculosDTO> findByCor(String corOuId) {
        CorArmacao corArmacao;

        try{
            int id = Integer.parseInt(corOuId);
            corArmacao = CorArmacao.fromId(id);
        }
        catch (NumberFormatException e){
            corArmacao = CorArmacao.fromNome(corOuId);
        }
        return  oculosRepository.findByCor(corArmacao).stream().map(OculosDTO::new).toList();
    }

}
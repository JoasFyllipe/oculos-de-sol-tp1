package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.MarcaDTO;
import io.github.JoasFyllipe.dto.MarcaResponseDTO;
import io.github.JoasFyllipe.dto.OculosResponseDTO;
import io.github.JoasFyllipe.exceptions.MarcaNotFoundException;
import io.github.JoasFyllipe.exceptions.OculosNotFoundException;
import io.github.JoasFyllipe.model.Genero;
import io.github.JoasFyllipe.model.Marca;
import io.github.JoasFyllipe.model.Oculos;
import io.github.JoasFyllipe.repository.MarcaRepository;
import io.github.JoasFyllipe.repository.OculosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MarcaServiceimpl implements MarcaService{

    @Inject
    MarcaRepository marcaRepository;

    @Inject
    OculosRepository oculosRepository;

    @Override
    @Transactional
    public MarcaResponseDTO create(MarcaDTO marcaDTO){
        Marca novaMarca = new Marca();
        novaMarca.setNome(marcaDTO.nome());

        marcaRepository.persist(novaMarca);

        return MarcaResponseDTO.valueOf(novaMarca);
    }

    @Override
    public void update(Long id, MarcaDTO marcaDTO) {
        Marca edicaoMarca = marcaRepository.findById(id);

        if(edicaoMarca != null){
            edicaoMarca.setNome(marcaDTO.nome());
        }
        else{
            throw new MarcaNotFoundException("Marca não encontrada para o Id: "+ id);
        }

    }

    @Override
    public void delete(Long id) {
        marcaRepository.deleteById(id);
    }

    @Override
    public MarcaResponseDTO findById(Long id) {
        Marca marca = marcaRepository.findById(id);
        if(marca == null)
            throw new OculosNotFoundException("Marca não encontrada para o Id: "+ id);

        return MarcaResponseDTO.valueOf(marca);
    }

    @Override
    public MarcaResponseDTO findByNome(String nome) {
        Marca marca = Marca.fromNome(nome, marcaRepository.listAll());

        if(marca == null){
            throw new MarcaNotFoundException("Marca não encontrada para o nome: " + nome);
        }
        return MarcaResponseDTO.valueOf(marca);
    }

    @Override
    public List<MarcaResponseDTO> findAll() {
        return  marcaRepository.findAll().stream().map(m -> MarcaResponseDTO.valueOf(m)).toList();
    }
}

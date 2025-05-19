package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.oculos.OculosRequestDTO;
import io.github.JoasFyllipe.dto.oculos.OculosResponseDTO;
import io.github.JoasFyllipe.exceptions.OculosNotFoundException;
import io.github.JoasFyllipe.model.*;
import io.github.JoasFyllipe.repository.MarcaRepository;
import io.github.JoasFyllipe.repository.OculosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OculosServiceimpl implements OculosService {

    @Inject
    OculosRepository oculosRepository;

    @Inject
    MarcaRepository marcaRepository;

    @Override
    @Transactional
    public OculosResponseDTO create(OculosRequestDTO oculos) {
        Oculos novoOculos = new Oculos();
        novoOculos.setNome(oculos.nome());
        novoOculos.setValor(oculos.valor());
        novoOculos.setQuantidadeEstoque(oculos.quantidadeEstoque());

        novoOculos.setCorArmacao(CorArmacao.valueOf(oculos.corArmacao().getID()));
        novoOculos.setGenero(Genero.valueOf(oculos.genero().getID()));
        novoOculos.setModelo(Modelo.valueOf(oculos.modelo().getID()));

        Marca marca = marcaRepository.findById(Long.valueOf(oculos.marca().getId()));
        novoOculos.setMarca(marca);

        oculosRepository.persist(novoOculos);

        return OculosResponseDTO.valueOf(novoOculos);
    }

    @Override
    public List<OculosResponseDTO> findAll() {
        return oculosRepository.findAll()
                .stream()
                .map(OculosResponseDTO::valueOf)
                .toList();
    }

    /*
     package io.github.JoasFyllipe.dto.oculos;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Genero;
import io.github.JoasFyllipe.model.Modelo;
import io.github.JoasFyllipe.model.Oculos;


public record OculosRequestDTO(
    String nome,
    Double valor,
    Integer quantidadeEstoque,
    CorArmacao corArmacao,
    Genero genero,
    Modelo modelo,
    MarcaRequestDTO marca) {

    public static Oculos toEntity(OculosRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Oculos(
                    dto.nome(),
                    dto.valor(),
                    dto.quantidadeEstoque(),
                    dto.corArmacao(),
                    dto.genero(),
                    dto.modelo(),
                    MarcaRequestDTO.toEntity(dto.marca())
                );
    }
}

     */
    @Override
    @Transactional
    public void update(Long id, OculosRequestDTO oculosDTO) {
        Oculos oculos = oculosRepository.findById(id);

        if (oculos != null) {
            oculos.setNome(oculosDTO.nome());
            oculos.setValor(oculosDTO.valor());
            oculos.setQuantidadeEstoque(oculosDTO.quantidadeEstoque());
            oculos.setCorArmacao(CorArmacao.valueOf(oculosDTO.corArmacao().getID()));
            oculos.setGenero(Genero.valueOf(oculosDTO.genero().getID()));
            oculos.setModelo(Modelo.valueOf(oculosDTO.modelo().getID()));

            Marca marca = marcaRepository.findById(Long.valueOf(oculosDTO.marca().getId()));
            oculos.setMarca(marca);
        } else {
            throw new OculosNotFoundException("Óculos não encontrado com o ID: " + id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!oculosRepository.deleteById(id)) {
            throw new OculosNotFoundException("Óculos não encontrado para o ID: " + id);
        }
    }

    @Override
    public List<OculosResponseDTO> findByCor(String corOuId) {
        CorArmacao corArmacao;

        try {
            int id = Integer.parseInt(corOuId);
            corArmacao = CorArmacao.fromId(id);
        } catch (NumberFormatException e) {
            corArmacao = CorArmacao.fromNome(corOuId);
        }

        return oculosRepository.findByCor(corArmacao)
                .stream()
                .map(OculosResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<OculosResponseDTO> findByGenero(String generoOuId) {
        Genero genero;

        try {
            int id = Integer.parseInt(generoOuId);
            genero = Genero.fromId(id);
        } catch (NumberFormatException e) {
            genero = Genero.fromNome(generoOuId);
        }

        return oculosRepository.findByGenero(genero)
                .stream()
                .map(OculosResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<OculosResponseDTO> findByModelo(String modeloOuId) {
        Modelo modelo;

        try {
            int id = Integer.parseInt(modeloOuId);
            modelo = Modelo.fromId(id);
        } catch (NumberFormatException e) {
            modelo = Modelo.fromNome(modeloOuId);
        }

        return oculosRepository.findByModelo(modelo)
                .stream()
                .map(OculosResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<OculosResponseDTO> findByMarca(String marcaOuId) {
        Marca marca;

        try {
            Long id = Long.valueOf(marcaOuId);
            marca = Marca.fromId(id, marcaRepository.listAll());
        } catch (NumberFormatException e) {
            marca = Marca.fromNome(marcaOuId, marcaRepository.listAll());
        }

        return oculosRepository.findByMarca(marca)
                .stream()
                .map(OculosResponseDTO::valueOf)
                .toList();
    }

    @Override
    public OculosResponseDTO findById(Long id) {
        Oculos oculos = oculosRepository.findById(id);
        if (oculos == null) {
            throw new OculosNotFoundException("Óculos não encontrado para o ID: " + id);
        }
        return OculosResponseDTO.valueOf(oculos);
    }
}

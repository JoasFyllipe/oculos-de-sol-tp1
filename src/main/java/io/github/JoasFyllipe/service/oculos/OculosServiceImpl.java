package io.github.JoasFyllipe.service.oculos;

import io.github.JoasFyllipe.dto.oculos.*;
import io.github.JoasFyllipe.dto.oculos.patch.*;
import io.github.JoasFyllipe.exceptions.OculosNotFoundException;
import io.github.JoasFyllipe.model.enums.CorArmacao;
import io.github.JoasFyllipe.model.enums.Genero;
import io.github.JoasFyllipe.model.enums.Modelo;
import io.github.JoasFyllipe.model.marca.Marca;
import io.github.JoasFyllipe.model.oculos.Oculos;
import io.github.JoasFyllipe.repository.MarcaRepository;
import io.github.JoasFyllipe.repository.OculosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OculosServiceImpl implements OculosService {

    @Inject
    OculosRepository oculosRepository;

    @Inject
    MarcaRepository marcaRepository;

    @Override
    @Transactional
    public OculosResponseDTO create(OculosRequestDTO dto) {
        Oculos oculos = new Oculos();
        return persistOculos(oculos, dto);
    }

    @Override
    @Transactional
    public OculosResponseDTO update(Long id, OculosRequestDTO dto) {
        Oculos oculos = oculosRepository.findByIdOptional(id)
                .orElseThrow(() -> new OculosNotFoundException("Óculos com ID " + id + " não encontrado."));
        return persistOculos(oculos, dto);
    }

    private OculosResponseDTO persistOculos(Oculos oculos, OculosRequestDTO dto) {
        oculos.setNome(dto.nome());
        oculos.setPreco(dto.preco());
        oculos.setQuantidadeEstoque(dto.quantidadeEstoque());

        // Converte os IDs recebidos dos DTOs para os Enums correspondentes
        oculos.setCorArmacao(CorArmacao.valueOf(dto.idCorArmacao()));
        oculos.setGenero(Genero.valueOf(dto.idGenero()));
        oculos.setModelo(Modelo.valueOf(dto.idModelo()));

        // Busca a entidade Marca pelo ID
        Marca marca = marcaRepository.findByIdOptional(dto.idMarca())
                .orElseThrow(() -> new NotFoundException("Marca com ID " + dto.idMarca() + " não encontrada."));
        oculos.setMarca(marca);

        oculosRepository.persist(oculos);
        return OculosResponseDTO.valueOf(oculos);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!oculosRepository.deleteById(id)) {
            throw new OculosNotFoundException("Óculos com ID " + id + " não encontrado.");
        }
    }

    @Override
    @Transactional
    public void updateEstoque(Long id, OculosUpdateEstoqueDTO dto) {
        Oculos oculos = oculosRepository.findByIdOptional(id)
                .orElseThrow(() -> new OculosNotFoundException("Óculos com ID " + id + " não encontrado."));
        oculos.setQuantidadeEstoque(dto.novaQuantidade());
    }

    @Override
    @Transactional
    public void updateNome(Long id, OculosUpdateNomeDTO dto) {
        Oculos oculos = oculosRepository.findByIdOptional(id)
                .orElseThrow(() -> new OculosNotFoundException("Óculos com ID " + id + " não encontrado."));
        oculos.setNome(dto.novoNome());
    }

    @Override
    @Transactional
    public void updatePreco(Long id, OculosUpdatePrecoDTO dto) {
        Oculos oculos = oculosRepository.findByIdOptional(id)
                .orElseThrow(() -> new OculosNotFoundException("Óculos com ID " + id + " não encontrado."));
        oculos.setPreco(dto.novoPreco());
    }

    @Override
    @Transactional
    public void updateCor(Long id, OculosUpdateCorDTO dto) {
        Oculos oculos = oculosRepository.findByIdOptional(id)
                .orElseThrow(() -> new OculosNotFoundException("Óculos com ID " + id + " não encontrado."));
        oculos.setCorArmacao(CorArmacao.valueOf(dto.idNovaCor()));
    }

    @Override
    @Transactional
    public void updateModelo(Long id, OculosUpdateModeloDTO dto) {
        Oculos oculos = oculosRepository.findByIdOptional(id)
                .orElseThrow(() -> new OculosNotFoundException("Óculos com ID " + id + " não encontrado."));
        oculos.setModelo(Modelo.valueOf(dto.idNovoModelo()));
    }

    @Override
    public OculosResponseDTO findById(Long id) {
        return oculosRepository.findByIdOptional(id)
                .map(OculosResponseDTO::valueOf)
                .orElseThrow(() -> new OculosNotFoundException("Óculos com ID " + id + " não encontrado."));
    }

    @Override
    public List<OculosResponseDTO> findAll() {
        return oculosRepository.listAll().stream()
                .map(OculosResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<OculosResponseDTO> findByCor(Integer idCor) {
        CorArmacao cor = CorArmacao.valueOf(idCor);
        return oculosRepository.findByCor(cor).stream()
                .map(OculosResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<OculosResponseDTO> findByGenero(Integer idGenero) {
        Genero genero = Genero.valueOf(idGenero);
        return oculosRepository.findByGenero(genero).stream()
                .map(OculosResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<OculosResponseDTO> findByModelo(Integer idModelo) {
        Modelo modelo = Modelo.valueOf(idModelo);
        return oculosRepository.findByModelo(modelo).stream()
                .map(OculosResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<OculosResponseDTO> findByMarca(Long idMarca) {
        Marca marca = marcaRepository.findByIdOptional(idMarca)
                .orElseThrow(() -> new NotFoundException("Marca com ID " + idMarca + " não encontrada."));
        return oculosRepository.findByMarca(marca).stream()
                .map(OculosResponseDTO::valueOf)
                .collect(Collectors.toList());
    }
}
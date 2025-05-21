package io.github.JoasFyllipe.service.funcionario;

import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;
import io.github.JoasFyllipe.exceptions.FuncionarioNotFoundException;
import io.github.JoasFyllipe.model.funcionario.Funcionario;
import io.github.JoasFyllipe.repository.FuncionarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import io.github.JoasFyllipe.service.usuario.UsuarioService;
import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import io.github.JoasFyllipe.repository.UsuarioRepository;

@ApplicationScoped
public class FuncionarioServiceImpl implements FuncionarioService {

    @Inject
    FuncionarioRepository funcionarioRepository;

    @Inject
    UsuarioRepository usuarioService;

    @Override
    @Transactional
    public FuncionarioResponseDTO create(FuncionarioRequestDTO funcionarioDTO) {
        Funcionario funcionario = FuncionarioRequestDTO.toEntity(funcionarioDTO);
        funcionario.setUsuario((usuarioService.findById((funcionarioDTO.usuario()))));
        funcionarioRepository.persist(funcionario);
        return FuncionarioResponseDTO.valueOf(funcionario);
    }

    @Override
    @Transactional
    public void update(Long id, FuncionarioUpdateRequestDTO funcionarioDTO) {
        Funcionario funcionario = funcionarioRepository.findById(id);
        if (funcionario == null) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado para o Id: " + id);
        }
        funcionario.setDataContratacao(LocalDate.parse(funcionarioDTO.dataContratacao()));
        funcionario.setCargo(funcionarioDTO.cargo());
        funcionario.setSalario(funcionarioDTO.salario());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!funcionarioRepository.deleteById(id)) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado para o Id: " + id);
        }
    }

    @Override
    public FuncionarioResponseDTO findById(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id);
        if (funcionario == null) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado para o Id: " + id);
        }
        return FuncionarioResponseDTO.valueOf(funcionario);
    }

    @Override
    public FuncionarioResponseDTO findByNome(String nome) {
        Funcionario funcionario = funcionarioRepository.findByNome(nome);
        if (funcionario == null) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado para o nome: " + nome);
        }
        return FuncionarioResponseDTO.valueOf(funcionario);
    }

    @Override
    public List<FuncionarioResponseDTO> findAll() {
        return funcionarioRepository.findAll()
                .stream()
                .map(FuncionarioResponseDTO::valueOf)
                .collect(Collectors.toList());
    }
}

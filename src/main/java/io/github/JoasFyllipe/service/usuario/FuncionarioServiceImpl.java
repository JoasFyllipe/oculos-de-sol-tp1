package io.github.JoasFyllipe.service.usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import io.github.JoasFyllipe.exceptions.FuncionarioNotFoundException;
import io.github.JoasFyllipe.model.usuario.endereco.Cidade;
import io.github.JoasFyllipe.model.usuario.endereco.Endereco;
import io.github.JoasFyllipe.model.usuario.telefone.Telefone;
import io.github.JoasFyllipe.model.usuario.Funcionario;
import io.github.JoasFyllipe.model.usuario.Perfil;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.model.usuario.telefone.TipoTelefone;
import io.github.JoasFyllipe.repository.CidadeRepository;
import io.github.JoasFyllipe.repository.FuncionarioRepository;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@ApplicationScoped
public class FuncionarioServiceImpl implements FuncionarioService {

    @Inject
    FuncionarioRepository funcionarioRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    CidadeRepository cidadeRepository;

    @Inject
    HashService hashService;

    @Override
    @Transactional
    public FuncionarioResponseDTO create(FuncionarioRequestDTO funcionarioDTO) {

        if(funcionarioDTO == null)
            throw new ValidationException("Informe os campos necessários");

        if(usuarioRepository.findByEmail(funcionarioDTO.usuario().email()) != null)
            throw new ValidationException("Esse email já foi cadastrado");

        if(usuarioRepository.findByCpf(funcionarioDTO.usuario().cpf()) != null)
            throw new ValidationException("Esse cpf já foi cadastrado");

        Usuario usuario = new Usuario();
        usuario.setEmail(funcionarioDTO.usuario().email());
        usuario.setCpf(funcionarioDTO.usuario().cpf());
        usuario.setNome(funcionarioDTO.usuario().nome());
        usuario.setDataNascimento(funcionarioDTO.usuario().dataNascimento());
        usuario.setSenha(hashService.getHashSenha(funcionarioDTO.usuario().senha()));
        if(usuario.getPerfis() == null)
            usuario.setPerfis(new ArrayList<>());
        usuario.getPerfis().add(Perfil.EMPLOYE);

        // CORREÇÃO: Associa o usuário a cada telefone antes de adicionar à lista
        if (funcionarioDTO.usuario().telefones() != null) {
            List<Telefone> telefones = funcionarioDTO.usuario().telefones().stream()
                    .filter(Objects::nonNull)
                    .map(TelefoneRequestDTO::toEntity)
                    .peek(telefone -> telefone.setUsuario(usuario))
                    .collect(Collectors.toList());
            usuario.setTelefone(telefones);
        }

        // CORREÇÃO: Associa o usuário a cada endereço antes de adicionar à lista
        if (funcionarioDTO.usuario().enderecos() != null) {
            List<Endereco> enderecos = funcionarioDTO.usuario().enderecos().stream()
                    .filter(Objects::nonNull)
                    .map(this::mapEnderecoDtoToEntity)
                    .peek(endereco -> endereco.setUsuario(usuario))
                    .collect(Collectors.toList());
            usuario.setEndereco(enderecos);
        }

        usuarioRepository.persist(usuario);

        Funcionario funcionario = new Funcionario();
        funcionario.setCargo(funcionarioDTO.cargo());
        funcionario.setSalario(funcionarioDTO.salario());
        funcionario.setDataContratacao(funcionarioDTO.dataContratacao());
        funcionario.setUsuario(usuario);

        funcionarioRepository.persist(funcionario);

        return FuncionarioResponseDTO.valueOf(funcionario);
    }

    @Override
    @Transactional
    public FuncionarioResponseDTO update(Long id, FuncionarioUpdateRequestDTO funcionarioDTO) {
        if(funcionarioDTO == null)
            throw new ValidationException("Informe os campos necessários");

        Funcionario funcionario = funcionarioRepository.findById(id);
        if (funcionario == null) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado para o Id: " + id);
        }

        Usuario usuario = funcionario.getUsuario();

        if(usuarioRepository.findByCpf(funcionarioDTO.cpf()) != null && (!funcionarioDTO.cpf().equals(usuario.getCpf())))
            throw new ValidationException("Cpf já cadastrado no sistema");

        usuario.setNome(funcionarioDTO.nome());
        usuario.setCpf(funcionarioDTO.cpf());
        usuario.setDataNascimento(funcionarioDTO.dataNascimento());

        funcionario.setCargo(funcionarioDTO.cargo());
        funcionario.setSalario(funcionarioDTO.salario());

        // CORREÇÃO na atualização de Telefones
        if (usuario.getTelefone() == null) {
            usuario.setTelefone(new ArrayList<>());
        }
        usuario.getTelefone().clear();
        if (funcionarioDTO.telefones() != null) {
            List<Telefone> novosTelefones = funcionarioDTO.telefones().stream()
                    .filter(Objects::nonNull)
                    .map(TelefoneRequestDTO::toEntity)
                    .peek(telefone -> telefone.setUsuario(usuario))
                    .collect(Collectors.toList());
            usuario.getTelefone().addAll(novosTelefones);
        }

        // CORREÇÃO na atualização de Endereços
        if (usuario.getEndereco() == null) {
            usuario.setEndereco(new ArrayList<>());
        }
        usuario.getEndereco().clear();
        if (funcionarioDTO.enderecos() != null) {
            List<Endereco> novosEnderecos = funcionarioDTO.enderecos().stream()
                    .filter(Objects::nonNull)
                    .map(this::mapEnderecoDtoToEntity)
                    .peek(endereco -> endereco.setUsuario(usuario))
                    .collect(Collectors.toList());
            usuario.getEndereco().addAll(novosEnderecos);
        }

        return FuncionarioResponseDTO.valueOf(funcionario);
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

    @Override
    public FuncionarioResponseDTO findByEmail(String email) {
        var funcionario = funcionarioRepository.findByEmail(email);
        if(funcionario == null)
            throw new FuncionarioNotFoundException("Funcionário não encontrado para o email: "+ email);

        return FuncionarioResponseDTO.valueOf(funcionario);
    }

    @Override
    public List<FuncionarioResponseDTO> findByCargo(String cargo) {
        return funcionarioRepository.findByCargo(cargo)
                .stream()
                .map(FuncionarioResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateEndereco(String email, List<EnderecoRequestDTO> enderecoDTOs) {
        Funcionario funcionario = funcionarioRepository.findByUsuario(email);
        if(funcionario == null)
            throw new FuncionarioNotFoundException("Funcionario não encontrado");

        Usuario usuario = funcionario.getUsuario();
        if (usuario.getEndereco() == null) {
            usuario.setEndereco(new ArrayList<>());
        }
        usuario.getEndereco().clear();
        if (enderecoDTOs != null) {
            List<Endereco> novosEnderecos = enderecoDTOs.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapEnderecoDtoToEntity)
                    .peek(endereco -> endereco.setUsuario(usuario))
                    .collect(Collectors.toList());
            usuario.getEndereco().addAll(novosEnderecos);
        }
    }

    @Override
    @Transactional
    public void updateTelefone(String email, List<TelefoneRequestDTO> telefoneDTOs) {
        Funcionario funcionario = funcionarioRepository.findByUsuario(email);
        if(funcionario == null)
            throw new FuncionarioNotFoundException("Funcionario não encontrado");

        Usuario usuario = funcionario.getUsuario();
        if (usuario.getTelefone() == null) {
            usuario.setTelefone(new ArrayList<>());
        }
        usuario.getTelefone().clear();
        if (telefoneDTOs != null) {
            List<Telefone> novosTelefones = telefoneDTOs.stream()
                    .filter(Objects::nonNull)
                    .map(TelefoneRequestDTO::toEntity)
                    .peek(telefone -> telefone.setUsuario(usuario))
                    .collect(Collectors.toList());
            usuario.getTelefone().addAll(novosTelefones);
        }
    }

    @Override
    @Transactional
    public void updateTelefoneEspecifico(String email, Long idTelefone, TelefoneRequestDTO telefoneDTO) {
        Funcionario funcionario = funcionarioRepository.findByUsuario(email);
        if(funcionario == null)
            throw new FuncionarioNotFoundException("Funcionário não encontrado");

        Telefone telefone = funcionario.getUsuario().getTelefone()
                .stream()
                .filter(a -> a.getId().equals(idTelefone))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Telefone nao encontrado"));

        telefone.setNumero(telefoneDTO.numero());
        telefone.setDdd(telefoneDTO.ddd());
        telefone.setPrincipal(telefoneDTO.principal());
        telefone.setTipo(TipoTelefone.valueOf(telefoneDTO.idTipoTelefone()));
    }

    @Override
    @Transactional
    public void updateSenha(String email, SenhaPatchRequestDTO senhaPatch) {
        if(senhaPatch == null){
            throw new ValidationException("Informe os campos necessários");
        }
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null)
            throw new ValidationException("Usuario não encontrado");

        if(!hashService.getHashSenha(senhaPatch.senhaAtual()).equals(usuario.getSenha()))
            throw new ValidationException("A senha atual está inválida");

        if(!senhaPatch.novaSenha().equals(senhaPatch.repetirNovaSenha()))
            throw new ValidationException("As senhas não conferem, verifique novamente");

        usuario.setSenha(hashService.getHashSenha(senhaPatch.novaSenha()));
    }

    @Override
    @Transactional
    public void updateCpf(String email, CpfPatchRequestDTO cpfPatch) {
        if(cpfPatch == null)
            throw new ValidationException("Informe os campos necessários");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null)
            throw new ValidationException("Usuario não encontrado");

        if(cpfPatch.novoCpf().equals(usuario.getCpf()))
            throw new ValidationException("O novo cpf não pode ser igual ao atual");

        if(usuarioRepository.findByCpf(cpfPatch.novoCpf()) != null)
            throw new ValidationException("Cpf já cadastrado");

        usuario.setCpf(cpfPatch.novoCpf());
    }

    @Override
    @Transactional
    public void updateNome(String email, NomePatchRequestDTO nomePatch) {
        if(nomePatch == null)
            throw new ValidationException("Informe os campos necessários");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null)
            throw new ValidationException("Usuario não encontrado");

        if(!(nomePatch.novoNome().length() > 0))
            throw new ValidationException("O nome não pode estar vazio");

        usuario.setNome(nomePatch.novoNome());
    }

    @Override
    @Transactional
    public void updateEmail(String email, EmailPatchRequestDTO emailPatch) {
        if(emailPatch == null)
            throw new ValidationException("informe os campos necessários");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null)
            throw new ValidationException("Usuario não encontrado");
        if(emailPatch.novoEmail().equals(usuario.getEmail()))
            throw new ValidationException("O novo email não pode ser igual ao email atual");

        if(usuarioRepository.findByEmail(emailPatch.novoEmail()) != null)
            throw new ValidationException("Email já cadastrado");

        usuario.setEmail(emailPatch.novoEmail());
    }

    @Override
    @Transactional
    public void updateDataNascimento(String email, DataNascimentoPatchDTO dataNascimentoPatch) {
        if(dataNascimentoPatch == null)
            throw new ValidationException("Informe os campos necessários");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null)
            throw new ValidationException("Usuario não encontrado");

        usuario.setDataNascimento(dataNascimentoPatch.dataNascimento());
    }

    @Override
    @Transactional
    public void updateSalario(String email, SalarioPatchRequestDTO salarioPatch) {
        if (salarioPatch == null || salarioPatch.salario() == null) {
            throw new ValidationException("Informe o novo salário.");
        }

        // A busca é feita pelo email do usuário logado para garantir a segurança
        Funcionario funcionario = funcionarioRepository.findByUsuario(email);
        if (funcionario == null) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado.");
        }

        // Validação para garantir que o salário é positivo
        if (salarioPatch.salario().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("O salário não pode ser um valor negativo.");
        }

        funcionario.setSalario(salarioPatch.salario());
    }

    private Endereco mapEnderecoDtoToEntity(EnderecoRequestDTO dto) {
        Cidade cidade = cidadeRepository.findById(dto.idCidade());
        if (cidade == null) {
            throw new ValidationException("Cidade com ID " + dto.idCidade() + " não encontrada.");
        }
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setBairro(dto.bairro());
        endereco.setCep(dto.cep());
        endereco.setComplemento(dto.complemento());
        endereco.setCidade(cidade);
        return endereco;
    }
}
package io.github.JoasFyllipe.service.usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.JoasFyllipe.dto.cliente.ClienteRequestDTO;
import io.github.JoasFyllipe.dto.cliente.ClienteResponseDTO;
import io.github.JoasFyllipe.dto.cliente.ClienteUpdateRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import io.github.JoasFyllipe.exceptions.ClienteNotFoundException;
import io.github.JoasFyllipe.exceptions.UsuarioNotFoundException;
import io.github.JoasFyllipe.model.usuario.endereco.Cidade;
import io.github.JoasFyllipe.model.usuario.endereco.Endereco;
import io.github.JoasFyllipe.model.usuario.Funcionario;
import io.github.JoasFyllipe.model.usuario.telefone.Telefone;
import io.github.JoasFyllipe.model.usuario.Cliente;
import io.github.JoasFyllipe.model.usuario.Perfil;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.model.usuario.telefone.TipoTelefone;
import io.github.JoasFyllipe.repository.CidadeRepository;
import io.github.JoasFyllipe.repository.ClienteRepository;
import io.github.JoasFyllipe.repository.FuncionarioRepository;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@ApplicationScoped
public class ClienteServiceImpl implements ClienteService {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    FuncionarioRepository funcionarioRepository;

    @Inject
    CidadeRepository cidadeRepository;

    @Inject
    HashService hashService;

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteRequestDTO clienteDTO) {

        if (clienteDTO == null)
            throw new ValidationException("Informe os campos necessários");

        if (usuarioRepository.findByEmail(clienteDTO.usuario().email()) != null)
            throw new ValidationException("Esse email já foi cadastrado");

        if (usuarioRepository.findByCpf(clienteDTO.usuario().cpf()) != null)
            throw new ValidationException("Esse cpf já foi cadastrado");

        Usuario novoUsuario = new Usuario();

        novoUsuario.setNome(clienteDTO.usuario().nome());
        novoUsuario.setDataNascimento(clienteDTO.usuario().dataNascimento());
        novoUsuario.setCpf(clienteDTO.usuario().cpf());
        novoUsuario.setEmail(clienteDTO.usuario().email());
        novoUsuario.setSenha(hashService.getHashSenha(clienteDTO.usuario().senha()));
        if (novoUsuario.getPerfis() == null)
            novoUsuario.setPerfis(new ArrayList<>());
        novoUsuario.getPerfis().add(Perfil.USER);

        // CORREÇÃO: Associa o usuário a cada telefone antes de adicionar à lista
        if (clienteDTO.usuario().telefones() != null) {
            List<Telefone> telefones = clienteDTO.usuario().telefones().stream()
                    .map(TelefoneRequestDTO::toEntity)
                    .peek(telefone -> telefone.setUsuario(novoUsuario)) // Associa o usuário ao telefone
                    .collect(Collectors.toList());
            novoUsuario.setTelefone(telefones);
        }

        // CORREÇÃO: Associa o usuário a cada endereço antes de adicionar à lista
        if (clienteDTO.usuario().enderecos() != null) {
            List<Endereco> enderecos = clienteDTO.usuario().enderecos().stream()
                    .map(this::mapEnderecoDtoToEntity)
                    .peek(endereco -> endereco.setUsuario(novoUsuario)) // Associa o usuário ao endereço
                    .collect(Collectors.toList());
            novoUsuario.setEndereco(enderecos);
        }

        // Preciso adicionar os cartões

        usuarioRepository.persist(novoUsuario);

        Cliente novoCliente = new Cliente();
        novoCliente.setUsuario(novoUsuario);
        novoCliente.setDataCadastro(LocalDateTime.now());
        clienteRepository.persist(novoCliente);

        return ClienteResponseDTO.valueOf(novoCliente);
    }

    @Override
    @Transactional
    public Cliente update(String email, ClienteUpdateRequestDTO clienteDTO) {

        if (clienteDTO == null)
            throw new ValidationException("Informe os campos necessários");

        Cliente cliente = clienteRepository.findByUsuario(email);
        if (cliente == null)
            throw new UsuarioNotFoundException("Usuário não encontrado para o email: " + email);

        Usuario usuario = cliente.getUsuario();

        if (usuarioRepository.findByCpf(clienteDTO.cpf()) != null && (!clienteDTO.cpf().equals(usuario.getCpf())))
            throw new ValidationException("Cpf já cadastrado");

        usuario.setNome(clienteDTO.nome());
        usuario.setCpf(clienteDTO.cpf());
        usuario.setDataNascimento(clienteDTO.dataNascimento());
        if (usuario.getPerfis() == null)
            usuario.setPerfis(new ArrayList<>());
        usuario.getPerfis().add(Perfil.USER);

        // CORREÇÃO na atualização de Telefones
        if (usuario.getTelefone() == null) {
            usuario.setTelefone(new ArrayList<>());
        }
        usuario.getTelefone().clear(); // Limpa a lista antiga (orphanRemoval=true deletará do banco)
        if (clienteDTO.telefones() != null) {
            List<Telefone> novosTelefones = clienteDTO.telefones().stream()
                    .map(TelefoneRequestDTO::toEntity)
                    .peek(telefone -> telefone.setUsuario(usuario)) // Associa o usuário ao telefone
                    .collect(Collectors.toList());
            usuario.getTelefone().addAll(novosTelefones);
        }

        // CORREÇÃO na atualização de Endereços
        if (usuario.getEndereco() == null) {
            usuario.setEndereco(new ArrayList<>());
        }
        usuario.getEndereco().clear(); // Limpa a lista antiga
        if (clienteDTO.enderecos() != null) {
            List<Endereco> novosEnderecos = clienteDTO.enderecos().stream()
                    .map(this::mapEnderecoDtoToEntity)
                    .peek(endereco -> endereco.setUsuario(usuario)) // Associa o usuário ao endereço
                    .collect(Collectors.toList());
            usuario.getEndereco().addAll(novosEnderecos);
        }

        return cliente;
    }

    @Override
    @Transactional
    public void updateEndereco(String email, List<EnderecoRequestDTO> enderecoDTOs) {
        Cliente cliente = clienteRepository.findByUsuario(email);
        if (cliente == null)
            throw new ClienteNotFoundException("Cliente não encontrado");

        Usuario usuario = cliente.getUsuario();
        if (usuario.getEndereco() == null) {
            usuario.setEndereco(new ArrayList<>());
        }

        usuario.getEndereco().clear();
        if (enderecoDTOs != null) {
            // CORREÇÃO: Associa o usuário a cada endereço antes de adicionar à lista
            List<Endereco> novosEnderecos = enderecoDTOs.stream()
                    .map(this::mapEnderecoDtoToEntity)
                    .peek(endereco -> endereco.setUsuario(usuario)) // Associa o usuário ao endereço
                    .collect(Collectors.toList());
            usuario.getEndereco().addAll(novosEnderecos);
        }
    }

    @Override
    @Transactional
    public void updateTelefone(String email, List<TelefoneRequestDTO> telefoneDTOs) {
        Cliente cliente = clienteRepository.findByUsuario(email);

        if (cliente == null)
            throw new ClienteNotFoundException("Cliente não encontrado");

        Usuario usuario = cliente.getUsuario();
        if (usuario.getTelefone() == null) {
            usuario.setTelefone(new ArrayList<>());
        }

        usuario.getTelefone().clear();
        if (telefoneDTOs != null) {
            // CORREÇÃO: Associa o usuário a cada telefone antes de adicionar à lista
            List<Telefone> novosTelefones = telefoneDTOs.stream()
                    .map(TelefoneRequestDTO::toEntity)
                    .peek(telefone -> telefone.setUsuario(usuario)) // Associa o usuário ao telefone
                    .collect(Collectors.toList());
            usuario.getTelefone().addAll(novosTelefones);
        }
    }

    @Override
    @Transactional
    public void updateTelefoneEspecifico(String email, Long idTelefone, TelefoneRequestDTO telefoneDTO) {
        Cliente cliente = clienteRepository.findByUsuario(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Cliente não encontrado");

        // Esta lógica já está correta, pois busca um telefone existente e gerenciado
        Telefone telefone = cliente.getUsuario().getTelefone()
                .stream()
                .filter(a -> a.getId().equals(idTelefone))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Telefone nao encontrado"));

        telefone.setNumero(telefoneDTO.numero());
        telefone.setDdd(telefoneDTO.ddd());
        telefone.setPrincipal(telefoneDTO.principal());
        telefone.setTipo(TipoTelefone.valueOf(telefoneDTO.idTipoTelefone()));
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

    @Override
    @Transactional
    public ClienteResponseDTO clienteFromFuncionario(String email) {
        Cliente verificarCliente = clienteRepository.findByUsuario(email);
        if(verificarCliente != null)
            throw new ValidationException("Já existe um cliente cadastrado para este usuario");

        if (funcionarioRepository.findByUsuario(email) == null)
            throw new ValidationException("Não existe nenhum funcionário cadastrado com esse e-mail. Crie uma nova conta");

        Funcionario funcionario = funcionarioRepository.findByUsuario(email);
        Cliente cliente = new Cliente();

        cliente.setUsuario(funcionario.getUsuario());
        Usuario usuario = funcionario.getUsuario();
        cliente.setDataCadastro(LocalDateTime.now());
        if(usuario.getPerfis() == null)
            usuario.setPerfis(new ArrayList<>());
        usuario.getPerfis().add(Perfil.USER);

        // Preciso fazer a lista de cartão

        clienteRepository.persist(cliente);
        return ClienteResponseDTO.valueOf(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id);
        if(cliente == null){
            throw new ClienteNotFoundException("Cliente não encontrado para o id: "+ id);
        }
        return ClienteResponseDTO.valueOf(cliente);
    }

    @Override
    public List<ClienteResponseDTO> findByNome(String nome) {
        List<Cliente> clientes = clienteRepository.findByNome(nome);
        if(clientes == null || clientes.isEmpty()){
            throw new ClienteNotFoundException("Nenhum cliente encontrado para o nome: "+ nome);
        }
        return clientes.stream()
                .map(ClienteResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDTO::valueOf).toList();
    }

    @Override
    public ClienteResponseDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Cliente não encontrado para o email: "+ email);
        return ClienteResponseDTO.valueOf(cliente);
    }

    @Override
    public ClienteResponseDTO findByUsuario(String email) {
        var cliente = clienteRepository.findByUsuario(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Usuario não encontrado para o email: "+ email);
        return ClienteResponseDTO.valueOf(cliente);
    }



    @Override
    public ClienteResponseDTO getMinhasInformacoes(String email) {
        Cliente cliente = clienteRepository.findByUsuario(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Informações de usuário não encontradas");
        return ClienteResponseDTO.valueOf(cliente);
    }

    // ... (o restante dos seus métodos de patch, que já estavam corretos) ...
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
}
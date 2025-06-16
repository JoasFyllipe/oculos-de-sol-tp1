package io.github.JoasFyllipe.service.pagamento;

import io.github.JoasFyllipe.dto.pagamento.CartaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.CartaoResponseDTO;
import io.github.JoasFyllipe.exceptions.CartaoNotFoundException;
import io.github.JoasFyllipe.exceptions.UsuarioNotFoundException;
import io.github.JoasFyllipe.model.pagamento.Cartao;
import io.github.JoasFyllipe.model.pagamento.ModalidadeCartao;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.repository.CartaoRepository;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import io.github.JoasFyllipe.service.usuario.HashService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CartaoServiceImpl implements CartaoService {

    @Inject
    CartaoRepository cartaoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    HashService hashService;

    @Override
    @Transactional
    public CartaoResponseDTO create(String email, CartaoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário com e-mail " + email + " não encontrado.");
        }

        Cartao cartao = new Cartao();
        cartao.setTitular(dto.titular());
        cartao.setCpfCartao(dto.cpfCartao());
        cartao.setNumero(dto.numero());
        cartao.setCvc(hashService.getHashSenha(dto.cvc()));
        cartao.setUsuario(usuario);

        // CORREÇÃO 1: Converte a data de String "MM/yy" para LocalDate
        cartao.setDataValidade(converterEValidarData(dto.dataValidade()));

        ModalidadeCartao modalidade = ModalidadeCartao.valueOf(dto.idModalidadeCartao());
        cartao.setModalidadeCartao(modalidade);

        cartaoRepository.persist(cartao);
        return CartaoResponseDTO.valueOf(cartao);
    }

    @Override
    @Transactional
    public void update(String email, Long cartaoId, CartaoRequestDTO dto) {
        Cartao cartao = cartaoRepository.findByUsuarioEmailAndId(email, cartaoId);
        if (cartao == null) {
            throw new CartaoNotFoundException("Cartão com ID " + cartaoId + " não encontrado para este usuário.");
        }

        cartao.setTitular(dto.titular());
        cartao.setCpfCartao(dto.cpfCartao());

        // CORREÇÃO 1: Converte a data de String "MM/yy" para LocalDate
        cartao.setDataValidade(converterEValidarData(dto.dataValidade()));

        ModalidadeCartao modalidade = ModalidadeCartao.valueOf(dto.idModalidadeCartao());
        cartao.setModalidadeCartao(modalidade);

    }

    @Override
    public CartaoResponseDTO findById(String email, Long cartaoId) {
        Cartao cartao = cartaoRepository.findByUsuarioEmailAndId(email, cartaoId);
        if (cartao == null) {
            throw new CartaoNotFoundException("Cartão com ID " + cartaoId + " não encontrado para este usuário.");
        }
        return CartaoResponseDTO.valueOf(cartao);
    }

    @Override
    public List<CartaoResponseDTO> findByUsuario(String email) {
        List<Cartao> listaCartoes = cartaoRepository.findByUsuarioEmail(email);
        return listaCartoes.stream()
                .map(CartaoResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String email, Long cartaoId) {
        Cartao cartao = cartaoRepository.findByUsuarioEmailAndId(email, cartaoId);
        if (cartao == null) {
            throw new CartaoNotFoundException("Cartão com ID " + cartaoId + " não encontrado para este usuário.");
        }
        cartaoRepository.delete(cartao);
    }

    // NOVO: Método auxiliar privado para tratar a data
    private LocalDate converterEValidarData(String dataString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth yearMonth = YearMonth.parse(dataString, formatter);
            LocalDate dataValidade = yearMonth.atEndOfMonth(); // Pega o último dia do mês

            // Verifica se a data de validade já passou
            if (dataValidade.isBefore(LocalDate.now())) {
                throw new BadRequestException("A data de validade do cartão já passou.");
            }
            return dataValidade;
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Formato da data de validade inválido. Use MM/YY.");
        }
    }
}
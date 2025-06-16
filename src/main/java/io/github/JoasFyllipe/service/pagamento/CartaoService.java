package io.github.JoasFyllipe.service.pagamento;

import io.github.JoasFyllipe.dto.pagamento.CartaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.CartaoResponseDTO;
import java.util.List;

public interface CartaoService {
    CartaoResponseDTO create(String email, CartaoRequestDTO dto);

    CartaoResponseDTO findById(String email, Long cartaoId);

    List<CartaoResponseDTO> findByUsuario(String email);

    void update(String email, Long cartaoId, CartaoRequestDTO dto);

    void delete(String email, Long cartaoId);
}
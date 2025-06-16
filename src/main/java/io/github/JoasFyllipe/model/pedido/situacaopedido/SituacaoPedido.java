package io.github.JoasFyllipe.model.pedido.situacaopedido;

import io.github.JoasFyllipe.model.usuario.Perfil;

public enum SituacaoPedido {
    AGUARDANDO_PAGAMENTO(1, "Aguardando Pagamento"),
    PAGAMENTO_AUTORIZADO(2, "Pagamento Autorizado"),
    PAGAMENTO_RECUSADO(3, "Pagamento Recusado"),
    EM_SEPARACAO(4, "Em Separação"),
    ENVIADO(5, "Enviado"),
    ENTREGUE(6, "Entregue"),
    CANCELADO(7, "Cancelado"),
    DEVOLVIDO(8, "Devolvido");


    private final Integer id;
    private final String descricao;

    SituacaoPedido(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getId() {
        return id;
    }

    public static SituacaoPedido valueOf(Integer id){
        if(id == null)
            return null;
        for(SituacaoPedido s : SituacaoPedido.values()){
            if(s.getId().equals(id)){
                return s;
            }
        }
        throw new IllegalArgumentException("Id inválido");
    }
}
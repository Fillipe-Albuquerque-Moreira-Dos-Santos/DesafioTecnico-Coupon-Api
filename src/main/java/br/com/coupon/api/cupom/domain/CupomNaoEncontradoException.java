package br.com.coupon.api.cupom.domain;

public class CupomNaoEncontradoException extends CupomException {

    public CupomNaoEncontradoException(Long id) {
        super("Cupom não encontrado: " + id);
    }

}

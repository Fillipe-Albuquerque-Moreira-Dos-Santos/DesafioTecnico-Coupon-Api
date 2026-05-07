package br.com.coupon.api.cupom.web;

import br.com.coupon.api.cupom.application.CupomService;
import br.com.coupon.api.cupom.domain.Cupom;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CupomController {

    private final CupomService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CupomResponse criar(@Valid @RequestBody CupomRequest req) {

        Cupom cupom = service.criar(req.getCode(), req.getDescription(), req.getDiscountValue(), req.getExpirationDate(), req.isPublished());
        return CupomResponse.from(cupom);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}

package br.com.coupon.api.cupom.mapper;

import br.com.coupon.api.dto.CupomDTO;
import br.com.coupon.api.cupom.entity.Cupom;
import org.springframework.stereotype.Component;

@Component
public class CupomMapper {

    public Cupom toEntity(CupomDTO dto) {
        Cupom cupom = new Cupom();
        cupom.setDescription(dto.getDescription());
        cupom.setDiscountValue(dto.getDiscountValue());
        cupom.setExpirationDate(dto.getExpirationDate());
        cupom.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
        cupom.setDeleted(false);
        return cupom;
    }

    public void updateEntity(CupomDTO dto, Cupom cupom) {
        cupom.setDescription(dto.getDescription());
        cupom.setDiscountValue(dto.getDiscountValue());
        cupom.setExpirationDate(dto.getExpirationDate());
        cupom.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
    }

    public CupomDTO toDto(Cupom cupom) {
        return new CupomDTO(
                cupom.getCode(),
                cupom.getDescription(),
                cupom.getDiscountValue(),
                cupom.getExpirationDate(),
                cupom.getPublished()
        );
    }
}
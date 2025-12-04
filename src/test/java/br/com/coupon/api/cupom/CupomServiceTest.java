package br.com.coupon.api.cupom;

import br.com.coupon.api.cupom.entity.Cupom;
import br.com.coupon.api.cupom.exception.CupomException;
import br.com.coupon.api.cupom.mapper.CupomMapper;
import br.com.coupon.api.cupom.repository.CupomRepository;
import br.com.coupon.api.cupom.service.CupomService;
import br.com.coupon.api.cupom.validator.CupomValidator;
import br.com.coupon.api.dto.CupomDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CupomServiceTest {

    @Mock
    private CupomRepository repository;

    @Mock
    private CupomValidator validator;

    @Mock
    private CupomMapper mapper;

    @InjectMocks
    private CupomService service;

    @Test
    void deveCriarCupomComCamposObrigatorios() {
        CupomDTO dto = new CupomDTO();
        dto.setCode("ABC123");
        dto.setDescription("Teste");
        dto.setDiscountValue(BigDecimal.valueOf(10.0));
        dto.setExpirationDate(LocalDate.now().plusDays(30));

        Cupom cupom = new Cupom();
        cupom.setId(1L);
        cupom.setCode("ABC123");

        doNothing().when(validator).validarDataExpiracao(any(LocalDate.class));
        when(validator.processarCodigo("ABC123")).thenReturn("ABC123");
        doNothing().when(validator).validarCodigoUnico("ABC123");
        when(mapper.toEntity(dto)).thenReturn(cupom);
        when(repository.save(cupom)).thenReturn(cupom);

        Cupom result = service.criar(dto);

        assertNotNull(result.getId());
        assertEquals("ABC123", result.getCode());
    }

    @Test
    void deveCriarCupomComCodigoProcessado() {
        CupomDTO dto = new CupomDTO();
        dto.setCode("AB@C#12");
        dto.setDescription("Teste");
        dto.setDiscountValue(BigDecimal.valueOf(15.0));
        dto.setExpirationDate(LocalDate.now().plusDays(20));

        Cupom cupom = new Cupom();
        cupom.setId(1L);
        cupom.setCode("ABC123");

        doNothing().when(validator).validarDataExpiracao(any(LocalDate.class));
        when(validator.processarCodigo("AB@C#12")).thenReturn("ABC123");
        doNothing().when(validator).validarCodigoUnico("ABC123");
        when(mapper.toEntity(dto)).thenReturn(cupom);
        when(repository.save(cupom)).thenReturn(cupom);

        Cupom result = service.criar(dto);

        assertEquals("ABC123", result.getCode());
    }

    @Test
    void deveCriarCupomComValorMinimo() {
        CupomDTO dto = new CupomDTO();
        dto.setCode("MIN050");
        dto.setDescription("Teste");
        dto.setDiscountValue(BigDecimal.valueOf(0.5));
        dto.setExpirationDate(LocalDate.now().plusDays(5));

        Cupom cupom = new Cupom();
        cupom.setId(1L);
        cupom.setDiscountValue(BigDecimal.valueOf(0.5));

        doNothing().when(validator).validarDataExpiracao(any(LocalDate.class));
        when(validator.processarCodigo("MIN050")).thenReturn("MIN050");
        doNothing().when(validator).validarCodigoUnico("MIN050");
        when(mapper.toEntity(dto)).thenReturn(cupom);
        when(repository.save(cupom)).thenReturn(cupom);

        Cupom result = service.criar(dto);

        assertEquals(BigDecimal.valueOf(0.5), result.getDiscountValue());
    }

    @Test
    void naoDeveCriarCupomComDataPassada() {
        CupomDTO dto = new CupomDTO();
        dto.setCode("PAST01");
        dto.setDescription("Teste");
        dto.setDiscountValue(BigDecimal.valueOf(10.0));
        dto.setExpirationDate(LocalDate.now().minusDays(1));

        doThrow(new CupomException("Data inválida")).when(validator).validarDataExpiracao(any(LocalDate.class));

        assertThrows(CupomException.class, () -> service.criar(dto));
    }

    @Test
    void deveCriarCupomPublicado() {
        CupomDTO dto = new CupomDTO();
        dto.setCode("PUB123");
        dto.setDescription("Teste");
        dto.setDiscountValue(BigDecimal.valueOf(5.0));
        dto.setExpirationDate(LocalDate.now().plusDays(10));
        dto.setPublished(true);

        Cupom cupom = new Cupom();
        cupom.setId(1L);
        cupom.setPublished(true);

        doNothing().when(validator).validarDataExpiracao(any(LocalDate.class));
        when(validator.processarCodigo("PUB123")).thenReturn("PUB123");
        doNothing().when(validator).validarCodigoUnico("PUB123");
        when(mapper.toEntity(dto)).thenReturn(cupom);
        when(repository.save(cupom)).thenReturn(cupom);

        Cupom result = service.criar(dto);

        assertTrue(result.getPublished());
    }

    @Test
    void deveDeletarCupomComSoftDelete() {
        Long id = 1L;
        Cupom cupom = new Cupom();
        cupom.setId(id);
        cupom.setDeleted(false);

        when(repository.findById(id)).thenReturn(Optional.of(cupom));
        doNothing().when(validator).validarCupomNaoDeletado(false);
        when(repository.save(any(Cupom.class))).thenAnswer(inv -> {
            cupom.setDeleted(true);
            return cupom;
        });

        service.deletar(id);

        assertTrue(cupom.getDeleted());
    }

    @Test
    void naoDeveDeletarCupomJaDeletado() {
        Long id = 1L;
        Cupom cupom = new Cupom();
        cupom.setId(id);
        cupom.setDeleted(true);

        when(repository.findById(id)).thenReturn(Optional.of(cupom));
        doThrow(new CupomException("Já deletado")).when(validator).validarCupomNaoDeletado(true);

        assertThrows(CupomException.class, () -> service.deletar(id));
    }
}

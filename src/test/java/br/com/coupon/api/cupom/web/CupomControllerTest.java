package br.com.coupon.api.cupom.web;

import br.com.coupon.api.cupom.application.CupomService;
import br.com.coupon.api.cupom.domain.Cupom;
import br.com.coupon.api.cupom.domain.CupomException;
import br.com.coupon.api.cupom.domain.CupomNaoEncontradoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CupomControllerTest {

    private static final BigDecimal DESCONTO = new BigDecimal("10.00");
    private static final LocalDate AMANHA = LocalDate.now().plusDays(1);

    @Mock
    private CupomService service;

    @InjectMocks
    private CupomController controller;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(converter)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void criaCupomERetorna201ComCorpo() throws Exception {
        Cupom retornadoPeloService = Cupom.criar("ABC123", "Promo", DESCONTO, AMANHA, true);
        when(service.criar(any(), any(), any(), any(), anyBoolean())).thenReturn(retornadoPeloService);

        mvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("AB@C12X", true)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.published").value(true));

        verify(service).criar(eq("AB@C12X"), eq("Promo"), eq(new BigDecimal("10.00")), eq(AMANHA), eq(true));
    }

    @Test
    void quandoServiceLancaCupomExceptionRetorna400() throws Exception {
        when(service.criar(any(), any(), any(), any(), anyBoolean()))
                .thenThrow(new CupomException("Código já cadastrado: ABC123"));

        mvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("ABC123", false)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Código já cadastrado: ABC123"));
    }

    @Test
    void deletaERetorna204() throws Exception {
        mvc.perform(delete("/coupon/1")).andExpect(status().isNoContent());

        verify(service).deletar(1L);
    }

    @Test
    void quandoServiceLancaNaoEncontradoRetorna404() throws Exception {
        doThrow(new CupomNaoEncontradoException(99L)).when(service).deletar(99L);

        mvc.perform(delete("/coupon/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cupom não encontrado: 99"));
    }

    @Test
    void bodyVazioRetorna400ComErrosPorCampo() throws Exception {
        mvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    private static String body(String code, boolean published) {
        return """
                {"code":"%s","description":"Promo","discountValue":10.00,"expirationDate":"%s","published":%s}
                """.formatted(code, AMANHA, published);
    }
}

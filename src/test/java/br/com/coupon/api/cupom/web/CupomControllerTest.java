package br.com.coupon.api.cupom.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CupomControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void criaCupomNormalizandoCodigo() throws Exception {
        mvc.perform(post("/coupon").contentType(MediaType.APPLICATION_JSON).content(body("AB@C#12X", true)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC12X"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    void rejeitaCodigoDuplicado() throws Exception {
        mvc.perform(post("/coupon").contentType(MediaType.APPLICATION_JSON).content(body("DUPLIC", false)))
                .andExpect(status().isCreated());

        mvc.perform(post("/coupon").contentType(MediaType.APPLICATION_JSON).content(body("DUPLIC", false)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deletaEFalhaAoTentarDeletarNovamente() throws Exception {
        MvcResult created = mvc.perform(post("/coupon").contentType(MediaType.APPLICATION_JSON).content(body("DELETE", false)))
                .andExpect(status().isCreated()).andReturn();
        long id = extractId(created.getResponse().getContentAsString());

        mvc.perform(delete("/coupon/" + id)).andExpect(status().isNoContent());
        mvc.perform(delete("/coupon/" + id)).andExpect(status().isBadRequest());
    }

    @Test
    void deletarInexistenteRetorna404() throws Exception {
        mvc.perform(delete("/coupon/9999")).andExpect(status().isNotFound());
    }

    @Test
    void rejeitaBodyVazio() throws Exception {
        mvc.perform(post("/coupon").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    private static String body(String code, boolean published) {
        return """
                {"code":"%s","description":"Promo","discountValue":10.00,"expirationDate":"%s","published":%s}
                """.formatted(code, LocalDate.now().plusDays(10), published);
    }

    private static long extractId(String json) {
        Matcher m = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(json);
        if (!m.find()) throw new IllegalStateException(json);
        return Long.parseLong(m.group(1));
    }
}

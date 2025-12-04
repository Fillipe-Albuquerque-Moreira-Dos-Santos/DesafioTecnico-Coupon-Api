package br.com.coupon.api.cupom.controller;

import br.com.coupon.api.dto.CupomDTO;
import br.com.coupon.api.cupom.entity.Cupom;
import br.com.coupon.api.cupom.service.CupomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cupons")
@RequiredArgsConstructor
@Slf4j
public class CupomViewController {

    private final CupomService service;

    // LISTAR TODOS
    @GetMapping
    public String listar(Model model) {
        List<Cupom> cupons = service.listarTodos();
        model.addAttribute("cupons", cupons);
        return "cupons"; // Vai para cupons.html
    }

    // FORMULÁRIO NOVO
    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("cupomDTO", new CupomDTO());
        return "cupom-form"; // Vai para cupom-form.html
    }

    // CRIAR CUPOM
    @PostMapping
    public String criar(@Valid @ModelAttribute CupomDTO cupomDTO,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cupom-form";
        }

        try {
            service.criar(cupomDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Cupom criado com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (RuntimeException ex) {
            log.error("Erro ao criar cupom", ex);
            redirectAttributes.addFlashAttribute("mensagem", "Erro: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/cupons";
    }

    // FORMULÁRIO EDITAR
    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cupom cupom = service.buscarPorId(id);
            CupomDTO dto = new CupomDTO(
                    cupom.getCode(),
                    cupom.getDescription(),
                    cupom.getDiscountValue(),
                    cupom.getExpirationDate(),
                    cupom.getPublished()
            );
            model.addAttribute("cupomDTO", dto);
            model.addAttribute("cupomId", id);
            return "cupom-form";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("mensagem", "Cupom não encontrado!");
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/cupons";
        }
    }

    // ATUALIZAR CUPOM
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute CupomDTO cupomDTO,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("cupomId", id);
            return "cupom-form";
        }

        try {
            service.atualizar(id, cupomDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Cupom atualizado com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (RuntimeException ex) {
            log.error("Erro ao atualizar cupom", ex);
            redirectAttributes.addFlashAttribute("mensagem", "Erro: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/cupons";
    }

    // DELETAR CUPOM
    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Cupom deletado com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (RuntimeException ex) {
            log.error("Erro ao deletar cupom", ex);
            redirectAttributes.addFlashAttribute("mensagem", "Erro: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/cupons";
    }
}
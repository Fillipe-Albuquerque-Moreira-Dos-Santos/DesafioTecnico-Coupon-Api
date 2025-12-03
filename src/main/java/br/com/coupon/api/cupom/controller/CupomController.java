package br.com.coupon.api.cupom.controller;

import br.com.coupon.api.dto.CupomDTO;
import br.com.coupon.api.cupom.entity.Cupom;
import br.com.coupon.api.cupom.service.CupomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cupons")
@RequiredArgsConstructor
@Tag(name = "Cupons", description = "API de gerenciamento de cupons")
public class CupomController {

    private final CupomService service;

    @PostMapping
    @Operation(summary = "Criar cupom", description = "Cria um novo cupom de desconto")
    public ResponseEntity<?> criar(@Valid @RequestBody CupomDTO dto) {
        try {
            Cupom cupom = service.criar(dto);
            return ResponseEntity.created(URI.create("/api/cupons/" + cupom.getId())).body(cupom);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar cupons", description = "Lista todos os cupons ativos")
    public ResponseEntity<?> listarTodos() {
        try {
            List<Cupom> cupons = service.listarTodos();
            return ResponseEntity.ok(cupons);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cupom", description = "Busca um cupom por ID")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Cupom cupom = service.buscarPorId(id);
            return ResponseEntity.ok(cupom);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cupom", description = "Deleta um cupom (soft delete)")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cupom", description = "Atualiza os dados de um cupom existente")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody CupomDTO dto) {
        try {
            Cupom atualizado = service.atualizar(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
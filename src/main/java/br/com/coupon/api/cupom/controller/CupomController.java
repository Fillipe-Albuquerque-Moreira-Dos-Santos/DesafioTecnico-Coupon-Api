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
    public ResponseEntity<Cupom> criar(@Valid @RequestBody CupomDTO dto) {
        Cupom cupom = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/api/cupons/" + cupom.getId()))
                .body(cupom);
    }

    @GetMapping
    @Operation(summary = "Listar cupons", description = "Lista todos os cupons ativos")
    public ResponseEntity<List<Cupom>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cupom", description = "Busca um cupom por ID")
    public ResponseEntity<Cupom> buscarPorId(@PathVariable Long id) {
        Cupom cupom = service.buscarPorId(id);
        return ResponseEntity.ok(cupom);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cupom", description = "Deleta um cupom (soft delete)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
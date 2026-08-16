package com.shoplite.shoplite_api.controller;

import com.shoplite.shoplite_api.dto.ProdutoRequest;
import com.shoplite.shoplite_api.dto.ProdutoResponse;
import com.shoplite.shoplite_api.model.Categoria;
import com.shoplite.shoplite_api.model.Produto;
import com.shoplite.shoplite_api.repository.CategoriaRepository;
import com.shoplite.shoplite_api.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Operation(summary = "Cadastra um novo produto", description = "Requer papel VENDEDOR ou ADMIN")
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody ProdutoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produto produto = new Produto();
        produto.setNome(request.nome());
        produto.setPreco(request.preco());
        produto.setEstoque(request.estoque());
        produto.setCategoria(categoria);
        produtoRepository.save(produto);

        ProdutoResponse response = ProdutoResponse.builder()
            .id(produto.getId())
            .nome(produto.getNome())
            .preco(produto.getPreco())
            .estoque(produto.getEstoque())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }
}
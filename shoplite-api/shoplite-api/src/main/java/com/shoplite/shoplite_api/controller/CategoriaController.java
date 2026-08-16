package com.shoplite.shoplite_api.controller;

import com.shoplite.shoplite_api.model.Categoria;
import com.shoplite.shoplite_api.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

   
    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.salvar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

   
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        List<Categoria> lista = categoriaService.listarTodas();
        return ResponseEntity.ok(lista);
    }
}

package com.example.demo.controller;

import com.example.demo.domain.ItemEstoque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.demo.service.EstoqueService;


@RestController // Dizer ao Spring que isto é uma API
@RequestMapping("/api/estoque") // O endereço da minha API, ou seja o endpoint
public class EstoqueController {
    // Simulando um banco de dados com um item
    //private ItemEstoque item = new ItemEstoque(1L,"Teclado mecanico",100);

    @Autowired
    private EstoqueService service; //chama o serviço não diretamente o repositorio, pois o serviço é a camada de negócios, ou seja, ele é responsável por chamar o repositorio e aplicar as regras de negócios antes de salvar ou buscar os dados.

    @GetMapping
    public List<ItemEstoque> listar(){
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ItemEstoque buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
        public void deletar(@PathVariable Long id) {
            service.excluir(id);
        }

    @PostMapping
    public ItemEstoque criar(@RequestBody ItemEstoque item){
        // O @RequestBody faz o Spring ler o JSON que você enviar e transformar em Objeto Java
        return service.salvar(item);
    }

  

    
}

package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.domain.ItemEstoque;
import com.example.demo.repository.ItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
@Service // por padrão usariamos new EstoqueService() para criar uma nova instancia, mas com o @Service o Spring cuida disso para nós, ele cria uma instancia unica e compartilha ela por toda a aplicação, ou seja, ele é um singleton
public class EstoqueService {
    @Autowired
    private ItemRepository repository;

    public ItemEstoque salvar(ItemEstoque item) {
        // Aqui você poderia colocar regras globais antes de salvar
        return repository.save(item);
    }
    public ItemEstoque buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado!"));
    }

    public List<ItemEstoque> listarTodos(){
        return repository.findAll();
    }
    public void excluir(Long id) {
        repository.deleteById(id);
    }

}

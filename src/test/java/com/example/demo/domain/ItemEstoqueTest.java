package com.example.demo.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ItemEstoqueTest {
    @Test
    void testAdicionarEstoque(){
        // Given - Dado um item de estoque com a quantia inicial de 100
        ItemEstoque item = new ItemEstoque(1L,"Teclado mecanico",100);
        
        // When - quando eu adicionor 50 unidads ao estome
        item.adicionarEstoque(50);

        // Then - então a quantia total deve ser 150
        assertEquals(150, item.getQuantidade());
    }
    
    @Test
    void naoDeveAdicionarEstoqueNegativo(){
        // Given - Dado um item de estoque com a quantia inicial de 100
        ItemEstoque item = new ItemEstoque(1L,"Teclado mecanico",100);
        
        // When - quando eu tentar adicionar uma quantia negativa
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            item.adicionarEstoque(-10);
        });

        // Then - então deve lançar uma exceção com a mensagem correta
        assertEquals("A quantia adicionada deve ser maior que zero!", exception.getMessage());
    }
}

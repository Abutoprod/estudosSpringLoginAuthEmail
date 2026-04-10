package com.example.demo.repository;
import com.example.demo.domain.ItemEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface ItemRepository extends JpaRepository<ItemEstoque, Long> {
    // Só isso! O Spring já cria o Save, Delete e Find sozinho. 
}

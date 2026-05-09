package com.example.demo.repository;

import com.example.demo.domain.ItemComanda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemComandaRepository extends JpaRepository<ItemComanda, Long> {
}
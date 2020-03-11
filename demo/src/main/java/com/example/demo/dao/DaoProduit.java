package com.example.demo.dao;

import com.example.demo.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoProduit extends JpaRepository<Produit,Long> {
}

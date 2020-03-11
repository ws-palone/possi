package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity @Data
@NoArgsConstructor @AllArgsConstructor
public class Category {
    @Id @GeneratedValue
    private Long id;
    private String libelle;
    @OneToMany(mappedBy = "category")
    private List<Produit> produits;
}

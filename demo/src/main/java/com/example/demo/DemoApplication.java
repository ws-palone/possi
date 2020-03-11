package com.example.demo;

import com.example.demo.dao.DaoCategory;
import com.example.demo.dao.DaoProduit;
import com.example.demo.model.Category;
import com.example.demo.model.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    DaoCategory daoCategory;
    @Autowired
    DaoProduit daoProduit;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Produit p = new Produit();
        Category cat = new Category();
        p.setCategory(cat);
        daoProduit.save(p);
        daoCategory.save(cat);
    }
}

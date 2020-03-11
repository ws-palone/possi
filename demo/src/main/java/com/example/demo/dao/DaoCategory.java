package com.example.demo.dao;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoCategory extends JpaRepository<Category,Long> {
}

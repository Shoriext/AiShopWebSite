package ru.onlinestore.aishopwebsite.repository;

import ru.onlinestore.aishopwebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Здесь можно добавлять кастомные запросы, Работа с БД через Spring Data JPA
    Optional<Product> findByName(String name);
}

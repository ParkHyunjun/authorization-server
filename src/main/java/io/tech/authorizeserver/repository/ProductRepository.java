package io.tech.authorizeserver.repository;

import io.tech.authorizeserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String name);
    boolean existsByName(String name);
}

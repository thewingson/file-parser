package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}

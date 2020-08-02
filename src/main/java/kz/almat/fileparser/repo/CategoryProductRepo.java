package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.Category;
import kz.almat.fileparser.model.CategoryProduct;
import kz.almat.fileparser.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Almat on 31.07.2020
 */

@Repository
public interface CategoryProductRepo extends JpaRepository<CategoryProduct, Long> {

    Optional<CategoryProduct> findByCategoryAndProduct(Category category, Product product);

}

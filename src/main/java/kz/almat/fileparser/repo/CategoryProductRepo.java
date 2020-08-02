package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Almat on 31.07.2020
 */

@Repository
public interface CategoryProductRepo extends JpaRepository<CategoryProduct, Long> {
}

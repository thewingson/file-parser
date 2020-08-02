package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Almat on 01.08.2020
 */

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {
}

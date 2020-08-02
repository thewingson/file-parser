package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.Product;
import kz.almat.fileparser.pojo.ProductExcelDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Almat
 * created on 24/07/2020
 */

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true)
    List<ProductExcelDto> findAllWithExcelData();

    @EntityGraph(value = "product-excel-entity-graph")
    List<Product> findAllByIdNotNull();

//    @EntityGraph(attributePaths = {"productCategoryList", "productImageList"})
//    List<Product> findAll();

}

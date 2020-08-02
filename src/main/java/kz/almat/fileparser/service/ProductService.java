package kz.almat.fileparser.service;

import kz.almat.fileparser.model.Product;
import kz.almat.fileparser.pojo.ProductFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Almat
 * created on 24/07/2020
 */
public interface ProductService {

    Product getProductById(Long id) throws Exception;
    List<Product> getAllProducts() throws Exception;
    void addProduct(ProductFilter productFilter) throws Exception;
    void editProduct(Long id, ProductFilter product) throws Exception;
    void deleteProduct(Long id);

    Map<String, Long> updateFromFile(MultipartFile file) throws Exception;

    ByteArrayInputStream exportToExcel() throws IOException;
}

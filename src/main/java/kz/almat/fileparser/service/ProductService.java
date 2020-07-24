package kz.almat.fileparser.service;

import kz.almat.fileparser.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Almat
 * created on 24/07/2020
 */
public interface ProductService {

    Product getProductById(Long id) throws Exception;
    List<Product> getAllProducts() throws Exception;
    void addProduct(Product product);
    void editProduct(Long id, Product product) throws Exception;
    void deleteProduct(Long id);

    void updateFromFile(MultipartFile file) throws Exception;
}

package kz.almat.fileparser.service.impl;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import kz.almat.fileparser.model.Product;
import kz.almat.fileparser.pojo.xls.ProductEntity;
import kz.almat.fileparser.repo.ProductRepo;
import kz.almat.fileparser.service.ProductService;
import kz.almat.fileparser.util.XlsxFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final XlsxFileParser xlsxFileParser;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo,
                              @Qualifier("xlsx") XlsxFileParser xlsxFileParser) {
        this.productRepo = productRepo;
        this.xlsxFileParser = xlsxFileParser;
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        Optional<Product> product = productRepo.findById(id);
        if(product.isEmpty()){
            throw new Exception("NO PRODUCT");
        }
        return product.get();
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        List<Product> products = productRepo.findAll();
        if(products.isEmpty()){
            throw new Exception("NO PRODUCT");
        }
        return products;
    }

    @Override
    public void addProduct(Product product) {
        productRepo.save(product);
    }

    @Override
    public void editProduct(Long id, Product product) throws Exception {
        try {
            Product productById = getProductById(id);
            productById.setName(product.getName());
            productRepo.save(product);
        } catch (Exception e) {
            throw new Exception("INVALID ID");
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public void updateFromFile(MultipartFile file) throws Exception {

        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename()); // TODO: System.getProperty("java.io.tmpdir") получать из FileUtilClass
        file.transferTo(convFile);

        InputStream stream = new FileInputStream(convFile);
        List<ProductEntity> products = Poiji.fromExcel(stream, PoijiExcelType.XLSX, ProductEntity.class);
        System.out.println(products);
    }
}

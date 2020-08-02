package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.Image;
import kz.almat.fileparser.model.Product;
import kz.almat.fileparser.model.ProductImage;
import kz.almat.fileparser.pojo.xls.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * @author Almat on 01.08.2020
 */

@Repository
public class ProductDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductImageRepo productImageRepo;

    @Autowired
    private ImageRepo imageRepo;

    private final int BATCH_SIZE = 10000;

    @Transactional
    public int updateFromExcel(List<Product> productList, Map<Long, ProductEntity> productEntityMap) {

        int updatableCycle = 0;
        int batch = 0;
        long beforeBatchCycle = System.currentTimeMillis();
        for (Product p : productList) {

            if (updatableCycle != 0 && updatableCycle % BATCH_SIZE == 0) {
                System.out.println(" ------------------------------- ");
                batch++;
                System.out.println("Batch : " + batch);
                long beforeFlushing = System.currentTimeMillis();
                entityManager.flush();
                long timeForFlush = System.currentTimeMillis() - beforeFlushing;
                System.out.println("timeForFlush: " + timeForFlush + "mls");
                long timeForBatchCycle = System.currentTimeMillis() - beforeBatchCycle;
                System.out.println("timeForBatchCycle: " + timeForBatchCycle + "mls");
                System.out.println(" ####################################### ");
                beforeBatchCycle = System.currentTimeMillis();
            }

            ProductEntity productFromExcel = productEntityMap.get(p.getId());
            if (productFromExcel != null) {
                boolean productUpdates = false;
                if (!p.getName().equals(productFromExcel.getName())) {
                    p.setName(productFromExcel.getName());
                    productUpdates = true;
                    productRepo.save(p);
                }
                if (!p.getDescription().equals(productFromExcel.getDescription())) {
                    p.setDescription(productFromExcel.getDescription());
                    productUpdates = true;
                    productRepo.save(p);
                }
                if (!p.getShortDescription().equals(productFromExcel.getShortDescription())) {
                    p.setShortDescription(productFromExcel.getShortDescription());
                    productUpdates = true;
                    productRepo.save(p);
                }
                if (!p.getIngredients().equals(productFromExcel.getIngredients())) {
                    p.setIngredients(productFromExcel.getIngredients());
                    productUpdates = true;
                    productRepo.save(p);
                }
                if (!p.getCcal().equals(productFromExcel.getCcal())) {
                    p.setCcal(productFromExcel.getCcal());
                    productUpdates = true;
                    productRepo.save(p);
                }
                if (!p.getWeight().equals(productFromExcel.getWeight())) {
                    p.setWeight(productFromExcel.getWeight());
                    productUpdates = true;
                    productRepo.save(p);
                }
                if (!p.getDeleted().equals(productFromExcel.getDeleted())) {
                    p.setDeleted(productFromExcel.getDeleted());
                    productUpdates = true;
                    productRepo.save(p);
                }

                productUpdates = updateImage(productFromExcel, p, productUpdates);

                if (productUpdates) {
                    updatableCycle++;
                }
            }
        }
        System.out.println(" ------------------------------- ");
        batch++;
        System.out.println("Final Batch : " + batch);
        entityManager.flush();
        entityManager.clear();
        System.out.println(" ####################################### ");

        return updatableCycle;
    }

    public boolean updateImage(ProductEntity productFromExcel, Product product, boolean productUpdates){
        if(productFromExcel.getImgFull() != null){
            if(product.getProductImageList() != null){
                if(product.getProductImageList().get(0).getImage() != null){
                    if(product.getProductImageList().get(0).getImage().getImgUrl() == null ||
                            !product.getProductImageList().get(0).getImage().getImgUrl().equals(productFromExcel.getImgFull())){
                            product.getProductImageList().get(0).getImage().setImgUrl(productFromExcel.getImgFull());
                            imageRepo.save(product.getProductImageList().get(0).getImage());
                            productUpdates = true;

                        return productUpdates;
                    } else {
                        return productUpdates;
                    }
                }else {
                    Image image = new Image();
                    image.setImgUrl(productFromExcel.getImgFull());
                    imageRepo.save(image);

                    product.getProductImageList().get(0).setImage(image);
                    productImageRepo.save(product.getProductImageList().get(0));
                    productUpdates = true;

                    return productUpdates;
                }
            }else {
                Image image = new Image();
                image.setImgUrl(productFromExcel.getImgFull());
                imageRepo.save(image);

                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImage(image);
                productImageRepo.save(productImage);
                productUpdates = true;

                return productUpdates;
            }
        }

        return productUpdates;
    }
}

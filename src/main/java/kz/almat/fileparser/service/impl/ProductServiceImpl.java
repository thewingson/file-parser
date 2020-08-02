package kz.almat.fileparser.service.impl;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import kz.almat.fileparser.model.*;
import kz.almat.fileparser.pojo.ProductExcelDto;
import kz.almat.fileparser.pojo.ProductFilter;
import kz.almat.fileparser.pojo.xls.ProductEntity;
import kz.almat.fileparser.repo.*;
import kz.almat.fileparser.service.ProductService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final CategoryProductRepo categoryProductRepo;
    private final ImageRepo imageRepo;
    private final ProductImageRepo productImageRepo;
    private final ProductDao productDao;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo,
                              CategoryRepo categoryRepo,
                              CategoryProductRepo categoryProductRepo,
                              ImageRepo imageRepo,
                              ProductImageRepo productImageRepo,
                              ProductDao productDao) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.categoryProductRepo = categoryProductRepo;
        this.imageRepo = imageRepo;
        this.productImageRepo = productImageRepo;
        this.productDao = productDao;
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new Exception("NO PRODUCT");
        }
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        List<Product> products = productRepo.findAllByIdNotNull();
        if (products.isEmpty()) {
            throw new Exception("NO PRODUCT");
        }
        return products;
    }

    @Transactional
    @Override
    public void addProduct(ProductFilter productFilter) throws Exception {
        Optional<Category> category = categoryRepo.findById(productFilter.getCategoryId());
        Optional<Image> image = imageRepo.findById(productFilter.getImageId());
        if (category.isPresent() && image.isPresent()) {
            Product product = new Product();
            product.setName(productFilter.getName());
            product.setDescription(productFilter.getDescription());
            productRepo.save(product);

            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategory(category.get());
            categoryProduct.setProduct(product);
            categoryProductRepo.save(categoryProduct);

            ProductImage productImage = new ProductImage();
            productImage.setImage(image.get());
            productImage.setProduct(product);
            productImageRepo.save(productImage);
        } else {
            throw new Exception("NO CATEGORY");
        }
    }

    @Transactional
    @Override
    public void editProduct(Long id, ProductFilter productFilter) throws Exception {
        Optional<Product> product = productRepo.findById(id);
        if(!product.isPresent()){
            throw new Exception("INVALID PRODUCT ID");
        }
        Optional<Category> category = categoryRepo.findById(productFilter.getCategoryId());
        if(!category.isPresent()){
            Category categoryToCreate = new Category();
            categoryToCreate.setName(productFilter.getName()); // getCategoryName();
            categoryRepo.save(categoryToCreate);

            CategoryProduct categoryProductToCreate = new CategoryProduct();
            categoryProductToCreate.setProduct(product.get());
            categoryProductToCreate.setCategory(categoryToCreate);
            categoryProductRepo.save(categoryProductToCreate);
        } else {
            Optional<CategoryProduct> categoryProduct = categoryProductRepo.findByCategoryAndProduct(category.get(), product.get());
            if(!categoryProduct.isPresent()){
                CategoryProduct categoryProductToCreate = new CategoryProduct();
                categoryProductToCreate.setProduct(product.get());
                categoryProductToCreate.setCategory(category.get());
                categoryProductRepo.save(categoryProductToCreate);
            }
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public Map<String, Long> updateFromFile(MultipartFile file) throws Exception {

        // get input stream from file
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename()); // TODO: System.getProperty("java.io.tmpdir") получать из FileUtilClass
        file.transferTo(convFile);
        InputStream stream = new FileInputStream(convFile);

        PoijiExcelType excelType = PoijiExcelType.XLS;
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
            excelType = PoijiExcelType.XLSX;
        }
        // file parsing
        long beforeParsingExcel = System.currentTimeMillis();
        List<ProductEntity> products = Poiji.fromExcel(stream, excelType, ProductEntity.class);
        long timeForParsing = System.currentTimeMillis() - beforeParsingExcel;
        System.out.println("timeForParsing: " + timeForParsing);

        long beforeMappingExcel = System.currentTimeMillis();
        Map<Long, ProductEntity> map =
                products.stream().collect(Collectors.toMap(ProductEntity::getId, item -> item));
        long timeForMapping = System.currentTimeMillis() - beforeMappingExcel;
        System.out.println("timeForMapping: " + timeForMapping);

        long beforeSelectingAll = System.currentTimeMillis();
        List<Product> productList = productRepo.findAllByIdNotNull();
        long timeForSelecting = System.currentTimeMillis() - beforeSelectingAll;
        System.out.println("timeForSelecting: " + timeForSelecting);

        long beforeUpdatingAll = System.currentTimeMillis();
        int updated = productDao.updateFromExcel(productList, map);
        long timeForUpdating = System.currentTimeMillis() - beforeUpdatingAll;
        System.out.println("timeForUpdating: " + timeForUpdating);

        Map<String, Long> data = new HashMap<>();
        data.put("updated", (long) updated);
        data.put("timeForParsing", timeForParsing);
        data.put("timeForSelecting", timeForSelecting);
        data.put("timeForMapping", timeForMapping);
        data.put("timeForUpdating", timeForUpdating);
        return data;

    }

    @Override
    public ByteArrayInputStream exportToExcel() throws IOException {
        long beforeSelecting = System.currentTimeMillis();
        List<ProductExcelDto> excelData = productRepo.findAllWithExcelData();
        long timeForSelecting = System.currentTimeMillis() - beforeSelecting;
        System.out.println("timeForSelecting: " + timeForSelecting);
        String[] COLUMNS = {"ID",
                "Name",
                "Description",
                "Short Description",
                "Ingredients",
                "Weight",
                "Ccal",
                "Deleted",
                "Image Full",
                "Category ID",
                "Category Name"};

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Products");
            // write header row
            writeHeader(sheet, COLUMNS);
            // write records
            long beforeGeneratingFile = System.currentTimeMillis();
            writeRecords(sheet, excelData);
            long timeForGeneratingFile = System.currentTimeMillis() - beforeGeneratingFile;
            System.out.println("timeForGeneratingFile: " + timeForGeneratingFile);

            long beforeWriting = System.currentTimeMillis();
            workbook.write(out);
            long timeForWriting = System.currentTimeMillis() - beforeWriting;
            System.out.println("timeForWriting: " + timeForWriting);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void writeHeader(Sheet sheet, String[] COLUMNS) {
        // Row for Header
        Row headerRow = sheet.createRow(0);
        // Header
        for (int col = 0; col < COLUMNS.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNS[col]);
        }
    }

    private void writeRecords(Sheet sheet, List<ProductExcelDto> excelData) {
        int rowIdx = 1;
        for (ProductExcelDto item : excelData) {
            Row row = sheet.createRow(rowIdx++);

            if (item.getId() != null) {
                row.createCell(0).setCellValue(item.getId());
            }
            if (item.getName() != null) {
                row.createCell(1).setCellValue(item.getName());
            }
            if (item.getDescription() != null) {
                row.createCell(2).setCellValue(item.getDescription());
            }
            if (item.getShortDescription() != null) {
                row.createCell(3).setCellValue(item.getShortDescription());
            }
            if (item.getIngredients() != null) {
                row.createCell(4).setCellValue(item.getIngredients());
            }
            if (item.getWeight() != null) {
                row.createCell(5).setCellValue(item.getWeight());
            }
            if (item.getCcal() != null) {
                row.createCell(6).setCellValue(item.getCcal());
            }
            if (item.getDeleted() != null) {
                row.createCell(7).setCellValue(item.getDeleted());
            }
            if (item.getImgFull() != null) {
                row.createCell(8).setCellValue(item.getImgFull());
            }
            if (item.getCategoryId() != null) {
                row.createCell(9).setCellValue(item.getCategoryId());
            }
            if (item.getCategoryName() != null) {
                row.createCell(10).setCellValue(item.getCategoryName());
            }
        }
    }
}

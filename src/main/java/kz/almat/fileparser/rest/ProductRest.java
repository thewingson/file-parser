package kz.almat.fileparser.rest;

import kz.almat.fileparser.model.Product;
import kz.almat.fileparser.pojo.xls.ProductEntity;
import kz.almat.fileparser.service.ProductService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Almat
 * created on 24/07/2020
 */

@RestController
@RequestMapping("/api/product")
public class ProductRest {

    private final ProductService productService;

    @Autowired
    public ProductRest(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(productService.getAllProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Product product) {
        try {
            productService.addProduct(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") Long id,
                                  @RequestBody Product product) {
        try {
            productService.editProduct(id, product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/update-from-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateFromFile(@RequestParam("file") MultipartFile file) {
//        try {
//            productService.updateFromFile(file);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }

        File filExcell = new File("document.xls");

        try {
            FileInputStream excelFile = new FileInputStream(filExcell);
            Workbook workbook =  new HSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);

            int rowSize = datatypeSheet.getLastRowNum() + 1;
            int colSize = datatypeSheet.getRow(0).getLastCellNum();
            for (int i = 0; i < rowSize; i++) {
                for (int j = 0; j < colSize; j++) {
                    Cell currentCell = datatypeSheet.getRow(i).getCell(j);
                    System.out.print("[" + currentCell.toString() + "]");
                }
                System.out.println();
            }

            return null;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Workbook getWorkbook(FileInputStream excelFile, String fileName) { // TODO: !!! сделать общим методом, не private
        try {
            if (fileName.endsWith(".xlsx")) {
                return new XSSFWorkbook(excelFile);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Загруженный файл не является Excel файлом. Попробуйте ещё раз.");
        }
        return null;
    }

}

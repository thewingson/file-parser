package kz.almat.fileparser.util;

import kz.almat.fileparser.pojo.xls.AbstractXlsEntity;
import kz.almat.fileparser.pojo.xls.ProductEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Service("xlsx")
public class XlsxFileParser implements FileParser {

    @Override
    public <E extends AbstractXlsEntity> List<E> read(MultipartFile file, Class<E> entity) throws IOException {

        List<E> resultList = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet datatypeSheet = workbook.getSheetAt(0); // TODO: !!! getSheetAt(0)

        int rowSize = datatypeSheet.getLastRowNum() + 1;
        int colSize = datatypeSheet.getRow(0).getLastCellNum(); // TODO: !!! getRow(0)

        ProductEntity productEntity; // TODO: !!! make abstract & and method with annotations
        for (int i = 0; i < rowSize; i++) {
            productEntity = new ProductEntity();
            for (int j = 0; j < colSize; j++) {
                Cell currentCell = datatypeSheet.getRow(i).getCell(j);
                if (currentCell != null) { // TODO:  && currentCell.getCellType() != CellType.BLANK
                    switch (j) { // TODO: annotation
                        case 0:
                            productEntity.setId((Long) this.getCellValue(currentCell));
                            break;
                        case 1:
                            productEntity.setName((String) this.getCellValue(currentCell));
                            break;
                        default:
                            break;
                    }
                }
            }
            resultList.add((E) productEntity); // TODO: !!! remove casting
        }

        return resultList;
    }

    @Override
    public void write(MultipartFile file, Class<? extends AbstractXlsEntity> entity) {

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

    private File convertMultipartToFile(MultipartFile file) throws IOException { // TODO: !!! сделать общим методом, не private
        String originalFilename = file.getOriginalFilename();
        File convFile = new File(originalFilename);
        file.transferTo(convFile);

        return convFile;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    return (long) cell.getNumericCellValue();
                }
            case BLANK:
                return "";
            case ERROR:
                return cell.getErrorCellValue();
            default:
                return null;
        }
    }

}

package kz.almat.fileparser.util;

import kz.almat.fileparser.pojo.xls.AbstractXlsEntity;
import kz.almat.fileparser.pojo.xls.ProductEntity;
import org.apache.poi.ss.usermodel.*;
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
@Service("xls")
public class XlsFileParser implements FileParser{

    @Override
    public <E extends AbstractXlsEntity> List<E> read(MultipartFile file, Class<E> clazz) throws IOException {
//        List<E> resultList = new ArrayList<E>();
        List<E> resultList = new ArrayList<>();

        FileInputStream excelFile = new FileInputStream(convertMultipartToFile(file));
        Workbook workbook = this.getWorkbook(excelFile, file.getName());

        Sheet datatypeSheet = workbook.getSheetAt(0); // TODO: !!! getSheetAt(0)

        int rowSize = datatypeSheet.getLastRowNum();
        int colSize = datatypeSheet.getRow(0).getLastCellNum(); // TODO: !!! getRow(0)

        ProductEntity entity; // TODO: !!! make abstract & and method with annotations
        for (int i = 0; i < colSize; i++) {
            Cell currentCell = datatypeSheet.getRow(0).getCell(i);
            if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                entity = new ProductEntity();
                for (int j = 0; j < rowSize; j++) {
                    currentCell = datatypeSheet.getRow(j).getCell(i);
                    if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                        switch (j) {
                            case 0:
                                entity.setId((Long) this.getCellValue(currentCell));
                                break;
                            case 1:
                                entity.setName((String) this.getCellValue(currentCell));
                                break;
                            default:
                                break;
                        }
                    }
                }
                resultList.add((E) entity); // TODO: !!! remove casting
            }
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
                    return cell.getNumericCellValue();
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

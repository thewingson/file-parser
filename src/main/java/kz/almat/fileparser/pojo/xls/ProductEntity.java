package kz.almat.fileparser.pojo.xls;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends AbstractXlsEntity{

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("ID")
    private Long id;

    @ExcelCellName("Name")
    private String name;

    @ExcelCellName("Category ID")
    private Long categoryId;

    @ExcelCellName("Category Name")
    private String categoryName;

    @ExcelCellName("Description")
    private String description;

    @ExcelCellName("Short Description")
    private String shortDescription;

    @ExcelCellName("Ingredients")
    private String ingredients;

    @ExcelCellName("Weight")
    private Float weight;

    @ExcelCellName("Ccal")
    private Float ccal;

    @ExcelCellName("Image full")
    private String imgFull;

    @ExcelCellName("Deleted")
    private Boolean deleted;

    @ExcelCellName("Created")
    private LocalDate created;


}

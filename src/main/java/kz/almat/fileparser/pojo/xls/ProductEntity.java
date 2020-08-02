package kz.almat.fileparser.pojo.xls;

import com.poiji.annotation.ExcelCellName;
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
public class ProductEntity {

    @ExcelCellName("ID")
    private Long id;

    @ExcelCellName("Name")
    private String name;

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

    @ExcelCellName("Image Full")
    private String imgFull;

    @ExcelCellName("Deleted")
    private Boolean deleted;

    @ExcelCellName("Category ID")
    private Long categoryId;

    @ExcelCellName("Category Name")
    private String categoryName;


}

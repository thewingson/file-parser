package kz.almat.fileparser.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Almat on 31.07.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductExcelDto {
    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    private String ingredients;
    private Float weight;
    private Float ccal;
    private Boolean deleted;
    private Long categoryId;
    private String categoryName;
    private String imgFull;
}

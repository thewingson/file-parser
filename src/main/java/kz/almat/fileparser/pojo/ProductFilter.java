package kz.almat.fileparser.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Almat on 31.07.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String name;
    private String description;
    private String shortDescription;
    private String ingredients;
    private Float weight;
    private Float ccal;
    private String imgFull;
    private Boolean deleted;
    private LocalDate created;
    private Long categoryId;
    private Long imageId;
}

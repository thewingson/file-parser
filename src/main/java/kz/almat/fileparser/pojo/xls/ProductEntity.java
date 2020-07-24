package kz.almat.fileparser.pojo.xls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends AbstractXlsEntity{
    private Long id;
    private String name;
}

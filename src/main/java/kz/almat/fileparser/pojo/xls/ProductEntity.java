package kz.almat.fileparser.pojo.xls;

import com.poiji.annotation.ExcelCell;
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

    @ExcelCell(0)
    private Long id;

    @ExcelCell(1)
    private String name;
}

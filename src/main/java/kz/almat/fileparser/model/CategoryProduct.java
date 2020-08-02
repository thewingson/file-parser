package kz.almat.fileparser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Almat on 31.07.2020
 */

@Entity
@Table(name = "category_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProduct {
    @Id
    @GeneratedValue(generator = "category_product_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "category_product_id_seq", name = "category_product_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "category_product_product_fk"), nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "category_product_category_fk"), nullable = false)
    private Category category;
}

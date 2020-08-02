package kz.almat.fileparser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Almat on 31.07.2020
 */

@Entity
@Table(name = "product_image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(generator = "product_image_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "product_image_id_seq", name = "product_image_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_image_product_fk"), nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "product_image_image_fk"), nullable = false)
    private Image image;
}

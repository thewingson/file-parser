package kz.almat.fileparser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Entity
@Table(name = "PRODUCT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(generator = "product_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "product_id_seq", name = "product_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

}

package kz.almat.fileparser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * @author Almat on 31.07.2020
 */

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"categoryProductList"})
public class Category {
    @Id
    @GeneratedValue(generator = "category_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "category_id_seq", name = "category_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CategoryProduct> categoryProductList;
}

package kz.almat.fileparser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.almat.fileparser.pojo.ProductExcelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @author Almat
 * created on 24/07/2020
 */

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(
        name = "itemDetailsMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ProductExcelDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "short_description", type = String.class),
                                @ColumnResult(name = "ingredients", type = String.class),
                                @ColumnResult(name = "weight", type = Float.class),
                                @ColumnResult(name = "ccal", type = Float.class),
                                @ColumnResult(name = "deleted", type = Boolean.class),
                                @ColumnResult(name = "category_id", type = Long.class),
                                @ColumnResult(name = "category_name", type = String.class),
                                @ColumnResult(name = "img_url", type = String.class)
                        }
                )
        }
)
@NamedNativeQuery(
        name = "Product.findAllWithExcelData",
        query = "select " +
                "p.id, " +
                "p.name, " +
                "p.description, " +
                "p.short_description, " +
                "p.ingredients, " +
                "p.weight, " +
                "p.ccal, " +
                "p.deleted, " +
                "c.id as category_id, " +
                "c.name as category_name, " +
                "i.img_url as img_url " +
                "from product p " +
                "left join category_product cp on cp.product_id = p.id " +
                "left join category c on c.id = cp.category_id " +
                "left join product_image pi on pi.product_id = p.id " +
                "left join image i on i.id = pi.image_id ",
        resultSetMapping = "itemDetailsMapping"
)


//@NamedEntityGraph(
//        name = "product-excel-entity-graph",
//        attributeNodes = @NamedAttributeNode(value = "productCategoryList", subgraph = "categories"),
//        subgraphs = @NamedSubgraph(name = "categories", attributeNodes = {@NamedAttributeNode("category")})
//)

@NamedEntityGraph(
        name = "product-excel-entity-graph",
        attributeNodes = @NamedAttributeNode(value = "productImageList", subgraph = "images"),
        subgraphs = @NamedSubgraph(name = "images", attributeNodes = {@NamedAttributeNode("image")})
)
//@NamedEntityGraph(
//        name = "product-excel-entity-graph",
//        attributeNodes = {
//                @NamedAttributeNode(value = "productCategoryList", subgraph = "categories"),
//                @NamedAttributeNode(value = "productImageList", subgraph = "images")
//        }
//        ,
//        subgraphs = {
//                @NamedSubgraph(name = "categories", attributeNodes = {@NamedAttributeNode("category")}),
//                @NamedSubgraph(name = "images", attributeNodes = {@NamedAttributeNode("image")})
//        }
//)
public class Product {

    @Id
    @GeneratedValue(generator = "product_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "product_id_seq", name = "product_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "ccal")
    private Float ccal;

    @Column(name = "img_full")
    private String imgFull;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<CategoryProduct> productCategoryList;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductImage> productImageList;

}

package kz.almat.fileparser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Almat on 31.07.2020
 */

@Entity
@Table(name = "image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(generator = "image_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "image_id_seq", name = "image_seq", allocationSize = 1)
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;
}

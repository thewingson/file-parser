package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Almat on 01.08.2020
 */

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
}

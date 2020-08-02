package kz.almat.fileparser.repo;

import kz.almat.fileparser.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Almat on 31.07.2020
 */

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
}

package lpa.com.repositories;

import lpa.com.domain.ProductCategory;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jt on 5/6/16.
 */
public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Integer> {
}

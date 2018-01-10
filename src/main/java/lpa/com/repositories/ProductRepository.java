package lpa.com.repositories;

import lpa.com.domain.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jt on 5/6/16.
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
}

package com.lpa.repositories;

import com.lpa.domain.Author;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jt on 5/6/16.
 */
public interface AuthorRepository extends CrudRepository<Author, Integer> {
}

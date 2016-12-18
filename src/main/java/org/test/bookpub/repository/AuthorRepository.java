package org.test.bookpub.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.test.bookpub.entity.Author;

/**
 * Created by Noel on 12/19/16.
 */
@RepositoryRestResource
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
}

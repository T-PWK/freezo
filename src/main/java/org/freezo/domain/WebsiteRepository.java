package org.freezo.domain;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteRepository extends PagingAndSortingRepository<Website, Long>
{
	Optional<Website> findByHostsIn(String host);
}

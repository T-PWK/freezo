package org.freezo.domain;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteRepository extends PagingAndSortingRepository<Website, Long>
{
	Optional<Website> findByHostsAndStatus(String host, Website.Status status);

	Set<Website> findByHostsIn(Set<String> hosts);
}

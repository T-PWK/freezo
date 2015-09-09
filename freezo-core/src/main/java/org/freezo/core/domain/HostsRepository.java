package org.freezo.core.domain;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface HostsRepository extends PagingAndSortingRepository<Host, String>
{
	public Set<Host> findByNameIn(Set<String> names);
}

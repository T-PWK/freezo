package org.freezo.core.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface HostsRepository extends PagingAndSortingRepository<Host, String>
{
}

package org.freezo.core.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReporitory extends PagingAndSortingRepository<Post, Long>
{
	Page<Post> findByWebsiteAndLabelsIn(Website website, String label, Pageable pageable);

	Page<Post> findByWebsite(Website website, Pageable pageable);
}

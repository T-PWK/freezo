package org.freezo.core.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedirectRepository extends PagingAndSortingRepository<Redirect, Long>
{
	@Query(value = "select r from Redirect r where r.website = ?1 and r.fromUrl = ?2 "
			+ "and (r.startAt is null or r.startAt < current_timestamp()) "
			+ "and (r.endAt is null or r.endAt > current_timestamp())")
	List<Redirect> findActive(Website website, String url);
}

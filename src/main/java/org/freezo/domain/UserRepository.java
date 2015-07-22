package org.freezo.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>
{
	/**
	 * Finds a profile by it's account's login.
	 * <p>
	 * This method is used mainly by authentication mechanism and therefore it performs join fetching with account
	 * entity.
	 *
	 * @param username login
	 * @return optional profile entity
	 */
	@Query("select u from User u join fetch u.account where u.account.username = ?1")
	Optional<User> findByUsername(String username);

	Page<User> findByAccountDisabled(boolean disabled, Pageable pageable);

	Page<User> findByAccountLocked(boolean locked, Pageable pageable);
}

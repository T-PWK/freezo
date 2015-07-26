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
	 * This method is used mainly by authentication mechanism and therefore it
	 * performs join fetching with account entity.
	 *
	 * @param username
	 *            login
	 * @return optional profile entity
	 */
	@Query("select u from User u join fetch u.account where u.account.username = ?1")
	Optional<User> findByUsername(String username);

	@Override
	@Query(value = "select u from User u inner join fetch u.account", countQuery = "select count(u) from User u")
	Page<User> findAll(Pageable pageable);

	@Query(value = "select u from User u inner join fetch u.account a where a.disabled = ?1",
			countQuery = "select count(u) from User u inner join u.account where u.account.disabled = ?1")
	Page<User> findByAccountDisabled(boolean disabled, Pageable pageable);

	@Query(value = "select u from User u inner join fetch u.account a where a.locked = ?1",
			countQuery = "select count(u) from User u inner join u.account where u.account.locked = ?1")
	Page<User> findByAccountLocked(boolean locked, Pageable pageable);

	@Query(value = "select u from User u join fetch u.account a where a.accountExpirationDate < current_timestamp()",
			countQuery = "select count(u) from User u inner join u.account a where a.accountExpirationDate < current_timestamp()")
	Page<User> findByAccountExpired(Pageable pageable);

	@Query(value = "select u from User u join fetch u.account a where a.accountExpirationDate is null or a.accountExpirationDate > current_timestamp()",
			countQuery = "select count(u) from User u inner join u.account a where a.accountExpirationDate is null or a.accountExpirationDate > current_timestamp()")
	Page<User> findByAccountNonExpired(Pageable pageable);

	@Query(value = "select u from User u join fetch u.account a where a.credentialsExpirationDate < current_timestamp()",
			countQuery = "select count(u) from User u inner join u.account a where a.credentialsExpirationDate < current_timestamp()")
	Page<User> findByCredentialsExpired(Pageable pageable);

	@Query(value = "select u from User u join fetch u.account a where a.credentialsExpirationDate is null or a.credentialsExpirationDate > current_timestamp())",
			countQuery = "select count(u) from User u inner join u.account a where a.credentialsExpirationDate is null or a.credentialsExpirationDate > current_timestamp())")
	Page<User> findByCredentialsNonExpired(Pageable pageable);
}

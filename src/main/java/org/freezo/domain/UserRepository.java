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

	@Query(value = "select u from User u join fetch u.account a where a.disabled = ?1",
			countQuery = "select count(u) from User u where u.account.disabled = ?1")
	Page<User> findByAccountDisabled(boolean disabled, Pageable pageable);

	Page<User> findByAccountLocked(boolean locked, Pageable pageable);

	@Query(value = "select u from User u join fetch u.account a where a.expired = true or a.accountExpirationDate < CURRENT_TIMESTAMP()",
			countQuery = "select count(u) from User u where u.account.expired = true or u.account.accountExpirationDate < CURRENT_TIMESTAMP()")
	Page<User> findByAccountExpired(Pageable pageable);

	@Query(value = "select u from User u join fetch u.account a where a.expired = false",
			countQuery = "select count(u) from User u where u.account.expired = false")
	Page<User> findByAccountNonExpired(Pageable pageable);

	// Hibernate: select count(user0_.id) as col_0_0_ from user user0_ left outer join account account1_ on
	// user0_.id=account1_.user_id where account1_.disabled=?
	// Hibernate: select user0_.id as id1_2_, user0_.bio as bio2_2_, user0_.email as email3_2_, user0_.first_name as
	// first_na4_2_, user0_.last_modified as last_mod5_2_, user0_.last_name as last_nam6_2_, user0_.location as
	// location7_2_, user0_.picture as picture8_2_, user0_.version as version9_2_, user0_.website as website10_2_ from
	// user user0_ left outer join account account1_ on user0_.id=account1_.user_id where account1_.disabled=? limit ?

	// Hibernate: select count(user0_.id) as col_0_0_ from user user0_ cross join account account1_ where
	// user0_.id=account1_.user_id and account1_.disabled=?
	// Hibernate: select user0_.id as id1_2_, user0_.bio as bio2_2_, user0_.email as email3_2_, user0_.first_name as
	// first_na4_2_, user0_.last_modified as last_mod5_2_, user0_.last_name as last_nam6_2_, user0_.location as
	// location7_2_, user0_.picture as picture8_2_, user0_.version as version9_2_, user0_.website as website10_2_ from
	// user user0_ cross join account account1_ where user0_.id=account1_.user_id and account1_.disabled=? limit ?

}

package org.freezo.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long>
{
	Optional<Account> findByUsername(String username);

}

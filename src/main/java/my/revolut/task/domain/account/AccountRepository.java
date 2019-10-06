package my.revolut.task.domain.account;

import java.util.UUID;

public interface AccountRepository {
	void save(Account account);

	Account getById(UUID accountId);
}

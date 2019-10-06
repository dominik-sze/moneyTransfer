package my.revolut.task.domain.account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
	void createAccount(UUID accountId, BigDecimal initialBalance);

	Optional<AccountView> getAccountById(UUID accountId);
}

package my.revolut.task.domain.account;

import com.google.inject.Singleton;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
class CheckingAccountRepository implements AccountRepository {
	private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void save(Account account) {
		accounts.put(account.getAccountId(), account);
	}

	@Override
	public Account getById(UUID accountId) {
		return accounts.get(accountId);
	}
}

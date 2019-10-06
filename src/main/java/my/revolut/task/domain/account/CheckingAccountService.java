package my.revolut.task.domain.account;

import com.google.inject.Inject;
import my.revolut.task.domain.account.CheckingAccount.AccountFactory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

class CheckingAccountService implements AccountService {
	private final AccountRepository accountRepository;
	private final AccountFactory accountFactory;

	@Inject
	CheckingAccountService(AccountRepository accountRepository, AccountFactory accountFactory) {
		this.accountRepository = accountRepository;
		this.accountFactory = accountFactory;
	}

	@Override
	public void createAccount(UUID accountId, BigDecimal initialBalance) {
		Account account = accountFactory.createAccount(accountId, initialBalance);
		accountRepository.save(account);
	}

	@Override
	public Optional<AccountView> getAccountById(UUID accountId) {
		return ofNullable(accountRepository.getById(accountId));
	}
}

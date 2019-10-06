package my.revolut.task.domain.account;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import my.revolut.task.domain.account.exception.AccountException;

import java.math.BigDecimal;
import java.util.UUID;

import static java.lang.String.format;

class CheckingAccount implements Account {
	private static final String NOT_ENOUGH_FUNDS_EXCEPTION_MESSAGE = "Not enough funds on account %s to withdraw %s. Current balance: %s";

	private final UUID accountId;
	private BigDecimal balance;

	private CheckingAccount(UUID accountId, BigDecimal initialBalance) {
		this.accountId = accountId;
		this.balance = initialBalance;
	}

	@Override
	public UUID getAccountId() {
		return accountId;
	}

	@Override
	public BigDecimal getBalance() {
		return balance;
	}

	@Override
	public void deposit(BigDecimal amount) {
		balance = balance.add(amount);
	}

	@Override
	public void withdraw(BigDecimal amount) {
		validateCurrentBalanceIsNotSmallerThan(amount);
		balance = balance.subtract(amount);
	}

	private void validateCurrentBalanceIsNotSmallerThan(BigDecimal amount) {
		if (balance.compareTo(amount) < 0) {
			throw new AccountException(format(NOT_ENOUGH_FUNDS_EXCEPTION_MESSAGE, accountId, amount, balance));
		}
	}

	@Override
	public int compareTo(Account o) {
		return accountId.compareTo(o.getAccountId());
	}

	@Singleton
	static class AccountFactory {
		private final AccountValidator accountValidator;

		@Inject
		AccountFactory(AccountValidator accountValidator) {
			this.accountValidator = accountValidator;
		}

		Account createAccount(UUID accountId, BigDecimal initialBalance) {
			accountValidator.validate(initialBalance);
			return new CheckingAccount(accountId, initialBalance);
		}
	}
}

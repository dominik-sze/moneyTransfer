package my.revolut.task.domain.account;

import my.revolut.task.domain.account.exception.AccountException;

import java.math.BigDecimal;

class AccountValidator {
	private static final String INITIAL_BALANCE_EXCEPTION_MESSAGE = "Initial amount has to be positive number";

	void validate(BigDecimal balance) {
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new AccountException(INITIAL_BALANCE_EXCEPTION_MESSAGE);
		}
	}
}

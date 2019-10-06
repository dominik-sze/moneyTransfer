package my.revolut.task.domain.transfer;

import my.revolut.task.domain.account.Account;
import my.revolut.task.domain.transfer.exception.TransferException;

import java.math.BigDecimal;

class TransferValidator {
	private static final String FROM_ACCOUNT_EXCEPTION_MESSAGE = "\"From\" account does not exist";
	private static final String TO_ACCOUNT_EXCEPTION_MESSAGE = "\"To\" account does not exist";
	private static final String ACCOUNT_ID_EXCEPTION_MESSAGE = "\"From\" and \"to\" account can't be the same";
	private static final String AMOUNT_EXCEPTION_MESSAGE = "Amount to transfer has to be larger than zero";

	void validate(Account fromAccount, Account toAccount, BigDecimal amount) {
		if (fromAccount == null) {
			throw new TransferException(FROM_ACCOUNT_EXCEPTION_MESSAGE);
		}
		if (toAccount == null) {
			throw new TransferException(TO_ACCOUNT_EXCEPTION_MESSAGE);
		}
		if (fromAccount.compareTo(toAccount) == 0) {
			throw new TransferException(ACCOUNT_ID_EXCEPTION_MESSAGE);
		}
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new TransferException(AMOUNT_EXCEPTION_MESSAGE);
		}
	}
}

package my.revolut.task.domain.transfer;

import com.google.inject.Inject;
import my.revolut.task.domain.account.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.sort;

class MoneyTransfer implements Transfer {
	private final UUID transferId;
	private final Account fromAccount;
	private final Account toAccount;
	private final BigDecimal amount;

	private MoneyTransfer(UUID transferId, Account fromAccount, Account toAccount, BigDecimal amount) {
		this.transferId = transferId;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
	}

	@Override
	public void run() {
		List<Account> list = asList(fromAccount, toAccount);
		sort(list);
		synchronized (list.get(0)) {
			synchronized (list.get(1)) {
				fromAccount.withdraw(amount);
				toAccount.deposit(amount);
			}
		}
	}

	@Override
	public UUID getTransferId() {
		return transferId;
	}

	@Override
	public UUID getFromAccountId() {
		return fromAccount.getAccountId();
	}

	@Override
	public UUID getToAccountId() {
		return toAccount.getAccountId();
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	static class TransferFactory {
		private final TransferValidator transferValidator;

		@Inject
		TransferFactory(TransferValidator transferValidator) {
			this.transferValidator = transferValidator;
		}

		Transfer createTransfer(UUID transferId, Account fromAccount, Account toAccount, BigDecimal amount) {
			transferValidator.validate(fromAccount, toAccount, amount);
			return new MoneyTransfer(transferId, fromAccount, toAccount, amount);
		}
	}
}

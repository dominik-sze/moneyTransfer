package my.revolut.task.domain.account;

import java.math.BigDecimal;

public interface Account extends AccountView, Comparable<Account> {
	void deposit(BigDecimal amount);

	void withdraw(BigDecimal amount);
}

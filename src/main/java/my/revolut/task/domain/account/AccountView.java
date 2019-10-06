package my.revolut.task.domain.account;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountView {
	UUID getAccountId();

	BigDecimal getBalance();
}

package my.revolut.task.domain.transfer;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferView {
	UUID getTransferId();

	UUID getFromAccountId();

	UUID getToAccountId();

	BigDecimal getAmount();
}

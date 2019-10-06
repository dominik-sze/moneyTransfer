package my.revolut.task.domain.transfer;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface TransferService {
	void transfer(UUID transactionId, UUID fromAccountId, UUID toAccountId, BigDecimal amount);

	Optional<TransferView> getById(UUID transferId);
}

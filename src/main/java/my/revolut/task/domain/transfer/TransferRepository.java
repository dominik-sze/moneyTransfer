package my.revolut.task.domain.transfer;

import java.util.UUID;

public interface TransferRepository {
	void save(Transfer transfer);

	Transfer getById(UUID transferId);
}

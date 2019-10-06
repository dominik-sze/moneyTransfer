package my.revolut.task.domain.transfer;

import com.google.inject.Singleton;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
class MoneyTransferRepository implements TransferRepository {
	private final Map<UUID, Transfer> transfers = new ConcurrentHashMap<>();

	@Override
	public void save(Transfer transfer) {
		transfers.put(transfer.getTransferId(), transfer);
	}

	@Override
	public Transfer getById(UUID transferId) {
		return transfers.get(transferId);
	}
}

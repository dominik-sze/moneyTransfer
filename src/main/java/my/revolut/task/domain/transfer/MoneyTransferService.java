package my.revolut.task.domain.transfer;

import com.google.inject.Inject;
import my.revolut.task.domain.account.Account;
import my.revolut.task.domain.account.AccountRepository;
import my.revolut.task.domain.transfer.MoneyTransfer.TransferFactory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

class MoneyTransferService implements TransferService {
	private final AccountRepository accountRepository;
	private final TransferFactory transferFactory;
	private final TransferRepository transferRepository;

	@Inject
	MoneyTransferService(AccountRepository accountRepository, TransferFactory transferFactory, TransferRepository transferRepository) {
		this.accountRepository = accountRepository;
		this.transferFactory = transferFactory;
		this.transferRepository = transferRepository;
	}

	@Override
	public void transfer(UUID transferId, UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
		Account fromAccount = accountRepository.getById(fromAccountId);
		Account toAccount = accountRepository.getById(toAccountId);
		Transfer transfer = transferFactory.createTransfer(transferId, fromAccount, toAccount, amount);
		transferRepository.save(transfer);
		transfer.run();
	}

	@Override
	public Optional<TransferView> getById(UUID transferId) {
		return ofNullable(transferRepository.getById(transferId));
	}
}

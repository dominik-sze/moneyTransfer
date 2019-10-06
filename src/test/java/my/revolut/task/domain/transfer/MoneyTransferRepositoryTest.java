package my.revolut.task.domain.transfer;

import my.revolut.task.domain.account.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static my.revolut.task.domain.transfer.MoneyTransfer.TransferFactory;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransferRepositoryTest {
	private static final UUID SOME_TRANSFER_ID = UUID.randomUUID();

	@Mock private Account fromAccount;
	@Mock private Account toAccount;
	@Mock private TransferValidator transferValidator;

	private final TransferRepository transferRepository = new MoneyTransferRepository();

	@Test
	public void shouldStoreAndRetrieveTransfer() {
		Transfer transfer = new TransferFactory(transferValidator).createTransfer(SOME_TRANSFER_ID, fromAccount, toAccount, new BigDecimal("100"));
		transferRepository.save(transfer);

		Transfer result = transferRepository.getById(SOME_TRANSFER_ID);

		assertThat(result).isSameAs(transfer);
	}
}

package my.revolut.task.domain.transfer;

import my.revolut.task.domain.account.Account;
import my.revolut.task.domain.account.AccountRepository;
import my.revolut.task.domain.transfer.MoneyTransfer.TransferFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransferServiceTest {
	private static final UUID FROM_ACCOUNT_ID = UUID.randomUUID();
	private static final UUID TO_ACCOUNT_ID = UUID.randomUUID();
	private static final UUID TRANSFER_ID = UUID.randomUUID();
	private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("10");

	@Mock private Account fromAccount;
	@Mock private Account toAccount;
	@Mock private Transfer transfer;

	@Mock private AccountRepository accountRepository;
	@Mock private TransferFactory transferFactory;
	@Mock private TransferRepository transferRepository;
	private TransferService transferService;

	@Before
	public void setUp() {
		transferService = new MoneyTransferService(accountRepository, transferFactory, transferRepository);
	}

	@Test
	public void shouldExecuteTransfer() {
		given(accountRepository.getById(FROM_ACCOUNT_ID)).willReturn(fromAccount);
		given(accountRepository.getById(TO_ACCOUNT_ID)).willReturn(toAccount);
		given(transferFactory.createTransfer(TRANSFER_ID, fromAccount, toAccount, TRANSFER_AMOUNT)).willReturn(transfer);

		transferService.transfer(TRANSFER_ID, FROM_ACCOUNT_ID, TO_ACCOUNT_ID, TRANSFER_AMOUNT);

		then(transferRepository).should().save(transfer);
		then(transfer).should().run();
	}

	@Test
	public void shouldGetTransfer() {
		given(transferRepository.getById(TRANSFER_ID)).willReturn(transfer);

		Optional<TransferView> optio = transferService.getById(TRANSFER_ID);

		assertThat(optio).contains(transfer);
	}
}
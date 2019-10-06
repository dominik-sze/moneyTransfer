package my.revolut.task.domain.transfer;

import my.revolut.task.domain.account.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static my.revolut.task.domain.transfer.MoneyTransfer.TransferFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransferTest {
	private static final UUID SOME_FROM_ACCOUNT_ID = UUID.randomUUID();
	private static final UUID SOME_TO_ACCOUNT_ID = UUID.randomUUID();
	private static final UUID SOME_TRANSFER_ID = UUID.randomUUID();
	private static final BigDecimal SOME_AMOUNT = new BigDecimal("10");

	@Mock private Account fromAccount;
	@Mock private Account toAccount;

	@Mock private TransferValidator transferValidator;
	private TransferFactory transferFactory;

	@Before
	public void setUp() {
		transferFactory = new TransferFactory(transferValidator);
	}

	@Test
	public void shouldCreateMoneyTransfer() {
		given(fromAccount.getAccountId()).willReturn(SOME_FROM_ACCOUNT_ID);
		given(toAccount.getAccountId()).willReturn(SOME_TO_ACCOUNT_ID);

		Transfer transfer = transferFactory.createTransfer(SOME_TRANSFER_ID, fromAccount, toAccount, SOME_AMOUNT);

		assertThat(transfer.getTransferId()).isEqualByComparingTo(SOME_TRANSFER_ID);
		assertThat(transfer.getFromAccountId()).isEqualByComparingTo(SOME_FROM_ACCOUNT_ID);
		assertThat(transfer.getToAccountId()).isEqualByComparingTo(SOME_TO_ACCOUNT_ID);
		assertThat(transfer.getAmount()).isEqualByComparingTo(SOME_AMOUNT);
	}

	@Test
	public void shouldRunWithdrawalAndDepositOnAppropriateAccountsWhenTransferIsRun() {
		Transfer transfer = transferFactory.createTransfer(SOME_TRANSFER_ID, fromAccount, toAccount, SOME_AMOUNT);

		transfer.run();

		then(fromAccount).should().withdraw(SOME_AMOUNT);
		then(toAccount).should().deposit(SOME_AMOUNT);
	}
}

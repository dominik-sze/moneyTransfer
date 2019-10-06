package my.revolut.task.domain.transfer;

import my.revolut.task.domain.account.Account;
import my.revolut.task.domain.transfer.exception.TransferException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TransferValidatorTest {
	private static final BigDecimal SOME_AMOUNT = new BigDecimal(1);
	private static final BigDecimal NEGATIVE_AMOUNT = new BigDecimal(-1);
	private static final String FROM_ACCOUNT_EXCEPTION_MESSAGE = "\"From\" account does not exist";
	private static final String TO_ACCOUNT_EXCEPTION_MESSAGE = "\"To\" account does not exist";
	private static final String AMOUNT_EXCEPTION_MESSAGE = "Amount to transfer has to be larger than zero";
	private static final String ACCOUNT_ID_EXCEPTION_MESSAGE = "\"From\" and \"to\" account can't be the same";

	@Mock private Account someAccount;
	@Mock private Account someOtherAccount;

	private final TransferValidator transferValidator = new TransferValidator();

	@Before
	public void setUp() {
		given(someAccount.compareTo(someOtherAccount)).willReturn(-1);
	}

	@Test
	public void shouldThrowExceptionIfFromAccountIsNull() {
		assertThatThrownBy(() -> transferValidator.validate(null, someOtherAccount, SOME_AMOUNT))
				.isInstanceOf(TransferException.class)
				.hasMessage(FROM_ACCOUNT_EXCEPTION_MESSAGE);
	}

	@Test
	public void shouldThrowExceptionIfToAccountIsNull() {
		assertThatThrownBy(() -> transferValidator.validate(someAccount, null, SOME_AMOUNT))
				.isInstanceOf(TransferException.class)
				.hasMessage(TO_ACCOUNT_EXCEPTION_MESSAGE);
	}

	@Test
	public void shouldThrowExceptionIfFromAccountAndToAccountAreTheSame() {
		given(someAccount.compareTo(someOtherAccount)).willReturn(0);

		assertThatThrownBy(() -> transferValidator.validate(someAccount, someOtherAccount, SOME_AMOUNT))
				.isInstanceOf(TransferException.class)
				.hasMessage(ACCOUNT_ID_EXCEPTION_MESSAGE);
	}

	@Test
	public void shouldThrowExceptionIfAmountToTransferIsNegative() {
		assertThatThrownBy(() -> transferValidator.validate(someAccount, someOtherAccount, NEGATIVE_AMOUNT))
				.isInstanceOf(TransferException.class)
				.hasMessage(AMOUNT_EXCEPTION_MESSAGE);
	}

	@Test
	public void shouldThrowExceptionIfAmountToTransferIsZero() {
		assertThatThrownBy(() -> transferValidator.validate(someAccount, someOtherAccount, ZERO))
				.isInstanceOf(TransferException.class)
				.hasMessage(AMOUNT_EXCEPTION_MESSAGE);
	}

	@Test
	public void shouldDoNothingIfAccountIdsAreNotTheSameAndAmountToTransferIsBiggerThanZero() {
		transferValidator.validate(someAccount, someOtherAccount, SOME_AMOUNT);
	}
}

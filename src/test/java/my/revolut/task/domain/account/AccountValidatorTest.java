package my.revolut.task.domain.account;

import my.revolut.task.domain.account.exception.AccountException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountValidatorTest {
	private static final String INITIAL_AMOUNT_EXCEPTION_MESSAGE = "Initial amount has to be positive number";

	private final AccountValidator accountValidator = new AccountValidator();

	@Test
	public void shouldThrowExceptionIfInitialBalanceIsLessThanZero() {
		assertThatThrownBy(() -> accountValidator.validate(new BigDecimal("-1")))
				.isInstanceOf(AccountException.class)
				.hasMessage(INITIAL_AMOUNT_EXCEPTION_MESSAGE);
	}

	@Test
	public void shouldNotThrowExceptionIfInitialBalanceIsZero() {
		accountValidator.validate(new BigDecimal("0"));
	}
}

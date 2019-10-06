package my.revolut.task.domain.account;

import my.revolut.task.domain.account.CheckingAccount.AccountFactory;
import my.revolut.task.domain.account.exception.AccountException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class CheckingAccountTest {
	private static final String NOT_ENOUGH_FUNDS_EXCEPTION_MESSAGE = "Not enough funds on account %s to withdraw %s. Current balance: %s";
	private static final BigDecimal SOME_INITIAL_BALANCE = new BigDecimal("123.45");
	private static final BigDecimal SOME_OTHER_INITIAL_BALANCE = new BigDecimal("21.09");
	private static final BigDecimal SOME_AMOUNT = new BigDecimal("67.89");
	private static final BigDecimal EXPECTED_AMOUNT_AFTER_DEPOSIT = SOME_INITIAL_BALANCE.add(SOME_AMOUNT);
	private static final BigDecimal EXPECTED_AMOUNT_AFTER_WITHDRAWAL = SOME_INITIAL_BALANCE.subtract(SOME_AMOUNT);
	private static final UUID SOME_ACCOUNT_ID = UUID.randomUUID();

	@Mock private AccountValidator accountValidator;
	private AccountFactory accountFactory;

	@Before
	public void setUp() {
		accountFactory = new AccountFactory(accountValidator);
	}

	@Test
	public void shouldDepositMoneyOnAccount() {
		Account account = accountFactory.createAccount(SOME_ACCOUNT_ID, SOME_INITIAL_BALANCE);

		account.deposit(SOME_AMOUNT);

		assertThat(account.getBalance()).isEqualTo(EXPECTED_AMOUNT_AFTER_DEPOSIT);
	}

	@Test
	public void shouldWithdrawMoneyFromAccount() {
		Account account = accountFactory.createAccount(SOME_ACCOUNT_ID, SOME_INITIAL_BALANCE);

		account.withdraw(SOME_AMOUNT);

		assertThat(account.getBalance()).isEqualTo(EXPECTED_AMOUNT_AFTER_WITHDRAWAL);
	}

	@Test
	public void shouldThrowExceptionWhenWithdrawingMoreThanCurrentBalance() {
		Account account = accountFactory.createAccount(SOME_ACCOUNT_ID, SOME_INITIAL_BALANCE);
		BigDecimal doubleInitialBalance = SOME_INITIAL_BALANCE.add(SOME_INITIAL_BALANCE);

		assertThatThrownBy(() -> account.withdraw(doubleInitialBalance))
				.isInstanceOf(AccountException.class)
				.hasMessage(format(NOT_ENOUGH_FUNDS_EXCEPTION_MESSAGE, SOME_ACCOUNT_ID, doubleInitialBalance, SOME_INITIAL_BALANCE));
	}

	@Test
	public void shouldRecognizeTwoAccountsAreTheSameIfTheyHaveTheSameId() {
		Account account1 = new AccountFactory(accountValidator).createAccount(SOME_ACCOUNT_ID, SOME_INITIAL_BALANCE);
		Account account2 = new AccountFactory(accountValidator).createAccount(SOME_ACCOUNT_ID, SOME_OTHER_INITIAL_BALANCE);

		assertThat(account1).isEqualByComparingTo(account2);
	}
}

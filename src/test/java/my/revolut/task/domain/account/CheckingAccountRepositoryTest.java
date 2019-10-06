package my.revolut.task.domain.account;

import my.revolut.task.domain.account.CheckingAccount.AccountFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckingAccountRepositoryTest {
	private static final BigDecimal SOME_INITIAL_BALANCE = new BigDecimal("123.45");
	private static final UUID SOME_ACCOUNT_ID = UUID.randomUUID();

	private AccountValidator accountValidator = new AccountValidator();
	private AccountFactory accountFactory = new AccountFactory(accountValidator);

	private AccountRepository accountRepository = new CheckingAccountRepository();

	@Test
	public void shouldStoreAndRetrieveAccount() {
		Account account = accountFactory.createAccount(SOME_ACCOUNT_ID, SOME_INITIAL_BALANCE);
		accountRepository.save(account);

		Account result = accountRepository.getById(SOME_ACCOUNT_ID);

		assertThat(result).isEqualByComparingTo(account);
	}
}

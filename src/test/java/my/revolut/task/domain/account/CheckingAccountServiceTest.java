package my.revolut.task.domain.account;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static my.revolut.task.domain.account.CheckingAccount.AccountFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class CheckingAccountServiceTest {
	private static final UUID SOME_ACCOUNT_ID = UUID.randomUUID();
	private static final BigDecimal SOME_INITIAL_BALANCE = new BigDecimal("100");

	@Mock private Account account;

	@Mock private AccountRepository accountRepository;
	@Mock private AccountFactory accountFactory;
	private AccountService accountService;

	@Before
	public void setUp() {
		accountService = new CheckingAccountService(accountRepository, accountFactory);
	}

	@Test
	public void shouldCreateAccountByDelegatingToFactoryAndRepository() {
		given(accountFactory.createAccount(SOME_ACCOUNT_ID, SOME_INITIAL_BALANCE)).willReturn(account);

		accountService.createAccount(SOME_ACCOUNT_ID, SOME_INITIAL_BALANCE);

		then(accountRepository).should().save(account);
	}

	@Test
	public void shouldRetrieveAccount() {
		given(accountRepository.getById(SOME_ACCOUNT_ID)).willReturn(account);

		Optional<AccountView> result = accountService.getAccountById(SOME_ACCOUNT_ID);

		assertThat(result).contains(account);
	}
}

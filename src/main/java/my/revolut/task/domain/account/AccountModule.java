package my.revolut.task.domain.account;

import com.google.inject.AbstractModule;

public class AccountModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(AccountRepository.class).to(CheckingAccountRepository.class);
		bind(AccountService.class).to(CheckingAccountService.class);
	}
}

package my.revolut.task.domain.transfer;

import com.google.inject.AbstractModule;
import my.revolut.task.domain.account.AccountModule;

public class TransferModule extends AbstractModule {
	@Override
	protected void configure() {
		install(new AccountModule());
		bind(TransferRepository.class).to(MoneyTransferRepository.class);
		bind(TransferService.class).to(MoneyTransferService.class);
	}
}

package my.revolut.task.api.http;

import com.google.inject.AbstractModule;
import my.revolut.task.api.MoneyTransferAPI;
import my.revolut.task.domain.account.AccountModule;
import my.revolut.task.domain.transfer.TransferModule;

public class APIModule extends AbstractModule {
	@Override
	protected void configure() {
		install(new TransferModule());
		install(new AccountModule());
		bind(MoneyTransferAPI.class).to(HttpMoneyTransferAPI.class);
	}
}

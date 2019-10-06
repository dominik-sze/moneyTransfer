package my.revolut.task;

import my.revolut.task.api.MoneyTransferAPI;
import my.revolut.task.api.http.APIModule;

import static com.google.inject.Guice.createInjector;

public class MoneyTransferApplication {
	public static void main(String[] args) {
		MoneyTransferAPI instance = createInjector(new APIModule()).getInstance(MoneyTransferAPI.class);
		instance.setUp();
	}
}

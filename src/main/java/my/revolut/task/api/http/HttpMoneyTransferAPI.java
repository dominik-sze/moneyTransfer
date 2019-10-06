package my.revolut.task.api.http;

import com.google.inject.Inject;
import my.revolut.task.api.MoneyTransferAPI;
import my.revolut.task.domain.account.AccountService;
import my.revolut.task.domain.transfer.TransferService;

class HttpMoneyTransferAPI implements MoneyTransferAPI {
	private final RootController rootController;
	private final TransferController transferController;
	private final AccountController accountController;
	private final ExceptionHandler exceptionHandler;

	@Inject
	HttpMoneyTransferAPI(TransferService transactionService, AccountService accountService) {
		JsonTransformer jsonTransformer = new JsonTransformer();
		this.rootController = new RootController(jsonTransformer);
		this.transferController = new TransferController(transactionService, jsonTransformer);
		this.accountController = new AccountController(accountService, jsonTransformer);
		this.exceptionHandler = new ExceptionHandler();
	}

	@Override
	public void setUp() {
		rootController.configure();
		transferController.configure();
		accountController.configure();
		exceptionHandler.exceptions();
	}
}

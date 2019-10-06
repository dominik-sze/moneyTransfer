package my.revolut.task.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import my.revolut.task.domain.account.AccountService;
import my.revolut.task.domain.account.AccountView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static java.lang.String.format;
import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.get;
import static spark.Spark.post;

class AccountController {
	private final AccountService accountService;
	private final JsonTransformer jsonTransformer;

	@Inject
	AccountController(AccountService accountService, JsonTransformer jsonTransformer) {
		this.accountService = accountService;
		this.jsonTransformer = jsonTransformer;
	}

	void configure() {
		registerAccountCreateRequestHandler();
		registerAccountResourceRetrievalHandler();
	}

	private void registerAccountCreateRequestHandler() {
		post("/account", APPLICATION_JSON.asString(), (request, response) -> {
			AccountCreateRequest accountCreateRequest = getAccountCreateRequestFrom(request);
			UUID accountId = UUID.randomUUID();
			accountService.createAccount(accountId, new BigDecimal(accountCreateRequest.getAmount()));
			return response(request, response, accountId);
		}, jsonTransformer);
	}

	private Link response(Request request, Response response, UUID accountId) {
		response.status(CREATED_201);
		response.type(APPLICATION_JSON.asString());
		return new Link(request.url() + "/" + accountId, "account");
	}

	private void registerAccountResourceRetrievalHandler() {
		get("/account/:accountId", (request, response) -> {
			UUID accountId = UUID.fromString(request.params(":accountId"));
			Optional<AccountView> accountView = accountService.getAccountById(accountId);
			return accountView
					.map(account -> response(request, response, account))
					.orElseThrow(resourceNotFoundException(accountId));
		}, jsonTransformer);
	}

	private AccountResource response(Request request, Response response, AccountView account) {
		response.status(OK_200);
		response.type(APPLICATION_JSON.toString());
		return new AccountResource(
				account.getAccountId(),
				account.getBalance(),
				new Link(request.url(), "self"));
	}

	private Supplier<ResourceNotFoundException> resourceNotFoundException(UUID accountId) {
		return () -> new ResourceNotFoundException(format("Account with id %s not found", accountId));
	}

	private AccountCreateRequest getAccountCreateRequestFrom(Request request) throws IOException {
		return new ObjectMapper().readValue(request.body(), AccountCreateRequest.class);
	}
}

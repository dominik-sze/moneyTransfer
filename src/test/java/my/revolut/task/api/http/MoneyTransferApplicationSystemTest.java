package my.revolut.task.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.revolut.task.api.MoneyTransferAPI;
import okhttp3.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.google.inject.Guice.createInjector;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

public class MoneyTransferApplicationSystemTest {
	private static final String ENTRY_POINT = "http://localhost:4567/";
	private static final Predicate<Link> ACCOUNT_LINK = link -> link.getRef().equals("account");
	private static final Predicate<Link> TRANSFER_LINK = link -> link.getRef().equals("transfer");

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final String ONE_HUNDRED = "100";
	private static final String FORTY = "40";
	private static final String TWENTY = "20";
	private static final String EIGHTY = "80";
	private static final String SIXTY = "60";

	private List<Link> entryPointRequestResult;
	private String accountResourceLink;
	private String transferResourceLink;
	private AccountResource firstAccountResource;
	private AccountResource secondAccountResource;

	@Before
	public void setUp() {
		MoneyTransferAPI instance = createInjector(new APIModule()).getInstance(MoneyTransferAPI.class);
		instance.setUp();
		awaitInitialization();
	}

	@After
	public void tearDown() {
		stop();
	}

	@Test
	public void shouldExecuteTransferBetweenTwoAccounts() throws IOException {
		givenEntryPointIsQueried();
		andAccountAndTransferLinksDiscovered();
		andFirstAccountIsCreated();
		andSecondAccountIsCreated();

		whenTransferFromFirstToSecondAccountIsExecutedForAmount(TWENTY);

		thenFirstAccountBalanceIsDecreasedByTwenty();
		andSecondAccountBalanceIsIncreasedByTwenty();
	}

	private void givenEntryPointIsQueried() throws IOException {
		Response response = executeGetRequest(ENTRY_POINT);
		String body = response.body().string();
		entryPointRequestResult = Arrays.asList(MAPPER.readValue(body, Link[].class));
	}

	private void andAccountAndTransferLinksDiscovered() {
		accountResourceLink = getLinkToResource(ACCOUNT_LINK);
		transferResourceLink = getLinkToResource(TRANSFER_LINK);
	}

	private String getLinkToResource(Predicate<Link> resourcePredicate) {
		return entryPointRequestResult.stream()
				.filter(resourcePredicate)
				.findFirst()
				.get()
				.getHref();
	}

	private void andFirstAccountIsCreated() throws IOException {
		firstAccountResource = createAccountWithInitialBalance(ONE_HUNDRED);
		assertThat(firstAccountResource.getAmount()).isEqualByComparingTo(new BigDecimal(ONE_HUNDRED));
	}

	private void andSecondAccountIsCreated() throws IOException {
		secondAccountResource = createAccountWithInitialBalance(FORTY);
		assertThat(secondAccountResource.getAmount()).isEqualByComparingTo(new BigDecimal(FORTY));
	}

	private AccountResource createAccountWithInitialBalance(String initialBalance) throws IOException {
		String accountCreateRequestBody = MAPPER.writeValueAsString(new AccountCreateRequest(initialBalance));
		Response postResponse = executePostRequest(accountResourceLink, accountCreateRequestBody);
		assertThat(postResponse.code()).isEqualTo(CREATED_201);

		Link link = MAPPER.readValue(postResponse.body().string(), Link.class);
		Response getResponse = executeGetRequest(link.getHref());
		assertThat(getResponse.code()).isEqualTo(OK_200);

		return MAPPER.readValue(getResponse.body().string(), AccountResource.class);
	}

	private void whenTransferFromFirstToSecondAccountIsExecutedForAmount(String amount) throws IOException {
		String transferCreate = MAPPER.writeValueAsString(new TransferCreateRequest(firstAccountResource.getAccountId(), secondAccountResource.getAccountId(), amount));
		Response postResponse = executePostRequest(transferResourceLink, transferCreate);

		Link link = MAPPER.readValue(postResponse.body().string(), Link.class);
		Response getResponse = executeGetRequest(link.getHref());

		TransferResource transferResource = MAPPER.readValue(getResponse.body().string(), TransferResource.class);

		assertThat(postResponse.code()).isEqualTo(CREATED_201);
		assertThat(getResponse.code()).isEqualTo(OK_200);
		assertThat(transferResource.getFromAccountId()).isEqualTo(firstAccountResource.getAccountId());
		assertThat(transferResource.getToAccountId()).isEqualTo(secondAccountResource.getAccountId());
		assertThat(transferResource.getAmount()).isEqualByComparingTo(new BigDecimal("20"));
	}

	private void thenFirstAccountBalanceIsDecreasedByTwenty() throws IOException {
		accountBalanceEqualsTo(firstAccountResource, EIGHTY);
	}

	private void andSecondAccountBalanceIsIncreasedByTwenty() throws IOException {
		accountBalanceEqualsTo(secondAccountResource, SIXTY);
	}

	private void accountBalanceEqualsTo(AccountResource someAccountResource, String amount) throws IOException {
		Response getResponse = executeGetRequest(someAccountResource.getLink().getHref());
		AccountResource accountResource = MAPPER.readValue(getResponse.body().string(), AccountResource.class);

		assertThat(getResponse.code()).isEqualTo(OK_200);
		assertThat(accountResource.getAmount()).isEqualByComparingTo(new BigDecimal(amount));
	}

	private Response executeGetRequest(String link) throws IOException {
		Request request = new Request.Builder()
				.url(link)
				.build();
		return CLIENT.newCall(request).execute();
	}

	private Response executePostRequest(String url, String jsonBody) throws IOException {
		RequestBody body = RequestBody.create(
				MediaType.parse(APPLICATION_JSON.toString()), jsonBody);
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		return CLIENT.newCall(request).execute();
	}
}

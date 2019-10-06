package my.revolut.task.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import my.revolut.task.domain.transfer.TransferService;
import my.revolut.task.domain.transfer.TransferView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static java.lang.String.format;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.get;
import static spark.Spark.post;

class TransferController {
	private final TransferService transferService;
	private final JsonTransformer jsonTransformer;

	@Inject
	TransferController(TransferService transferService, JsonTransformer jsonTransformer) {
		this.transferService = transferService;
		this.jsonTransformer = jsonTransformer;
	}

	void configure() {
		registerTransferCreateRequestHandler();
		registerTransferResourceRetrievalHandler();
	}

	private void registerTransferCreateRequestHandler() {
		post("/transfer", APPLICATION_JSON.asString(), (request, response) -> {
			UUID transferId = UUID.randomUUID();
			transferMoney(transferId, request);
			return response(request, response, transferId);
		}, jsonTransformer);
	}

	private void transferMoney(UUID transferId, Request request) throws IOException {
		TransferCreateRequest transferCreateRequest = getTransferCreateRequestFrom(request);
		UUID fromAccountId = transferCreateRequest.getFromAccountId();
		UUID toAccountId = transferCreateRequest.getToAccountId();
		BigDecimal amount = new BigDecimal(transferCreateRequest.getAmount());
		transferService.transfer(transferId, fromAccountId, toAccountId, amount);
	}

	private Link response(Request request, Response response, UUID transferId) {
		response.status(CREATED_201);
		response.type(APPLICATION_JSON.asString());
		return new Link(request.url() + "/" + transferId, "transfer");
	}

	private void registerTransferResourceRetrievalHandler() {
		get("/transfer/:transferId", (request, response) -> {
			UUID transferId = UUID.fromString(request.params(":transferId"));
			Optional<TransferView> transferView = transferService.getById(transferId);
			return transferView.
					map(transfer -> response(request, response, transfer))
					.orElseThrow(transferNotFoundException(transferId));
		}, jsonTransformer);
	}

	private Supplier<ResourceNotFoundException> transferNotFoundException(UUID transferId) {
		return () -> new ResourceNotFoundException(format("Transfer with id %s does not exist", transferId));
	}

	private TransferResource response(Request request, Response response, TransferView transferView) {
		response.status(OK_200);
		response.type(APPLICATION_JSON.toString());
		return new TransferResource(
				transferView.getTransferId(),
				transferView.getFromAccountId(),
				transferView.getToAccountId(),
				transferView.getAmount(),
				new Link(request.url(), "self"));
	}

	private TransferCreateRequest getTransferCreateRequestFrom(Request request) throws IOException {
		return new ObjectMapper().readValue(request.body(), TransferCreateRequest.class);
	}
}

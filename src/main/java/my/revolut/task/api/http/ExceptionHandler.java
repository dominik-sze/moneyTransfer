package my.revolut.task.api.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import my.revolut.task.domain.account.exception.AccountException;
import my.revolut.task.domain.transfer.exception.TransferException;

import static org.eclipse.jetty.http.HttpStatus.*;
import static spark.Spark.exception;

class ExceptionHandler {
	void exceptions() {
		exception(ResourceNotFoundException.class, (exception, request, response) -> {
			response.status(NOT_FOUND_404);
			response.body(exception.getMessage());
		});
		exception(AccountException.class, (exception, request, response) -> {
			response.status(BAD_REQUEST_400);
			response.body(exception.toString());
		});
		exception(TransferException.class, (exception, request, response) -> {
			response.status(BAD_REQUEST_400);
			response.body(exception.toString());
		});
		exception(IllegalArgumentException.class, (exception, request, response) -> {
			response.status(BAD_REQUEST_400);
			response.body(exception.toString());
		});
		exception(JsonMappingException.class, (exception, request, response) -> {
			response.status(BAD_REQUEST_400);
			response.body(exception.toString());
		});
		exception(JsonParseException.class, (exception, request, response) -> {
			response.status(BAD_REQUEST_400);
			response.body(exception.toString());
		});
		exception(Exception.class, (exception, request, response) -> {
			response.status(INTERNAL_SERVER_ERROR_500);
			response.body(exception.toString());
		});
	}
}

package my.revolut.task.api.http;

import com.google.inject.Inject;

import java.util.Arrays;

import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.get;

class RootController {
	private final JsonTransformer jsonTransformer;

	@Inject
	RootController(JsonTransformer jsonTransformer) {
		this.jsonTransformer = jsonTransformer;
	}

	void configure() {
		get("/", (request, response) -> {
			response.status(OK_200);
			response.type(APPLICATION_JSON.toString());
			return Arrays.asList(
					new Link(request.url(), "self"),
					new Link(request.url() + "transfer", "transfer"),
					new Link(request.url() + "account", "account"));
		}, jsonTransformer);
	}
}

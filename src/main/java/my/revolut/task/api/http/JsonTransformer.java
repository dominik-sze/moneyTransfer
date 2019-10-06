package my.revolut.task.api.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import spark.ResponseTransformer;

@Singleton
class JsonTransformer implements ResponseTransformer {
	@Override
	public String render(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}
}

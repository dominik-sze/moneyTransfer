package my.revolut.task.api.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountCreateRequest {
	@JsonProperty(required = true)
	private String amount;
}

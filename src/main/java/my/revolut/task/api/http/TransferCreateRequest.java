package my.revolut.task.api.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TransferCreateRequest {
	@JsonProperty(required = true)
	private UUID fromAccountId;
	@JsonProperty(required = true)
	private UUID toAccountId;
	@JsonProperty(required = true)
	private String amount;
}

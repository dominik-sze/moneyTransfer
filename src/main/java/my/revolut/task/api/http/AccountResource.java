package my.revolut.task.api.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountResource {
	private UUID accountId;
	private BigDecimal amount;
	private Link link;
}

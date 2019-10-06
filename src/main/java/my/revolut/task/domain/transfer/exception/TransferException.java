package my.revolut.task.domain.transfer.exception;

public class TransferException extends RuntimeException {
	public TransferException(String message) {
		super(message);
	}
}

package my.revolut.task.api.http;

class ResourceNotFoundException extends RuntimeException {
	ResourceNotFoundException(String message) {
		super(message);
	}
}

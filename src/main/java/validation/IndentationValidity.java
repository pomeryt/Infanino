package validation;

/**
 * Check if the indentation is valid or not.
 */
public final class IndentationValidity implements Validation {

	public IndentationValidity(final String code) {
		this.code = code;
	}
	
	@Override
	public boolean valid() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public int line() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public String reason() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}
	
	private final String code;
}

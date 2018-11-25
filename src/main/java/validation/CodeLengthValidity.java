package validation;

/**
 * It checks the number of code lines<br>
 * The code length more than line limit would be invalid.
 */

public class CodeLengthValidity implements Validation {

	public CodeLengthValidity(final String code,final int lineLimit) {
		this.code = code;
		this.lineLimit = lineLimit;
	}

	@Override
	public boolean valid() {
		final String[] lines = this.code.split("\n");
		if (lines.length > this.lineLimit) {
			this.validity = false;
			return false;
		}
		this.validity = true;
		return true;
	}

	@Override
	public int line() {
		if (this.validity) {
			throw new IllegalStateException("The code should be invalid to use this method");
		} else {
			return this.lineLimit + 1;
		}
	}

	@Override
	public String reason() {
		if (this.validity) {
			throw new IllegalStateException("The code should be invalid to use this method");
		} else {
			return "The code length is more than " + this.lineLimit;
		}
	}

	private final int lineLimit;
	private final String code;
	private boolean validity;
}

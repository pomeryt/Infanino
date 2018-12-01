package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It checks the empty lines of code.<br>
 * The code that contains consecutive empty lines would be invalid.
 */
public final class EmptyLineValidity implements Validation {
	
	public EmptyLineValidity(final String code) {
		this.code = code;
	}
	
	@Override
	public boolean valid() {
		final String[] lines = this.code.split("\n");
		final String blankPattern = "^\\s*$";
		final Pattern pattern = Pattern.compile(blankPattern);
		
		for (int index = 0; index < lines.length - 1; index++) {
			final Matcher currentLine = pattern.matcher(lines[index]);
			final Matcher nextLine = pattern.matcher(lines[index + 1]);
			
			if (currentLine.find() && nextLine.find()) {
				this.errorLine = index + 1;
				this.validity = false;
				this.errorMsg = "There are empty lines more than two.";
				return false;
			}
		}
		
		this.validity = true;
		return true;
	}

	@Override
	public int line() {
		if (this.validity) {
			throw new IllegalStateException("This method can be used when the Empty Line is invalid.");
		}
		
		return this.errorLine;
	}

	@Override
	public String reason() {
		if (this.validity) {
			throw new IllegalStateException("This method can be used when the Empty Line is invalid.");
		}
		
		return this.errorMsg;
	}
	
	private String errorMsg;
	private boolean validity;
	private int errorLine;
	private final String code;
}

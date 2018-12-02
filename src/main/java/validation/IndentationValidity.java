package validation;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Check if the indentation is valid or not.
 */
@SuppressWarnings("PMD.GuardLogStatement")
public final class IndentationValidity implements Validation {

	public IndentationValidity(final String code) {
		this.code = code;
	}
	
	@Override
	public boolean valid() {
		if (!this.indentExists()) {
			return true;
		}
		
		final String[] lines = this.code.split("\n");

		if (this.lineHasIndent(lines[0])) {
			this.setError("First line should not have indentation", 1);
			this.validity = false;
			return false;
		}
		
		final int codeLength = lines.length;
		final int indentLineNum = this.firstIndentLineNum(lines, codeLength);
		final char indentType = this.indentType(lines, indentLineNum);
		
		return new ValidationWithStackLogic(
			indentType, codeLength, lines
		).valid(
			(message, lineNum) -> {
				this.setError(message, lineNum);
				this.validity = false;
			},
			(message, lineNum) -> {
				this.setError(message, lineNum);
				this.validity = false;
			},
			this.indentUnit(lines, codeLength, indentLineNum, indentType)
		);
	}

	@Override
	public int line() {
		if (this.validity) {
			throw new IllegalStateException("This method can be used when the indentation is invalid.");
		} else {
			return this.errorLine;
		}
	}

	@Override
	public String reason() {
		if (this.validity) {
			throw new IllegalStateException("This method can be used when the indentation is invalid.");
		} else {
			return this.errorMsg;
		}
	}

	private boolean lineHasIndent(final String line) {
		final Pattern pattern = Pattern.compile(INDENT_PATTERN);
		final Matcher matcher = pattern.matcher(line);
		return matcher.find();
	}
	
	private boolean indentExists() {
		return this.code.contains(String.valueOf(SPACE_BAR)) || this.code.contains(String.valueOf(TAB));
	}

	private int firstIndentLineNum(final String[] lines, final int lineLength) {
		int currentLine = 0;
		final Pattern pattern = Pattern.compile(INDENT_PATTERN);
		Matcher matcher;
		do {

			currentLine++;
			if (currentLine >= lineLength) {
				return 0;
			}
			matcher = pattern.matcher(lines[currentLine]);

		} while (!matcher.find());

		return currentLine;
	}

	private char indentType(final String[] lines, final int indentLine) {
		if (lines[indentLine].charAt(0) == SPACE_BAR) {
			return SPACE_BAR;
		} else if (lines[indentLine].charAt(0) == TAB) {
			return TAB;
		} else {
			throw new IllegalStateException(
					"Expected indentation: space bar or tab." + "Actual indentation: " + lines[indentLine].charAt(0));
		}
	}

	private int indentUnit(final String[] lines, final int lineLength, final int indentLineNum, final char indentType) {

		// Indent Line found is less than line length.
		if (indentLineNum >= lineLength) {
			return 0;
		}

		int indentCharIndex = 0;

		while (lines[indentLineNum].charAt(indentCharIndex) == indentType) {
			indentCharIndex++;
		}

		return indentCharIndex;
	}
	
	private void setError(final String errorMsg, final int errorLine) {
		this.errorMsg = errorMsg;
		this.errorLine = errorLine;
	}

	private interface IndentAmount {
		boolean valid();

		int value();
	}

	private static class ValidIndent implements IndentAmount {
		ValidIndent(final int amount) {
			this.amount = amount;
		}

		@Override
		public boolean valid() {

			return true;
		}

		@Override
		public int value() {
			return this.amount;
		}

		private final int amount;
	}

	private static class InvalidIndent implements IndentAmount {

		InvalidIndent(final char expectedType, final char actualType) {
			this.expectedType = expectedType;
			this.actualType = actualType;
		}

		@Override
		public boolean valid() {

			return false;
		}

		@Override
		public int value() {

			throw new IllegalStateException(
					"Indentation mismatched.\n" + "Expected: " + this.expectedType + "\nActual: " + this.actualType);
		}

		private final char expectedType;
		private final char actualType;
	}
	
	private interface ErrorEvent {
		void handle(String message, int line);
	}
	
	private static class ValidationWithStackLogic {
		
		ValidationWithStackLogic(final char indentType, final int codeLength, final String... lines) {
			this.indentType = indentType;
			this.codeLength = codeLength;
			this.lines = lines.clone();
		}
		
		public boolean valid(final ErrorEvent typeError, final ErrorEvent indentError, final int baseIndent) {
			final char invalidType = this.invalidType(this.indentType); 		
					
			for (int currentLine = 0; currentLine < this.codeLength; currentLine++) {

				final IndentAmount amount = this.indentAmount(this.lines[currentLine], this.indentType);

				if (!amount.valid()) {
					typeError.handle(
						"Indentation mismatched.\n" + "Expected: " + this.indentType + "\nActual: " + invalidType, 
						currentLine + 1
					);
					return false;
				}

				final int currentIndent = amount.value();

				if (this.stack.isEmpty()) {
					this.stack.addElement(currentIndent);
					continue;
				}

				while (currentIndent < this.stack.lastElement()) {
					this.stack.pop();
				}
				
				if (currentIndent == this.stack.lastElement()) {
					continue;
				}
				
				if (currentIndent - baseIndent != this.stack.lastElement()) {
					indentError.handle(
						"Please check the indentation.", 
						currentLine + 1
					);
					return false;
				}
				
				this.stack.addElement(currentIndent);
			}
			
			return true;
		}
		
		private char invalidType(final char indentType) {
			if (indentType == SPACE_BAR) {
				return TAB;
			}
			return SPACE_BAR;
		}
		
		private IndentAmount indentAmount(final String line, final char indentType) {
			final char invalidType = this.invalidType(indentType);

			int indentAmount = -1;

			do {
				indentAmount++;
				if (line.charAt(indentAmount) == invalidType) {
					return new InvalidIndent(indentType, invalidType);
				}
			} while (line.charAt(indentAmount) == indentType);

			return new ValidIndent(indentAmount);
		}
		
		private final char indentType;
		private final int codeLength;
		private final String[] lines;
		
		private final Stack<Integer> stack = new Stack<Integer>();
	}

	private int errorLine;
	private String errorMsg;
	private boolean validity = true;

	private final String code;
	private static final char SPACE_BAR = ' ';
	private static final char TAB = '\t';
	private static final String INDENT_PATTERN = "^\\s+[a-zA-Z0-9]+$";
}

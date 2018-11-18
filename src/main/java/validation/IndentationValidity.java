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
		final String[] lines = this.code.split("\n");

		if (this.lineHasIndent(lines[0])) {
			this.setError("First line should not have indentation", 1);
			this.validity = false;
			return false;
		}
		final Stack<Integer> stack = new Stack<Integer>();
		final int codeLength = lines.length;
		final int indentLineNum = this.firstIndentLineNum(lines, codeLength);
		final char indentType = this.indentType(lines, indentLineNum);
		final char invaldType = (indentType == spaceBar) ? tab : spaceBar;

		for (int currentLine = 0; currentLine < codeLength; currentLine++) {

			final IndentAmount amount = this.indentAmount(lines[currentLine], indentType);

			if (!amount.valid()) {

				this.setError("Indentation mismatched.\n" + "Expected: " + indentType + "\nActual: " + invaldType,
						currentLine + 1);
				this.validity = false;
				return false;
			}

			final int currentIndent = amount.value();

			if (stack.isEmpty()) {
				stack.addElement(currentIndent);
				continue;
			}

			while (currentIndent < stack.lastElement()) {
				stack.pop();
			}
			if (currentIndent == stack.lastElement()) {
				continue;
			}
			final int baseIndent = this.indentUnit(lines, codeLength, indentLineNum, indentType);
			if (currentIndent - baseIndent != stack.lastElement()) {
				this.setError("Please check the indentation.", currentLine + 1);
				this.validity = false;
				return false;
			}
			stack.addElement(currentIndent);

		}
		return this.validity;
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
		final Pattern pattern = Pattern.compile(this.indentPattern);
		final Matcher matcher = pattern.matcher(line);
		return matcher.find();
	}

	private int firstIndentLineNum(final String[] lines, final int lineLength) {
		int currentLine = 0;
		final Pattern pattern = Pattern.compile(this.indentPattern);
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
		if (lines[indentLine].charAt(0) == this.spaceBar) {
			return this.spaceBar;
		} else if (lines[indentLine].charAt(0) == this.tab) {
			return this.tab;
		} else {
			throw new IllegalStateException(
					"Expected indentation: space bar or tab." + "Actual indentation: " + lines[indentLine].charAt(0));

		}
	}

	private int indentUnit(final String[] lines, final int lineLength, final int indentLineNum, final char indentType) {

		if (indentLineNum >= lineLength) { // Indent Line found is less than line length.
			return 0;
		}

		int indentCharIndex = 0;

		while (lines[indentLineNum].charAt(indentCharIndex) == indentType) {
			indentCharIndex++;
		}

		return indentCharIndex;
	}

	private IndentAmount indentAmount(final String line, final char indentType) {
		char invalidType = this.spaceBar;
		if (indentType == this.spaceBar) {
			invalidType = this.tab;
		}

		int indentAmount = -1;

		do {
			indentAmount++;
			if (line.charAt(indentAmount) == invalidType) {
				return new InvalidIndent(indentType, invalidType);
			}
		} while (line.charAt(indentAmount) == indentType);

		return new ValidIndent(indentAmount);
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
		public ValidIndent(final int amount) {
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

		final int amount;
	}

	private static class InvalidIndent implements IndentAmount {

		public InvalidIndent(final char expectType, final char actualType) {
			this.expectType = expectType;
			this.actualType = actualType;
		}

		@Override
		public boolean valid() {

			return false;
		}

		@Override
		public int value() {

			throw new IllegalStateException(
					"Indentation mismatched.\n" + "Expected: " + this.expectType + "\nActual: " + this.actualType);
		}

		final private char expectType;
		final private char actualType;
	}

	private int errorLine;
	private String errorMsg;
	private boolean validity = true;

	private final char spaceBar = ' ';
	private final char tab = '\t';
	private final String code;
	private final String indentPattern = "^\\s+[a-zA-Z0-9]+$";

}

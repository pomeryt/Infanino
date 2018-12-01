package validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

public final class CodeLengthValidityTest {
	private final static String WORD = "apple\n";

	@Test
	public void validIfCodeLengthLessThanLineLimit() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i < 199; i++) {
			code.append(WORD);
		}
		code.append("banana");
		MatcherAssert.assertThat(new CodeLengthValidity(code.toString(),200).valid(), CoreMatchers.equalTo(true));
	}

	@Test
	public void shouldThrowWhenObtainLineForValidCase() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i < 199; i++) {
			code.append(WORD);
		}
		code.append("cocoa");
		final Validation codeLength = new CodeLengthValidity(code.toString(),200);

		if (codeLength.valid()) {
			assertThrows(IllegalStateException.class, () -> codeLength.line());
		} else {
			throw new IllegalStateException("The code length should be valid.");
		}
	}

	@Test
	public void shouldThrowWhenObtainReasonForValidCase() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i < 199; i++) {
			code.append(WORD);
		}
		code.append("dodoria");
		final Validation codeLength = new CodeLengthValidity(code.toString(),200);

		if (codeLength.valid()) {
			assertThrows(IllegalStateException.class, () -> codeLength.reason());
		} else {
			throw new IllegalStateException("The code length should be valid");
		}
	}

	@Test
	public void invalidIfCodeLengthMoreThanLineLimit() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i <= 199; i++) {
			code.append(WORD);
		}
		code.append("egg");
		MatcherAssert.assertThat(new CodeLengthValidity(code.toString(),200).valid(), CoreMatchers.equalTo(false));
	}

	@Test
	public void shouldGiveLineNumberWhenInvalid() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i <= 199; i++) {
			code.append(WORD);
		}
		code.append("fire");
		final Validation codeLength = new CodeLengthValidity(code.toString(),200);

		if (codeLength.valid()) {
			throw new IllegalStateException("It was valid when it shouldn't be.");
		} else {
			MatcherAssert.assertThat(codeLength.line(), CoreMatchers.equalTo(201));
		}
	}

	@Test
	public void shouldGiveReasonWhenInvalid() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i <= 199; i++) {
			code.append(WORD);
		}
		code.append("gomen");
		final Validation codeLength = new CodeLengthValidity(code.toString(),200);

		if (codeLength.valid()) {
			throw new IllegalStateException("It was valid when it shouldn't be.");
		} else {
			MatcherAssert.assertThat(codeLength.reason(),
					CoreMatchers.equalTo("The code length is more than " + 200));
		}
	}
	
	@Test
	public void excessiveEmptyLinesAtEndShouldBeInvalid() {
		final String code = "one\ntwo\nthree\n\n\n\n\n\n\n\n";
		MatcherAssert.assertThat(new CodeLengthValidity(code, 3).valid(), CoreMatchers.equalTo(false));
	}
}

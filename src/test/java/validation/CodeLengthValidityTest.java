package validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

public final class CodeLengthValidityTest {
	final String word = "apple\n";

	@Test
	void validIfCodeLengthLessThanLineLimit() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i < 199; i++) {
			code.append(this.word);
		}
		code.append("banana");
		MatcherAssert.assertThat(new CodeLengthValidity(code.toString(),200).valid(), CoreMatchers.equalTo(true));
	}

	@Test
	void shouldThrowWhenObtainLineForValidCase() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i < 199; i++) {
			code.append(this.word);
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
	void shouldThrowWhenObtainReasonForValidCase() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i < 199; i++) {
			code.append(this.word);
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
	void invalidIfCodeLengthMoreThanLineLimit() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i <= 199; i++) {
			code.append(this.word);
		}
		code.append("egg");
		MatcherAssert.assertThat(new CodeLengthValidity(code.toString(),200).valid(), CoreMatchers.equalTo(false));
	}

	@Test
	void shouldGiveLineNumberWhenInvalid() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i <= 199; i++) {
			code.append(this.word);
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
	void shouldGiveReasonWhenInvalid() {

		final StringBuilder code = new StringBuilder();
		for (int i = 0; i <= 199; i++) {
			code.append(this.word);
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

}

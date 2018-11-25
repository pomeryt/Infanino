package validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

public final class CodeLengthValidityTest {
	final private int lineLimit = 200;
	final String word = "apple\n";

	@Test
	void validIfCodeLengthLessThanLineLimit() {

		final StringBuilder code = new StringBuilder("");
		for (int i = 0; i < this.lineLimit; i++) {
			code.append(this.word);
		}
		MatcherAssert.assertThat(new CodeLengthValidity(code.toString()).valid(), CoreMatchers.equalTo(true));
	}

	@Test
	void shouldThrowWhenObtainLineForValidCase() {

		final StringBuilder code = new StringBuilder("");
		for (int i = 0; i < this.lineLimit; i++) {
			code.append(this.word);
		}
		final Validation codeLength = new CodeLengthValidity(code.toString());

		if (codeLength.valid()) {
			assertThrows(IllegalStateException.class, () -> codeLength.line());
		} else {
			throw new IllegalStateException("The code length should be valid.");
		}
	}

	@Test
	void shouldThrowWhenObtainReasonForValidCase() {

		final StringBuilder code = new StringBuilder("");
		for (int i = 0; i < this.lineLimit; i++) {
			code.append(this.word);
		}
		final Validation codeLength = new CodeLengthValidity(code.toString());

		if (codeLength.valid()) {
			assertThrows(IllegalStateException.class, () -> codeLength.reason());
		} else {
			throw new IllegalStateException("The code length should be valid");
		}
	}

	@Test
	void invalidIfCodeLengthMoreThanLineLimit() {

		final StringBuilder code = new StringBuilder("");
		for (int i = 0; i <= this.lineLimit; i++) {
			code.append(this.word);
		}
		MatcherAssert.assertThat(new CodeLengthValidity(code.toString()).valid(), CoreMatchers.equalTo(false));
	}

	@Test
	void shouldGiveLineNumberWhenInvalid() {

		final StringBuilder code = new StringBuilder("");
		for (int i = 0; i <= this.lineLimit; i++) {
			code.append(this.word);
		}
		final Validation codeLength = new CodeLengthValidity(code.toString());

		if (codeLength.valid()) {
			throw new IllegalStateException("It was valid when it shouldn't be.");
		} else {
			MatcherAssert.assertThat(codeLength.line(), CoreMatchers.equalTo(this.lineLimit + 1));
		}
	}

	@Test
	void shouldGiveReasonWhenInvalid() {

		final StringBuilder code = new StringBuilder("");
		for (int i = 0; i <= this.lineLimit; i++) {
			code.append(this.word);
		}
		final Validation codeLength = new CodeLengthValidity(code.toString());

		if (codeLength.valid()) {
			throw new IllegalStateException("It was valid when it shouldn't be.");
		} else {
			MatcherAssert.assertThat(codeLength.reason(),
					CoreMatchers.equalTo("The code length is more than " + this.lineLimit));
		}
	}

}

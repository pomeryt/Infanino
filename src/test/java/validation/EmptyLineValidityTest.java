package validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
final class EmptyLineValidityTest {
	@Test
	void emptyLineIsLessThanOne() {
		final String code = "one\n" + 
				"two\n" + 
				"three\n" + 
				"\n" + 
				"four\n" + 
				"\n" + 
				"five";
		MatcherAssert.assertThat(
			new EmptyLineValidity(code).valid(), 
			CoreMatchers.equalTo(true)
		);
	}
	@Test
	void emptyLineIsLessThanOneAndHasIndent() {
		final String code = "one\n" + 
				"two\n" + 
				"three\n" + 
				"\t \n" + 
				"four\n" + 
				" \t\n" + 
				"five";
		MatcherAssert.assertThat(
			new EmptyLineValidity(code).valid(), 
			CoreMatchers.equalTo(true)
		);
	}
	@Test
	void emptyLineIsMoreThanTwo() {
		final String code = "one\n" + 
				"\n" + 
				"two\n" + 
				"\n" + 
				"\n" + 
				"three\n" + 
				"four\n" + 
				"five";
		MatcherAssert.assertThat(
			new EmptyLineValidity(code).valid(), 
			CoreMatchers.equalTo(false)
		);
	}
	@Test
	void emptyLineIsMoreThanTwoAndHasIndent() {
		final String code = "one\n" + 
				" \n" + 
				"two\n" + 
				"\t \n" + 
				" \t\n" + 
				"three\n" + 
				"four\n" + 
				"five";
		MatcherAssert.assertThat(
			new EmptyLineValidity(code).valid(), 
			CoreMatchers.equalTo(false)
		);
	}
	@Test
	void shouldGiveLineNumberWhenInvalid() {
		final String code = "one\n" + 
				"\n" + 
				"two\n" + 
				"\n" + 
				"\n" + 
				"three\n" + 
				"four\n" + 
				"five";
		final Validation lineEmpty = new EmptyLineValidity(code);
		if (lineEmpty.valid()) {
			throw new IllegalStateException(
				"It was valid when it shouldn't be."
			);
		} else {
			MatcherAssert.assertThat(
				lineEmpty.line(), 
				CoreMatchers.equalTo(4)
			);
		}
	}
	@Test
	void shouldThrowWhenObtainLineForValidCase() {
		final String code = "one\n" + 
				"two\n" + 
				"three\n" + 
				"\n" + 
				"four\n" + 
				"\n" + 
				"five";
		final Validation lineEmpty = new IndentationValidity(code);
		if (lineEmpty.valid()) {
			assertThrows(IllegalStateException.class, () -> lineEmpty.line());
		} else { 
			throw new IllegalStateException("The code should be valid.");
		}
	}
	
	@Test
	void shouldGiveReasonWhenInvalid() {
		final String code = "one\n" + 
				"\n" + 
				"two\n" + 
				"\n" + 
				"\n" + 
				"three\n" + 
				"four\n" + 
				"five";
		final Validation lineEmpty = new EmptyLineValidity(code);
		if (lineEmpty.valid()) {
			throw new IllegalStateException(
				"It was valid when it shouldn't be."
			);
		} else {
			MatcherAssert.assertThat(
				lineEmpty.reason(), 
				CoreMatchers.equalTo("There are empty lines more than two.")
			);
		}
	}
	@Test
	void shouldGiveReasonWhenInvalidAndHasIndent() {
		final String code = "one\n" + 
				" \n" + 
				"two\n" + 
				"\t\n" + 
				" \t\n" + 
				"three\n" + 
				"four\n" + 
				"five";
		final Validation lineEmpty = new EmptyLineValidity(code);
		if (lineEmpty.valid()) {
			throw new IllegalStateException(
				"It was valid when it shouldn't be."
			);
		} else {
			MatcherAssert.assertThat(
				lineEmpty.reason(), 
				CoreMatchers.equalTo("There are empty lines more than two.")
			);
		}
	}
	@Test
	void shouldThrowWhenObtainReasonForValidCase() {
		final String code = "one\n" + 
				"two\n" + 
				"three\n" + 
				"\n" + 
				"four\n" + 
				"\n" + 
				"five";
		final Validation lineEmpty = new EmptyLineValidity(code);
		if (lineEmpty.valid()) {
			assertThrows(IllegalStateException.class, () -> lineEmpty.reason());
		} else {
			throw new IllegalStateException("The code should be valid.");
		}
	}
}

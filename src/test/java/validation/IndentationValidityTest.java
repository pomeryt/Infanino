package validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.TooManyMethods")
final class IndentationValidityTest {

	@Test
	void firstLineShouldNotStartWithSpace() {
		final String code = " apple";
		MatcherAssert.assertThat(
			new IndentationValidity(code).valid(), 
			CoreMatchers.equalTo(false)
		);
	}
	
	@Test
	void firstLineShouldNotStartWithTab() {
		final String code = "\tapple";
		MatcherAssert.assertThat(
			new IndentationValidity(code).valid(), 
			CoreMatchers.equalTo(false)
		);
	}
	
	@Test
	void validCaseThatFirstSpaceBecomesTheLengthOfIndentation() {
		final String code = "first\n  second\n    thrid";
		MatcherAssert.assertThat(
			new IndentationValidity(code).valid(), 
			CoreMatchers.equalTo(true)
		);
	}
	
	@Test
	void invalidCaseThatFirstSpaceBecomesTheLengthOfIndentation() {
		final String code = "first\n  second\n   thrid";
		MatcherAssert.assertThat(
			new IndentationValidity(code).valid(), 
			CoreMatchers.equalTo(false)
		);
	}

	@Test
	void validCaseThatTabCanBeUsedToDetermineTheLengthOfIndentation() {
		final String code = "first\n\tsecond\n\t\tthrid";
		MatcherAssert.assertThat(
			new IndentationValidity(code).valid(), 
			CoreMatchers.equalTo(true)
		);
	}
	
	@Test
	void invalidCaseThatTabCanBeUsedToDetermineTheLengthOfIndentation() {
		final String code = "first\n\tsecond\n\t\t thrid";
		MatcherAssert.assertThat(
			new IndentationValidity(code).valid(), 
			CoreMatchers.equalTo(false)
		);
	}
	
	@Test
	void shouldNotIndentMoreThanOnceAtATime() {
		final String code = "a\n b\n   c";
		MatcherAssert.assertThat(
			new IndentationValidity(code).valid(), 
			CoreMatchers.equalTo(false)
		);
	}
	
	@Test
	void shouldGiveLineNumberWhenInvalid() {
		final String code = "one\ntwo\n three\n  four\n    five";
		final Validation indentation = new IndentationValidity(code);
		if (indentation.valid()) {
			throw new IllegalStateException(
				"It was valid when it shouldn't be."
			);
		} else {
			MatcherAssert.assertThat(
				indentation.line(), 
				CoreMatchers.equalTo(5)
			);
		}
	}
	
	@Test
	void shouldGiveReasonWhenInvalid() {
		final String code = "uno\n dos\n   tres";
		final Validation indentation = new IndentationValidity(code);
		if (indentation.valid()) {
			throw new IllegalStateException(
				"It was valid when it shouldn't be."
			);
		} else {
			MatcherAssert.assertThat(
				indentation.reason(), 
				CoreMatchers.equalTo("Please check the indentation.")
			);
		}
	}
	
	@Test
	void shouldThrowWhenObtainReasonForValidCase() {
		final String code = "one\n\ttwo\n\tthree";
		final Validation indentation = new IndentationValidity(code);
		if (indentation.valid()) {
			assertThrows(IllegalStateException.class, () -> indentation.reason());
		} else {
			throw new IllegalStateException("The indentation should be valid.");
		}
	}
	
	@Test
	void shouldThrowWhenObtainLineForValidCase() {
		final String code = "one\n two\n three";
		final Validation indentation = new IndentationValidity(code);
		if (indentation.valid()) {
			assertThrows(IllegalStateException.class, () -> indentation.line());
		} else { 
			throw new IllegalStateException("The indentation should be valid.");
		}
	}
}
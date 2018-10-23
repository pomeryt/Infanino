package validation;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

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
		MatcherAssert.assertThat(
			new IndentationValidity(code).line(), 
			CoreMatchers.equalTo(5)
		);
	}
	
	@Test
	void shouldGiveReasonWhenInvalid() {
		final String code = "uno\n dos\n   tres";
		MatcherAssert.assertThat(
			new IndentationValidity(code).reason(), 
			CoreMatchers.equalTo("Please check indentation.")
		);
	}
}

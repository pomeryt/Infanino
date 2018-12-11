package validation;

/**
 * Check if the code is written in valid way. <br>
 * The program should validate the code before compilation.
 */
public interface Validation {
	/**
	 * Check if the code is valid or not.
	 * @return Validity.
	 */
	boolean valid();
	
	/**
	 * @return Line number of invalid code.
	 */
	int line();
	
	/**
	 * @return Reason why the code is invalid.
	 */
	String reason();
}

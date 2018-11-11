package validation;

/**
 * Check if the code is written in valid way. <br>
 * The program should validate the code before compilation.
 */
public interface Validation {
	/**
	 * Check if the code is valid or not.
	 * @return validity.
	 */
	boolean valid();
	
	/**
	 * @return line number of invalid code.
	 */
	int line();
	
	/**
	 * @return reason why the code is invalid.
	 */
	String reason();
}

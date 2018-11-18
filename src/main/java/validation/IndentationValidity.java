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
		if(lines == null) {
			lines = code.split("\n");   	// code split
			lineLength = lines.length;	
		}
		
		calcIndentUnit();			// Calculate Base Indent
		final Stack<Integer> stack = new Stack<Integer>();
		
		if(firstLineStartWithIndent()) {				// first line doesn't have indent
			for (int currentLine=0;currentLine<lineLength;currentLine++) {	
				
				final IndentAmount amount = this.indentAmount(lines[currentLine]);	
				
				if(amount.valid()) {					
					final int currentIndent = amount.value();
					if(stack.isEmpty()) {
						stack.addElement(currentIndent);
					} else {
						
						while(currentIndent<stack.lastElement()) {
							stack.pop();
							
						}
						
						if(currentIndent>stack.lastElement()) {
							if(currentIndent - baseIndent == stack.lastElement()) {
								stack.addElement(currentIndent);
							} else {
								setError("Please check the indentation.", currentLine+1);
								validity =false;
								return false;
							}
						}
					}
				} else {
					setError("Indentation Type Miss Match", currentLine+1);
					validity = false;
					return false;
				}
			
				
			}
		} else {
			
			setError("First line should not have indentation", 1);
			validity = false;
			
		}
	
		return validity;
	}
	@Override
	public int line() {
		if(validity) {
			throw new IllegalStateException("The indentation should be valid.");
		}else {
			return errorLine;			
		}
	}
	@Override
	public String reason() {
		if(validity) {
			throw new IllegalStateException("The indentation should be valid.");
		} else {
			return errorMsg;
		}
	}
	private boolean firstLineStartWithIndent() {
		final Pattern pattern = Pattern.compile(nonIndentPattern);	
		final Matcher matcher = pattern.matcher(lines[0]);
		return matcher.find();
	}
	
	private int firstIndentLine() {
		int currentLine = 0;  
		final Pattern pattern = Pattern.compile(indentPattern);
		Matcher matcher ;
		do{
			
			currentLine++;
			if(currentLine>=lineLength) {
				baseIndent = 0;
				break;
			}
			matcher = pattern.matcher(lines[currentLine]);
			
		}while(!matcher.find());
		 
		 
		return currentLine;
	}
	
	
	private char type(final int indentLine) {
		if(lines[indentLine].charAt(0) == spaceBar) {
			indentType = spaceBar;
			return spaceBar;
		} else if(lines[indentLine].charAt(0) == tab) {
			indentType = tab;
			return tab;
		} else {
			throw new IllegalStateException("Expected indentation: space bar or tab."
					+ "Actual indentation: "+lines[indentLine].charAt(0));
			
		}
	}
	private void calcIndentUnit()  {
		int indent= 0;
		int indentLine = 0;
		indentLine = firstIndentLine();
		
		
		if(indentLine<lineLength) { // Indent Line found is less than line length.
		
			type(indentLine);
			
			while(lines[indentLine].charAt(indent) == indentType){ indent++; }
			
			this.baseIndent = indent;
		}
	}
	
	
	private IndentAmount indentAmount(final String line) {
		char indentTypeR = spaceBar;
		if(indentType == spaceBar) {
			indentTypeR = tab;
		}
		
		
		int indentAmount=0;
		
		if(line.charAt(indentAmount) == indentTypeR) {return new InvalidIndent();}
		
		while(line.charAt(indentAmount) == indentType) {
			indentAmount++;
			if(line.charAt(indentAmount) == indentTypeR) {
				
				return new InvalidIndent();
			}
		}
		
		return new ValidIndent(indentAmount);
	}
	private void setError(final String errorMsg,final int errorLine) {
		this.errorMsg = errorMsg;
		this.errorLine = errorLine;
	}
	
	
	private interface IndentAmount {
		  boolean valid();
		  int value();
		}

	private class ValidIndent implements IndentAmount {
		public ValidIndent(final int amount) {
			this.amount = amount;
		}
		@Override
		public boolean valid() {
			
			return true; 
		}
		
		@Override
		public int value() { return amount; }
		final int amount;
	}
	private class InvalidIndent implements IndentAmount{

		@Override
		public boolean valid() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int value() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
		
	private char indentType;
	private int baseIndent;
	private int errorLine;
	private String errorMsg;
	private boolean validity = true;;
	private  int lineLength;
	private String[] lines;
	
	private final char spaceBar = ' ';
	private final char tab = '\t';
	
	private final String code;
	private final String nonIndentPattern="^[a-zA-Z0-9]+$";
	private final String indentPattern = "^\\s+[a-zA-Z0-9]+$"; 
	
	
}


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
		this.lines = code.split("\n");   	// code split
		this.lineLength = this.lines.length;	
		
		calcIndentUnit();			// Calculate Base Indent
		
		if(firstLineHaveIndent()) {				// first line doesn't have indent
			setError("First line should not have indentation", 1);
			this.validity = false;
			return false;
		}
		final Stack<Integer> stack = new Stack<Integer>();
		
		for (int currentLine=0;currentLine<this.lineLength;currentLine++) {	
			
			final IndentAmount amount = this.indentAmount(lines[currentLine]);	
			
			if(!amount.valid()) {					
				setError("Indentation Type Miss Match", currentLine+1);
				this.validity = false;
				return false;
			} 
			
			final int currentIndent = amount.value();
			if(stack.isEmpty()) {
				stack.addElement(currentIndent);
			}else{
				while(currentIndent<stack.lastElement()) {
					stack.pop();
					
				}
				
				if(currentIndent>stack.lastElement()) {
					if(currentIndent - this.baseIndent != stack.lastElement()) {
						setError("Please check the indentation.", currentLine+1);
						this.validity =false;
						return false;
					}
						
					stack.addElement(currentIndent);
					
				}
				
			}
				
			
		}
		return this.validity;
	}
	@Override
	public int line() {
		if(this.validity) {
			throw new IllegalStateException("The indentation should be valid.");
		}else {
			return this.errorLine;			
		}
	}
	@Override
	public String reason() {
		if(this.validity) {
			throw new IllegalStateException("The indentation should be valid.");
		} else {
			return this.errorMsg;
		}
	}
	private boolean firstLineHaveIndent() {
		final Pattern pattern = Pattern.compile(this.indentPattern);	
		final Matcher matcher = pattern.matcher(this.lines[0]);
		return matcher.find();
	}
	
	private int firstIndentLine() {
		int currentLine = 0;  
		final Pattern pattern = Pattern.compile(this.indentPattern);
		Matcher matcher ;
		do{
			
			currentLine++;
			if(currentLine>=this.lineLength) {
				this.baseIndent = 0;
				break;
			}
			matcher = pattern.matcher(this.lines[currentLine]);
			
		}while(!matcher.find());
		 
		 
		return currentLine;
	}
	
	
	private char type(final int indentLine) {
		if(this.lines[indentLine].charAt(0) == this.spaceBar) {
			this.indentType = this.spaceBar;
			return this.spaceBar;
		} else if(this.lines[indentLine].charAt(0) == this.tab) {
			this.indentType = this.tab;
			return this.tab;
		} else {
			throw new IllegalStateException("Expected indentation: space bar or tab."
					+ "Actual indentation: "+this.lines[indentLine].charAt(0));
			
		}
	}
	private void calcIndentUnit()  {
		int indent= 0;
		int indentLine = 0;
		indentLine = firstIndentLine();
		
		
		if(indentLine<this.lineLength) { // Indent Line found is less than line length.
		
			type(indentLine);
			
			while(this.lines[indentLine].charAt(indent) == this.indentType){ indent++; }
			
			this.baseIndent = indent;
		}
	}
	
	
	private IndentAmount indentAmount(final String line) {
		char indentTypeR = this.spaceBar;
		if(this.indentType == this.spaceBar) {
			indentTypeR = this.tab;
		}
		
		int indentAmount=0;
		
		if(line.charAt(indentAmount) == indentTypeR) {return new InvalidIndent();}
		
		while(line.charAt(indentAmount) == this.indentType) {
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

	private static class ValidIndent implements IndentAmount {
		public ValidIndent(final int amount) {
			this.amount = amount;
		}
		@Override
		public boolean valid() {
			
			return true; 
		}
		
		@Override
		public int value() { return this.amount; }
		final int amount;
	}
	private static class InvalidIndent implements IndentAmount{

		@Override
		public boolean valid() {
		
			return false;
		}

		@Override
		public int value() {
			
			throw new IllegalStateException("Indent type miss match");
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
	
	private final String indentPattern = "^\\s+[a-zA-Z0-9]+$"; 
	
	
}


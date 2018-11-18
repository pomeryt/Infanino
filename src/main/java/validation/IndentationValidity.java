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
		part = code.split("\n");   	// code split
		codeLine = part.length;			
		validity = true;
	}
	
	@Override
	public boolean valid() {
		
		calIndentNum();			// Calculate Base Indent
		final Stack<Integer> stack = new Stack<Integer>();
		
		if(isNonSpaceFirstLine()) {				// first line doesn't have indent
			for (int i=0;i<codeLine;i++) {	
				
				final int num = getIndent(part[i]);	
				
				if(num == -1) {
					setError("Indentation Type Miss Match", i+1);
					validity = false;
					return false;
				}
				
				if(compareIndentNum(stack, num,i)) {continue;}
				else {return false;}
				
			}
		} else {
			
			setError("First Line Do Not Has Indentation", 1);
			validity = false;
			
		}
	
		return validity;
	}
	@Override
	public int line() {
		if(validity) {
			throw new IllegalStateException();
		}else {
			return errorLine;			
		}
	}
	@Override
	public String reason() {
		if(validity) {
			throw new IllegalStateException();
		}
		else {
			return error;
		}
	}
	private boolean isNonSpaceFirstLine() {
		final Pattern patternFirstSpace = Pattern.compile(nonSpacePattern);	
		final Matcher matcher = patternFirstSpace.matcher(part[0]);
		return matcher.find();
	}
	private int findIndentLine() {
		int currentLine = 0;  
		final Pattern spacePattern = Pattern.compile(spacePatternRegex);
		Matcher matcher = spacePattern.matcher(part[currentLine]);
		do{
			
			currentLine++;
			if(currentLine>=codeLine) {
				indentLength = 0;
				break;
			}
			matcher = spacePattern.matcher(part[currentLine]);
			
		}while(!matcher.find());
		 
		 
		return currentLine;
	}
	private char checkType(final int indentLine) {
		if(part[indentLine].charAt(0) == space) {
			indentType = space;
			return space;
		}
		else if(part[indentLine].charAt(0) == tab) {
			indentType = tab;
			return tab;
		}
		return '\0';
	}
	private void calIndentNum() {
		int indent= 0;
		int indentLine = 0;
		indentLine = findIndentLine();
		
		
		if(indentLine<codeLine) { 
		
			checkType(indentLine);
			
			while(part[indentLine].charAt(indent) == indentType){ indent++; }
			
			this.indentLength = indent;
		}
	}
	private boolean compareIndentNum(final Stack<Integer> stack,final int num,final int lineNum) {
		if(stack.isEmpty()) {
			stack.addElement(num);
		}
		else {
			while(num<stack.lastElement()) {
				stack.pop();
				
			}
			
			if(num>stack.lastElement()) {
				if(num - indentLength == stack.lastElement()) {
					stack.addElement(num);
				}
				else {
					setError("Please check the indentation.", lineNum+1);
					validity =false;
					return false;
				}
			}
		}
				
		return true;
	}
	private int getIndent(final String part) {
		char indentTypeR = space;
		if(indentType == space) {
			indentTypeR = tab;
		}
		
		
		int indent=0;
		
		if(part.charAt(indent) == indentTypeR) {return -1;}
		
		while(part.charAt(indent) == indentType) {
			indent++;
			if(part.charAt(indent) == indentTypeR) {
				
				return -1;
			}
		}
		
		return indent;
	}
	private void setError(final String errorType,final int errorLine) {
		error = errorType;
		this.errorLine = errorLine;
	}
	
	
	
	private char indentType;
	private int indentLength;
	private int errorLine;
	private String error;
	private boolean validity;
	
	private final String[] part;
	private final int codeLine;
	private final char space = ' ';
	private final char tab = '\t';
	
	private final String nonSpacePattern="^[a-zA-Z0-9]+$";
	private final String spacePatternRegex = "^\\s+[a-zA-Z0-9]+$"; 
	
	
}


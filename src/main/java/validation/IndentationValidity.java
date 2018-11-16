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
		final Pattern patternFirstSpace = Pattern.compile(nonSpacePattern);	
		final Matcher matcher = patternFirstSpace.matcher(part[0]);
		calIndentNum();			// Calculate Base Indent
		final Stack<Integer> stack = new Stack<Integer>();
		if(matcher.find()) {				// first line doesn't have indent
			for (int i=1;i<codeLine;i++) {	
				
				final int num = getIndent(part[i]);	
				
				if(num == -1) {
					setError("Indentation Type Miss Match", i+1);
					validity = false;
					return false;
				}
				
				if(stack.isEmpty()) {
					stack.addElement(num);
				}
				else {
					while(num<stack.lastElement()) {
						stack.pop();
					}
					if(num>stack.lastElement()) {
						if(num - indent == stack.lastElement()) {
							stack.addElement(num);
						}
						else {
							setError("Please check the indentation.", i+1);
							validity =false;
							return false;
						}
					}
					else if(num==stack.lastElement()) {
						continue;
					}
				}
			}
		}
		else {
			
			setError("First Line Do Not Has Indentation", 1);
			validity = false;
			
		}
	
		return validity;
	}

	@Override
	public int line() {
		if(validity) {
			throw new IllegalStateException();
		}
		else {
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
	private void calIndentNum() {
		
		int indent= 0;
		int index = 1;  // second line start
		if(index >= codeLine) {
			this.indent = indent;
			return;
		}
		final Pattern pattern = Pattern.compile(spacePattern);
		Matcher matcher = pattern.matcher(part[index]);
		
		while(!matcher.find()) {
			
			index++;
			if(index>=codeLine) {
				this.indent = indent;
				return;
			}
			matcher = pattern.matcher(part[index]);
			
		}
		int pos=0;

		if(part[index].charAt(0) == space) {
			while(part[index].charAt(pos) == space){
				pos++;
				indent++;
			}
			checkType = "space";
		}
		else if(part[index].charAt(0)==tab) {
			while(part[index].charAt(pos) == tab){
				pos++;
				indent++;
			}
			checkType = "tab";
		}
		this.indent = indent;
	}
	
	private int getIndent(final String part) {
		
		int num=0;
		int pos=0;
		
		if("space".equals(checkType)) {
			if(part.charAt(pos) == tab) {
				
				return -1;
			}
			
			while(part.charAt(pos) == space) {
				pos++;
				if(part.charAt(pos) == tab) {
					
					return -1;
				}
			}
			num = pos;
		}
		else if("tab".equals(checkType)) {
			if(part.charAt(pos) == space) {
				
				return -1;
			}
			while(part.charAt(pos)==tab) {
				pos++;
				if(part.charAt(pos) == space) {
					
					return -1;
				}
			}
			num = pos;
		}
		
		
		return num;
	}
	
	private void setError(final String errorType,final int errorLine) {
		error = errorType;
		this.errorLine = errorLine;
	}
	
	
	private final String[] part;
	private final int codeLine;
	private String checkType;
	private int indent;
	private int errorLine;
	private String error;
	private boolean validity;
	
	
	private final char space = ' ';
	private final char tab = '\t';
	
	private final String nonSpacePattern="^[a-zA-Z0-9]+$";
	private final String spacePattern = "^\\s+[a-zA-Z0-9]+$"; 
	
	
}


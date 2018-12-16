package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validate model declaration
 */
public final class ModelDeclarationValidity implements Validation {

    public ModelDeclarationValidity(final String code) {
        this.code = code;
    }

    @Override
    public boolean valid() {
        final String[] lines = this.code.split("\n");
        final int codeLength = lines.length;
        for (int currentLine = 0; currentLine < codeLength; currentLine++) {
            if (!this.lineHasModelKeyword(lines[currentLine])) {
                continue;
            }
            if (!this.nothingAfterModelKeyword(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "No character allowed after the \"model\" keyword.";
                return false;
            }
            if (this.onlyHasBlankBeforeModelKeyword(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "The model name is required.";
                return false;
            }
            if (!this.oneSpaceBeforeModelKeyword(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "There should be only one space between name and \"model\" keyword.";
                return false;
            }
            if (!this.firstLetterIsCaptialized(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "The first letter of model name should be capitalized.";
                return false;
            }
        }
        return true;
    }

    @Override
    public int line() {
        return this.errLine;
    }

    @Override
    public String reason() {
        return this.errMsg;
    }

    private boolean lineHasModelKeyword(final String line) {
        final Pattern pattern = Pattern.compile(HAS_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean nothingAfterModelKeyword(final String line) {
        final Pattern pattern = Pattern.compile(NOTHING_AFTER_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean onlyHasBlankBeforeModelKeyword(final String line) {
        final Pattern pattern = Pattern.compile(ONLY_BLANK_BEFORE_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean oneSpaceBeforeModelKeyword(final String line) {
        final Pattern pattern = Pattern.compile(ONE_SPACE_BEFORE_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean firstLetterIsCaptialized(final String line) {
        final Pattern pattern = Pattern.compile(FIRST_LETTER_CAPTIALIZED_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private String errMsg;
    private int errLine;

    private final String code;

    private static final String FIRST_LETTER_CAPTIALIZED_PATTERN = "^\\s*[A-Z][A-Za-z0-9]*\\smodel$";
    private static final String ONE_SPACE_BEFORE_MODEL_PATTERN = "^\\s*[A-Za-z0-9]+\\smodel$";
    private static final String ONLY_BLANK_BEFORE_MODEL_PATTERN = "^\\s*model$";
    private static final String NOTHING_AFTER_MODEL_PATTERN = "^([\\S\\s]*\\s+|\\s?)model$";
    private static final String HAS_MODEL_PATTERN = "^([\\S\\s]*\\s+|\\s?)model(\\s?|\\s+[\\s\\S]*)$";
}

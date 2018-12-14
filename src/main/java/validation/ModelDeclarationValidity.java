package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Check Keyword rule for Model
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
            if (!this.lineHasModel(lines[currentLine])) {
                continue;
            }
            if (!this.nothingAfterModel(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "No character allowed after the \"model\" keyword.";
                return false;
            }
            if (this.onlyHasBlankBeforeModel(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "The model name is required.";
                return false;
            }
            if (!this.oneSpacebarBeforeModel(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "There should be only one space between name and \"model\" keyword.";
                return false;
            }
            if (!this.firstCharacterIsCaptial(lines[currentLine])) {
                this.errLine = currentLine + 1;
                this.errMsg = "Should be first character is capitalized with model keyword.";
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

    private boolean lineHasModel(final String line) {
        final Pattern pattern = Pattern.compile(HAS_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean nothingAfterModel(final String line) {
        final Pattern pattern = Pattern.compile(NOTHING_AFTER_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean onlyHasBlankBeforeModel(final String line) {
        final Pattern pattern = Pattern.compile(ONLY_BLANK_BEFORE_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean oneSpacebarBeforeModel(final String line) {
        final Pattern pattern = Pattern.compile(ONE_SPACEBAR_BEFORE_MODEL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private boolean firstCharacterIsCaptial(final String line) {
        final Pattern pattern = Pattern.compile(FIRST_CHARACTER_CAPTIAL_PATTERN);
        final Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private String errMsg;
    private int errLine;

    private final String code;

    private static final String FIRST_CHARACTER_CAPTIAL_PATTERN = "^\\s*[A-Z][A-Za-z0-9]*\\smodel$";
    private static final String ONE_SPACEBAR_BEFORE_MODEL_PATTERN = "^\\s*[A-Za-z0-9]+\\smodel$";
    private static final String ONLY_BLANK_BEFORE_MODEL_PATTERN = "^\\s*model$";
    private static final String NOTHING_AFTER_MODEL_PATTERN = "^([\\S\\s]*\\s+|\\s?)model$";
    private static final String HAS_MODEL_PATTERN = "^([\\S\\s]*\\s+|\\s?)model(\\s?|\\s+[\\s\\S]*)$";
}

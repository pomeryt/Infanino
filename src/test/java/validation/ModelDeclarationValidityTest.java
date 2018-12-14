package validation;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.TooManyMethods")
final class ModelDeclarationValidityTest {
    private final static String THROW_MESSAGE ="It was valid when it shouldn't be.";
    @Test
    public void validCaseModelDeclaration() {
        final String code = "User model\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void validCaseIfThatHasTabBeforeModelName() {
        final String code = "\tUser model\n\t\tIt has id in Int\n\t\tIt prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void validCaseIfThatHasSpaceBeforeModelName() {
        final String code = "  User model\n    It has id in Int\n    It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void invalidCaseIfModelNameIsSmallLetter() {
        final String code = "user model\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldGiveLineNumberWhenModelNameIsSmallLetter() {
        final String code = "Car model\n  It has modelNum in Int\nuser model\n  It has id in Int\n  It prints name";
        final Validation modelDeclaration = new ModelDeclarationValidity(code);
        final int invalidLineNum = 3;
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.line(),
                    CoreMatchers.equalTo(invalidLineNum)
            );
        }
    }

    @Test
    public void shouldGiveReasonWhenModelNameIsSmallLetter() {
        final String code = "Car model\n  It has modelNum in Int\nuser model\n  It has id in Int\n  It prints name";
        final Validation modelDeclaration = new ModelDeclarationValidity(code);
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.reason(),
                    CoreMatchers.equalTo("Should be first character is capitalized with model keyword.")
            );
        }
    }

    @Test
    public void invalidCaseWhenNameIsMissing() {
        final String code = "model\n  It has id int int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldGiveLineNumberWhenNameIsMissing() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel\n  It has id in Int\n  It prints name";
        final ModelDeclarationValidity modelDeclaration = new ModelDeclarationValidity(code);
        final int errorLine = 4;
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.line(),
                    CoreMatchers.equalTo(errorLine)
            );
        }
    }

    @Test
    public void shouldGiveReasonWhenNameIsMissing() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel\n  It has id in Int\n  It prints name";
        final ModelDeclarationValidity modelDeclaration = new ModelDeclarationValidity(code);
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.reason(),
                    CoreMatchers.equalTo("The model name is required.")
            );
        }
    }

    @Test
    public void invalidCaseWhenExistBlankAfterModel() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel \n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void invalidCaseWhenExistBlankAndCharacterAfterModel() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel asdf\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldGiveLineNumberWhenExistBlankAndCharacterAfterModel() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel asdf\n  It has id in Int\n  It prints name";
        final ModelDeclarationValidity modelDeclaration = new ModelDeclarationValidity(code);
        final int errorLine = 4;
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.line(),
                    CoreMatchers.equalTo(errorLine)
            );
        }
    }

    @Test
    public void shouldGiveReasonWhenExistBlankAndCharacterAfterModel() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel asdf\n  It has id in Int\n  It prints name";
        final ModelDeclarationValidity modelDeclaration = new ModelDeclarationValidity(code);
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.reason(),
                    CoreMatchers.equalTo("No character allowed after the \"model\" keyword.")
            );
        }
    }

    @Test
    public void invalidCaseThatMoreThanOneSpaceBarBetweenNameAndModel() {
        final String code = "User  model\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldGiveLineNumberWhenMoreThanOneSpaceBarBetweenNameAndModel() {
        final String code = "User  model\n  It has id in Int\n  It prints name";
        final Validation modelDeclaration = new ModelDeclarationValidity(code);
        final int invalidLineNum = 1;
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.line(),
                    CoreMatchers.equalTo(invalidLineNum)
            );
        }
    }

    @Test
    public void shouldGiveReasonWhenMoreThanOneSpaceBarBetweenNameAndModel() {
        final String code = "User  model\n  It has id in Int\n  It prints name";
        final Validation modelDeclaration = new ModelDeclarationValidity(code);
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.reason(),
                    CoreMatchers.equalTo("There should be only one space between name and \"model\" keyword.")
            );
        }
    }

}

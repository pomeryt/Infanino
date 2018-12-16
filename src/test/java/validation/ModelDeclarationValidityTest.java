package validation;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.TooManyMethods")
final class ModelDeclarationValidityTest {
    private static final String THROW_MESSAGE = "It was valid when it shouldn't be.";

    @Test
    public void validModelDeclaration() {
        final String code = "User model\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void okayToHaveTabBeforeModelName() {
        final String code = "\tUser model\n\t\tIt has id in Int\n\t\tIt prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void okayToHaveSpaceBeforeModelName() {
        final String code = "  User model\n    It has id in Int\n    It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void firstLetterOfModelNameShouldBeCaptalized() {
        final String code = "user model\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldGiveLineNumberWhenFirstLetterOfModelNameIsLowerCased() {
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
    public void shouldGiveReasonWhenFirstLetterOfModelNameIsLowerCased() {
        final String code = "Car model\n  It has modelNum in Int\nuser model\n  It has id in Int\n  It prints name";
        final Validation modelDeclaration = new ModelDeclarationValidity(code);
        if (modelDeclaration.valid()) {
            throw new IllegalStateException(
                    THROW_MESSAGE
            );
        } else {
            MatcherAssert.assertThat(
                    modelDeclaration.reason(),
                    CoreMatchers.equalTo("The first letter of model name should be capitalized.")
            );
        }
    }

    @Test
    public void invalidWhenNameIsMissing() {
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
    public void invalidWhenBlankExistAfterModelKeyword() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel \n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void invalidWhenBlankOrCharacterExistAfterModel() {
        final String code = "Car model\n  It has modelNum in Int\n  It prints number\nmodel asdf\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldGiveLineNumberWhenBlankOrCharacterExistAfterModel() {
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
    public void shouldGiveReasonWhenBlankOrCharacterExistAfterModel() {
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
    public void invalidWhenMoreThanOneSpaceBetweenNameAndModel() {
        final String code = "User  model\n  It has id in Int\n  It prints name";
        MatcherAssert.assertThat(
                new ModelDeclarationValidity(code).valid(),
                CoreMatchers.equalTo(false)
        );
    }

    @Test
    public void shouldGiveLineNumberWhenMoreThanOneSpaceBetweenNameAndModel() {
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
    public void shouldGiveReasonWhenMoreThanOneSpaceBetweenNameAndModel() {
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

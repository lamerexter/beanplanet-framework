
# Validation

Library for performing validation on arbitrary objects.  

```
class AuthResponseValidator extends CompositeValidator<AuthnResponse> {
    public AuthResponseValidator() {
        super(
            stopOnFirstError: true,
            new SignatureValidator<>(),
            new IssuerValidator(AuthnResponse::getIssuer),
            new IssueInstantValidator(),
            new AssertionValidator(),
            ...
        );
    }
}
```

Capabilities provided to any project:

* Declarative specification of validations for a given object
* Java 8 lambda-function friendly
* All validators for a given object (e.g. AuthnRequest, AuthnResponse or AttributeQuery) are in one place.
* Ability to 'compose' validations, story-to-story, feature-to-feature as validations can and do change.
* Ability to conditionally carry out validations.
* Greater ability to create common/generic validators through the optional provision of _value provider_ lambda 'accessor' functions.
* Can return multiple validation errors, without the 'wack-a-mole' scenario of exception throwing
* Easy to read and maintain: validations follow the structure of the DOM of the given object
* TDD built-in.  All validators should be 100% unit testable.
* JSR-303 validation API future compliance (e.g. `public myEndpoint(@Valid AuthnResponse response) { ... }`)

Validator classes provided out-of-the-box:

* `interface Validator<T>` - the superinterface of all validators.
* `interface ConditionalValidator<T>` extends Validator<T> - conditional validation.  All out-of-the-box validators can be optionally configured with a conditions to 'turn on' or 'turn off' their validation logic.
* `class FixedErrorValidator<T>` - always adds an error if a given condition is true.
* `class NotEmptyValidator<T>` - adds an error if a given string is null or empty.
* `class PatternValidator<T>` - adds an error if a given string does not match a regular expression.
* `class RequiredValidator<T>` - adds an error if a given object is null.
* `class StringLengthValidator<T>` - adds an error if a given string length does not lie within a given range.

### Building the project

`mvn clean install`

### Testing the project

`mvn test`

## Licence

[MIT Licence](LICENCE)

This code is provided for informational purposes only and is not yet intended for use outside GOV.UK Verify

package org.beanplanet.validation;

import org.beanplanet.messages.domain.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.messages.domain.MessageImpl.globalMessage;

/**
 * A validator of email addresses. There are many competing versions of email validation - most using regular expressions to validate
 * the basic format, each with chatacter set(s) constrains and other limitations. This implementation takes a pragmatic approach, with
 * the solutions being through composition of one or more well-known, well-understood and well-documented solutions already in existence.
 *
 * <p>
 * The preferred method to create an email address validator is to use the builder:
 * <pre>
 * Validator&lt;String&gt; validator = EmailAddressValidator.builder()
 *                                                          .simpleFormat()  // Validates basic structure complies with pattern ^(.+)@(\S+)$
 *                                                          .rfc5322()       // Update of RFC 822 validates complies with pattern ^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$
 *                                                          .gmailPlusAddressing() // Gmail format, including the plus (+) addressing addition
 *                                                          .owasp()         // Validates complies with pattern ^[a-zA-Z0-9_+&*-] + (?:\\.[a-zA-Z0-9_+&*-] + )*@(?:[a-zA-Z0-9-]+\\.) + [a-zA-Z]{2, 7}
 *                                                          .format(String ...) // Variable list of explicit regular expressions used to match valid email addresses
 *                                                          .and()           / Apply 'and' conjunction of two or more methods (i.e. all MUST match), default is or()
 *                                                          .build()
 * </pre>
 * </p>
 *
 * @param <T>   The type of the context object containing the field being validated.
 */
public class EmailAddressValidator<T> extends CompositeValidator<T> {
    public static final String DEFAULT_MESSAGE_CODE = "email.address.format";
    public static final String DEFAULT_PARAM_MESSAGE = "Email address format is invalid";

    public static final String SIMPLE_FORMAT_PATTERN = "^(.+)@(\\S+)$";
    public static final String RFC5322_FORMAT_PATTERN = "^[a-zA-Z0-9!#$%&’'`*+/=?^_`{|}~.-]+@[a-zA-Z0-9.-]+$";
    public static final String GMAILPLUS_FORMAT_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
    public static final String OWASP_FORMAT_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    public EmailAddressValidator() {
        this(globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE));
    }

    public EmailAddressValidator(final Validator<T> ... validators) {
        super(validators);
    }

    public EmailAddressValidator(Message message) {
        this(null, message, (Function<T, ?>)null);
    }

    public EmailAddressValidator(Message message, String runtimeExpression) {
        this(null, message, runtimeExpression);
    }

    public <R> EmailAddressValidator(Message message, Function<T, R> valueProvider) {
        this(null, message, valueProvider);
    }

    public EmailAddressValidator(Predicate<T> condition, Message message) {
        this(condition, message, (Function<T, ?>)null);
    }

    public EmailAddressValidator(Predicate<T> condition, Message message, String runtimeExpression) {
        super(condition, true);
    }

    public <R> EmailAddressValidator(Predicate<T> condition, Message message, Function<T, R> valueProvider) {
        super(condition, true);
    }

    public static <E> EmailAddressValidatorPrologBuilder<E> builder() {
        return new EmailAddressValidatorPrologBuilder<>();
    }


    public static class EmailAddressValidatorPrologBuilder<E> {
        private Predicate<E> condition = null;
        private Function<?, E> valueSupplier = null;
        private String valueSupplierExpression = null;
        private Message message;
        private boolean conjunction;

        public EmailAddressValidatorPrologBuilder<E> condition(final Predicate<E> condition) {
            this.condition = condition;
            return this;
        }

        public EmailAddressValidatorPrologBuilder<E> condition(final boolean condition) {
            return condition(e -> condition);
        }

        public EmailAddressValidatorPrologBuilder<E> value(final Function<?, E> valueSupplier) {
            this.valueSupplier = valueSupplier;
            return this;
        }

        public EmailAddressValidatorPrologBuilder<E> value(final String valueSupplierExpression) {
            this.valueSupplierExpression = valueSupplierExpression;
            return this;
        }

        public EmailAddressValidatorPrologBuilder<E> message(final Message message) {
            this.message = message;
            return this;
        }

        public EmailAddressValidatorPrologBuilder<E> message(Consumer<Message.Builder> messageBuilderConsumer) {
            Message.Builder builder = Message.builder();
            messageBuilderConsumer.accept(builder);
            return message(builder.build());
        }

        public EmailAddressValidatorPrologBuilder<E> or() {
            this.conjunction = false; // Disjunction
            return this;
        }

        public EmailAddressValidatorPrologBuilder<E> and() {
            this.conjunction = true;
            return this;
        }

        public EmailAddressValidatorEpilogBuilder<E> simpleFormat() {
            return new EmailAddressValidatorEpilogBuilder<>(
                    condition,
                    valueSupplier,
                    valueSupplierExpression,
                    message,
                    conjunction
            ).simpleFormat();
        }

        public EmailAddressValidatorEpilogBuilder<E> rfc5322() {
            return new EmailAddressValidatorEpilogBuilder<>(
                    condition,
                    valueSupplier,
                    valueSupplierExpression,
                    message,
                    conjunction
            ).rfc5322();
        }

        public EmailAddressValidatorEpilogBuilder<E> gmailPlusAddressing() {
            return new EmailAddressValidatorEpilogBuilder<>(
                    condition,
                    valueSupplier,
                    valueSupplierExpression,
                    message,
                    conjunction
            ).gmailPlusAddressing();
        }

        public EmailAddressValidatorEpilogBuilder<E> owasp() {
            return new EmailAddressValidatorEpilogBuilder<>(
                    condition,
                    valueSupplier,
                    valueSupplierExpression,
                    message,
                    conjunction
            ).owasp();
        }

        public EmailAddressValidatorEpilogBuilder<E> format(String ... patterns) {
            return new EmailAddressValidatorEpilogBuilder<>(
                    condition,
                    valueSupplier,
                    valueSupplierExpression,
                    message,
                    conjunction
            ).format(patterns);
        }

        @SuppressWarnings("unchecked")
        public EmailAddressValidator<E> build() {
            return new EmailAddressValidatorEpilogBuilder<>(
                    condition,
                    valueSupplier,
                    valueSupplierExpression,
                    message,
                    conjunction
            ).build();
        }

    }

    public static class EmailAddressValidatorEpilogBuilder<E> {
        private Predicate<E> condition = null;
        private Function<?, E> valueSupplier = null;
        private String valueSupplierExpression = null;
        private Message message;
        private boolean conjunction;
        private List<Validator<E>> validators = new ArrayList<>();

        private EmailAddressValidatorEpilogBuilder(){}

        private EmailAddressValidatorEpilogBuilder(
                final Predicate<E> condition,
                final Function<?, E> valueSupplier,
                final String valueSupplierExpression,
                final Message message,
                final boolean conjunction
        ) {
            this.condition = condition;
            this.valueSupplier = valueSupplier;
            this.valueSupplierExpression = valueSupplierExpression;
            this.message = message;
            this.conjunction = conjunction;
        }

        public EmailAddressValidatorEpilogBuilder<E> simpleFormat() {
            return format(SIMPLE_FORMAT_PATTERN);
        }

        public EmailAddressValidatorEpilogBuilder<E> rfc5322() {
            return format(RFC5322_FORMAT_PATTERN);
        }

        public EmailAddressValidatorEpilogBuilder<E> gmailPlusAddressing() {
            return format(GMAILPLUS_FORMAT_PATTERN);
        }

        public EmailAddressValidatorEpilogBuilder<E> owasp() {
            return format(OWASP_FORMAT_PATTERN);
        }

        @SuppressWarnings("unchecked,rawtypes")
        public EmailAddressValidatorEpilogBuilder<E> format(String ... patterns) {
            if (patterns != null) {
                for (String pattern : patterns) {
                    this.validators.add(valueSupplierExpression != null ?
                            new PatternValidator<>(condition, getOrDefaultMessage(), valueSupplierExpression, pattern)
                            : new PatternValidator<E>(condition, getOrDefaultMessage(), (Function)valueSupplier, pattern));
                }
            }
            return this;
        }

        private Message getOrDefaultMessage() {
            return message != null ? message : Message.builder().code(DEFAULT_MESSAGE_CODE).parameterisedMessage(DEFAULT_PARAM_MESSAGE).build();
        }

        @SuppressWarnings("unchecked")
        public EmailAddressValidator<E> build() {
            if (validators.isEmpty()) {
                simpleFormat();
            }
            return new EmailAddressValidator<E>(validators.toArray(n -> (Validator<E>[])new Validator[n]));
        }
    }
}

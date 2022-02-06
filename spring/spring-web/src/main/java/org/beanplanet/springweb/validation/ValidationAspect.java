package org.beanplanet.springweb.validation;

import static java.util.Objects.nonNull;
import static java.util.stream.IntStream.range;
import static org.beanplanet.messages.domain.MessagesImpl.messages;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.validation.CompositeValidator;
import org.beanplanet.validation.Validated;
import org.beanplanet.validation.ValidationException;
import org.beanplanet.validation.Validator;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Aspect
@Component
public class ValidationAspect {
  private Map<AnnotatedElement, Validator> signatureValidators = new ConcurrentHashMap<>();

  private static final Validator NO_OP_VALIDATOR = (v, m) -> { return m; };

  private Function<ValidatedValidatorFactoryContext, Validator> ValidatedValidatorFactory = ctx -> {
    if ( ctx.getAnnotation().validatorClass().length > 0) {
      Object validator = TypeUtil.instantiateClass(ctx.getAnnotation().validatorClass()[0]);
      if ( !(validator instanceof Validator) ) {
        throw new IllegalArgumentException("The class "+ctx.getAnnotation().validatorClass()[0]+" declared on @Validated must implement "+Validator.class.getName());
      }
      return (Validator) validator;
    }

    if ( ctx.getValidatedType() != null && !TypeUtil.isPrimitiveType(ctx.getValidatedType()) ) {
      String possibleValidatorTypeFqName = ctx.getValidatedType().getName()+"Validator";
      Class<?> possibleValidatorType = TypeUtil.loadClass(possibleValidatorTypeFqName, Thread.currentThread().getContextClassLoader());
      if ( Validator.class.isAssignableFrom(possibleValidatorType) ) return (Validator)TypeUtil.instantiateClass(possibleValidatorType);
    }

    throw new IllegalArgumentException("Unable to determine validator type from an "+ctx.getAnnotation()+" "+ctx.getAnnotated());
  };

  public static class ValidatedValidatorFactoryContext {
    private final Validated annotation;
    private final Class<?> validatedType;
    private final AnnotatedElement annotated;

    public ValidatedValidatorFactoryContext(final Validated annotation, final Class<?> validatedType, final AnnotatedElement annotated) {
      this.annotation = annotation;
      this.validatedType = validatedType;
      this.annotated = annotated;
    }

    public Validated getAnnotation() {
      return annotation;
    }

    public Class<?> getValidatedType() {
      return validatedType;
    }

    public AnnotatedElement getAnnotated() {
      return annotated;
    }
  }

  @Before("execution(* com.mb.recon.partner.reporting.controller.*.*(..))")
  public void paramCheck(JoinPoint joinPoint) {
    signatureValidators.computeIfAbsent(((MethodSignature)joinPoint.getSignature()).getMethod(), buildValidator()).validate(joinPoint, messages());
  }

  private Function<? super AnnotatedElement, ? extends Validator> buildValidator() {
    return annotatedElement -> annotatedElement instanceof Method ? buildMethodValidator((Method)annotatedElement) : NO_OP_VALIDATOR;
  }

  private Validator buildMethodValidator(Method method) {
    return range(0, method.getParameters().length).filter(n -> nonNull(method.getParameters()[n].getAnnotation(Validated.class))).mapToObj(n -> buildMethodIndexedParameterValidator(method.getParameters()[n].getAnnotation(Validated.class), method.getParameters()[n], n)).reduce(NO_OP_VALIDATOR, (v1, v2) -> new CompositeValidator<>(v1, v2));
  }

  private Validator buildMethodIndexedParameterValidator(final Validated annotation, final Parameter parameter, int paramIndex) {
    return new CompositeValidator<JoinPoint>(jp -> jp.getArgs()[paramIndex], ValidatedValidatorFactory.apply(new ValidatedValidatorFactoryContext(annotation, parameter.getType(), parameter)), (v, m) -> { if ( m.hasErrors() ) { throw new ValidationException(m); } return m; });
  }
}

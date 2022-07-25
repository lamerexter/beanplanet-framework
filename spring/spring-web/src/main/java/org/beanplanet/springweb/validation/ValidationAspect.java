package org.beanplanet.springweb.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.validation.CompositeValidator;
import org.beanplanet.validation.Validated;
import org.beanplanet.validation.ValidationException;
import org.beanplanet.validation.Validator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.stream.IntStream.range;
import static org.beanplanet.messages.domain.MessagesImpl.messages;

@Aspect
@Component
public class ValidationAspect implements ApplicationContextAware {
  private ApplicationContext applicationContext;
  private Map<AnnotatedElement, Validator> signatureValidators = new ConcurrentHashMap<>();

  private static final Validator NO_OP_VALIDATOR = (v, m) -> { return m; };

  private Function<ValidatedValidatorFactoryContext, Validator> ValidatedValidatorFactory = ctx -> {
    if ( getValidatorTypes(ctx.getAnnotation()).length > 0) {
      final Class<?> validatorType = getValidatorTypes(ctx.getAnnotation())[0];
      Object validator = getBeanOrNull(validatorType);
      if ( validator == null ) {
        validator = TypeUtil.instantiateClass(getValidatorTypes(ctx.getAnnotation())[0]);
      }
      if ( !(validator instanceof Validator) ) {
        throw new IllegalArgumentException("The class "+getValidatorTypes(ctx.getAnnotation())[0]+" declared on @Validated must implement "+Validator.class.getName());
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

  private <T> T getBeanOrNull(Class<T> type) {
    try {
      return applicationContext.getBean(type);
    } catch (NoSuchBeanDefinitionException notFoundEx) {
      return null;
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

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

  @Before("(within(@(@org.springframework.stereotype.Controller *) *) || within(@(@org.springframework.stereotype.Service *) *)) && execution(* *(.., @org.beanplanet.validation.Validated (*), ..))")
  public void validateComponent(JoinPoint joinPoint) {
    signatureValidators.computeIfAbsent(((MethodSignature)joinPoint.getSignature()).getMethod(), buildValidator()).validate(joinPoint, messages());
  }

  private Class<?>[] getValidatorTypes(Validated validated) {
    return validated.value().length > 0 ? validated.value() : validated.validatorClass();
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

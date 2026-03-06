package com.matrimony.common.validator;



import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class AutoValidationAspect {

    private final Validator validator;

    @Around("@within(com.matrimony.common.validator.AutoValidate) || @annotation(com.matrimony.common.validator.AutoValidate)")
    public Object validateArguments(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Set<ConstraintViolation<Object>> allViolations = new LinkedHashSet<>();

        if (args != null) {
            for (Object arg : args) {
                if (arg == null) {
                    continue; // null check is handled by @NotNull on fields; but arg itself can be null
                }

                // Skip simple types (you can extend this list if needed)
                if (isSimpleType(arg.getClass())) {
                    continue;
                }

                Set<ConstraintViolation<Object>> violations = validator.validate(arg);
                if (!violations.isEmpty()) {
                    allViolations.addAll(violations);
                }
            }
        }

        if (!allViolations.isEmpty()) {
            throw new ConstraintViolationException(allViolations);
        }

        return joinPoint.proceed();
    }

    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive()
                || String.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type)
                || Boolean.class.isAssignableFrom(type)
                || Enum.class.isAssignableFrom(type)
                || type.getName().startsWith("java.time.")
                || type.getName().startsWith("java.util.UUID")
                || type.getName().startsWith("java.math.");
    }
}
package se.amsen.par.twootoot.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for building requests. Fields annotated as BodyParameters behave as:
 * 1. Should be sent in Request body
 *
 * @author params on 27/10/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BodyParameter {
}
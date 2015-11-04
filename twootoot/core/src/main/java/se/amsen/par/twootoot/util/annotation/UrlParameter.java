package se.amsen.par.twootoot.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for building requests. Fields annotaded as UrlParameters behave as:
 * 2. Should be appended to URL like http://some.url.com?annotatedField=fieldValue
 *
 * @author params on 27/10/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UrlParameter {
}
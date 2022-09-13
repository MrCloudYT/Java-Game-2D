package de.marcus.javagame.io.logging;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
    String displayName();
}
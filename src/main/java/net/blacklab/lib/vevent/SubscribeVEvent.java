package net.blacklab.lib.vevent;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("ALL")
@Retention(value = RUNTIME)
@Target(value = METHOD)
public @interface SubscribeVEvent {

}

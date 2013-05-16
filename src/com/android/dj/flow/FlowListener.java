package com.android.dj.flow;

import org.atteo.evo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IndexAnnotated
public @interface FlowListener {
	public Class value();
	public int maxInstances() default 0;
}

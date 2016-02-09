package com.visiontech.yummysmile.di.components;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author manuel.ortiz
 *         <p/>
 *         PerActivity Scope for dagger
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}

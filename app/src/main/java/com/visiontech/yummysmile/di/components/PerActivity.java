package com.visiontech.yummysmile.di.components;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author manuel.ortiz
 *
 * Dagger 2 will keep a single instance of any depedency class as long as its scope exists.
 * In this case the scope is being declared here, is the PerActivity.
 * So all dependencies declared using this scope will exist as a singleton, but only in the Activity scope.
 * After the activity doesn't exist in memory, al its dependencies will be released.
 *
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}

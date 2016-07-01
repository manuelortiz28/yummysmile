package com.visiontech.yummysmile.di.scopes;

/**
 * @author manuel.ortiz
 */

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author manuel.ortiz
 *
 * Dagger 2 will keep a single instance of any depedency class as long as its scope exists.
 * In this case the scope is being declared here, is the PerFragment.
 * So all dependencies declared using this scope will exist as a singleton, but only in the Activity scope.
 * After the activity doesn't exist in memory, al its dependencies will be released.
 *
 */
@Scope
@Retention(CLASS)
public @interface PerFragment {
}

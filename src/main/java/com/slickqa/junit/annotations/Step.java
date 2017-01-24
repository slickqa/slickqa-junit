package com.slickqa.junit.annotations;

/**
 * Created by Keith on 11/11/16.
 * Used to create steps for SlickMetaData
 */
public @interface Step {
    String step();
    String expectation() default "NA";
}

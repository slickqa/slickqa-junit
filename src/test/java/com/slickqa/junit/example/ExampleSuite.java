package com.slickqa.junit.example;

import com.slickqa.junit.SlickSuite;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Created by jason.corbett on 1/24/17.
 */
@RunWith(SlickSuite.class)
@SlickSuite.SuiteClasses({ExampleTest.class,
                          AnotherExampleTest.class})
public class ExampleSuite {
    @Before
    public void suiteSetup() {

    }
}

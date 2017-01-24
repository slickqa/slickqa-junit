package com.slickqa.junit.example;

import com.slickqa.junit.SlickResultRule;
import com.slickqa.junit.annotations.SlickMetaData;
import com.slickqa.junit.annotations.Step;
import org.junit.Rule;
import org.junit.Test;

/**
 * An example Test
 */
public class ExampleTest {

    @Rule
    public SlickResultRule slick = new SlickResultRule();

    @Test
    @SlickMetaData(title = "Example Test One",
                   component = "A very fine component",
                   steps = {
                       @Step(step = "first step", expectation = "first step worked"),
                       @Step(step = "second")
                   })
    public void exampleTestOne() {
    }

    @Test
    @SlickMetaData(title = "Example Test Two",
            component = "Another very fine component",
            feature = "A feature",
            steps = {
                    @Step(step = "first step", expectation = "first step worked"),
                    @Step(step = "second")
            })
    public void exampleTestTwo() {
        assert false;
    }
}

package com.slickqa.junit.example;

import com.slickqa.junit.SlickResultRule;
import com.slickqa.junit.annotations.SlickMetaData;
import com.slickqa.junit.annotations.Step;
import org.junit.Rule;
import org.junit.Test;

/**
 * An example Test
 */
public class AnotherExampleTest {

    @Rule
    public SlickResultRule slick = new SlickResultRule();

    @Test
    @SlickMetaData(title = "Example Test Three",
                   component = "A very fine component",
                   feature = "A feature for a very fine component",
                   steps = {
                       @Step(step = "first step", expectation = "first step worked"),
                       @Step(step = "second", expectation = "second step worked")
                   })
    public void exampleTestThree() {
    }

    @Test
    public void notReportedTest() {
    }

    @Test
    @SlickMetaData(title = "Example Test Four",
            component = "Another very fine component",
            feature = "A feature",
            steps = {
                    @Step(step = "first step that is different", expectation = "first step worked"),
                    @Step(step = "second step", expectation = "second step worked")
            })
    public void exampleTestFour() throws Exception {
        //Thread.sleep(2 * 60 * 1000); // 2 minutes
    }

    @Test
    @SlickMetaData(title = "Example Logging Test",
            component = "Another very fine component",
            feature = "A feature",
            steps = {
                    @Step(step = "first step that isn't different", expectation = "first step worked"),
                    @Step(step = "second step", expectation = "second step worked"),
                    @Step(step = "weird, a third step", expectation = "that it is all over")
            })
    public void exampleLoggingTest() throws Exception {
        slick.log().debug("Hello");
        slick.log().info("This is an info {0}", "message.");
        for(int i = 0; i < 10; i++) {
            slick.log().warn("This message is {0} of {1} {2}.", i + 1, 10, "messages");
        }
        //Thread.sleep(30 * 1000);
    }
}

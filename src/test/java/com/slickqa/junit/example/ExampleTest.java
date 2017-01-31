package com.slickqa.junit.example;

import com.slickqa.junit.SlickResultRule;
import com.slickqa.junit.annotations.SlickMetaData;
import com.slickqa.junit.annotations.Step;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    public void exampleTestOne() throws Exception {
        //Thread.sleep(60 * 1000);
    }

    @Test
    @SlickMetaData(title = "Example Test Two",
            component = "Another very fine component",
            feature = "A feature",
            steps = {
                    @Step(step = "first step", expectation = "first step worked"),
                    @Step(step = "second")
            })
    public void exampleTestTwo() throws Exception {
        //throw new AssertionError("This test is supposed to fail so you can see it!");
        Path file = Paths.get(ExampleTest.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                         .resolve(Paths.get("com", "slickqa", "junit", "example", "screenshot.png"));
        slick.addFile(file);
    }
}

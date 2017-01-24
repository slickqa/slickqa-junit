package com.slickqa.junit;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * Created by jason.corbett on 1/23/17.
 */
public class SlickSuite extends Suite {
    public final SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();

    public SlickSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
        init();
    }

    protected void init() {
        controller.createSuiteResults(this.getDescription().getChildren());
    }
}

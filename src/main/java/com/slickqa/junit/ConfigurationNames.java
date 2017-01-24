package com.slickqa.junit;

/**
 * These are names of configurations that should exist in whatever configuration source you have.
 */
public class ConfigurationNames {
    /**
     * The base url of slick.  This should be the url that you go to with the browser.  An example
     * might be http://demo.slickqa.com/ or http://demo.slickqa.com/slick if you have it deployed under the
     * /slick base url.
     */
    static public final String BASE_URL = "slick.baseurl";

    /**
     * The name of the project to file results and tests under.  It will be created if it does not exist.
     */
    static public final String PROJECT_NAME = "slick.project";

    /**
     * The name of the release to use for the testrun and results, it will be created if necessary.
     */
    static public final String RELEASE_NAME = "slick.release";

    /**
     * The name of the build to use for the testrun and results, it will be created if necessary.
     */
    static public final String BUILD_NAME = "slick.build";

    /**
     * The name of the testplan (if any) to use for the testrun, it will be created if necessary.
     */
    static public final String TESTPLAN_NAME = "slick.testplan";

    /**
     * The name of the testrun.  If missing and there is a testplan, the name of the testplan will be used.
     * If both are missing then a combination of the date and time will be used.
     */
    static public final String TESTRUN_NAME = "slick.testrun";
}

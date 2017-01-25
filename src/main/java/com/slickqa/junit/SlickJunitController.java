package com.slickqa.junit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.slickqa.client.SlickClient;
import com.slickqa.client.SlickClientFactory;
import com.slickqa.client.errors.SlickError;
import com.slickqa.client.impl.JsonUtil;
import com.slickqa.client.model.*;
import com.slickqa.junit.annotations.SlickMetaData;
import org.junit.runner.Description;

import java.util.*;

/**
 * Common class used by both the Rule (tests) and the Suite.  This class will initialize the slick client, create
 * the testrun in slick, and will hold a mapping of junit tests to slick results.  The test writer will not likely
 * have to interact with this class unless they want to customize the process by extending.
 */
public class SlickJunitController {
    protected boolean usingSlick;
    protected SlickConfigurationSource configurationSource;
    protected SlickClient slickClient;
    protected Project project;
    protected Testrun testrun;

    protected Map<String, Result> results;

    protected SlickJunitController() {
        usingSlick = false;
        configurationSource = initializeConfigurationSource();
        results = new HashMap<>();
        initializeController();
    }

    /**
     * This method exists in case you need to override where slick get's it's configuration data from.  By default
     * it will get it from system properties.
     *
     * @return an instance of
     */
    protected SlickConfigurationSource initializeConfigurationSource() {
        return new SystemPropertyConfigurationSource();
    }

    protected void initializeController() {
        String baseurl = configurationSource.getConfigurationEntry(ConfigurationNames.BASE_URL, null);
        String projectName = configurationSource.getConfigurationEntry(ConfigurationNames.PROJECT_NAME, null);
        if(baseurl != null && projectName != null) {
            try {

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
                mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                JsonUtil.mapper = mapper;

                if(!baseurl.endsWith("api") && !baseurl.endsWith("api/")) {
                    String add = "api/";
                    if(baseurl.endsWith("/")) {
                        baseurl = baseurl + add;
                    } else {
                        baseurl = baseurl + "/" + add;
                    }
                }
                slickClient = SlickClientFactory.getSlickClient(baseurl);
                ProjectReference projectReference = new ProjectReference();
                ReleaseReference releaseReference = null;
                BuildReference buildReference = null;
                String testplanId = null;
                String testrunName = null;
                try {
                    project = slickClient.project(projectName).get();
                } catch (SlickError e) {
                    project = new Project();
                    project.setName(projectName);
                    project = slickClient.projects().create(project);
                }
                projectReference.setName(project.getName());
                projectReference.setId(project.getId());

                String releaseName = configurationSource.getConfigurationEntry(ConfigurationNames.RELEASE_NAME, null);
                if(releaseName != null) {
                    releaseReference = new ReleaseReference();
                    releaseReference.setName(releaseName);
                }
                String buildName = configurationSource.getConfigurationEntry(ConfigurationNames.BUILD_NAME, null);
                if(buildName != null) {
                    buildReference = new BuildReference();
                    buildReference.setName(buildName);
                }

                testrunName = configurationSource.getConfigurationEntry(ConfigurationNames.TESTRUN_NAME, null);

                String testplanName = configurationSource.getConfigurationEntry(ConfigurationNames.TESTPLAN_NAME, null);
                if(testplanName != null) {
                    HashMap<String, String> query = new HashMap<>();
                    query.put("project.id", project.getId());
                    query.put("name", testplanName);
                    TestPlan tplan = null;
                    try {
                        List<TestPlan> tplans = slickClient.testplans(query).getList();
                        if(tplans != null && tplans.size() > 0) {
                            tplan = tplans.get(0);
                        }
                    } catch (SlickError e) {
                        // don't care
                    }
                    if(tplan == null) {
                        tplan = new TestPlan();
                        tplan.setName(testplanName);
                        tplan.setProject(projectReference);
                        tplan = slickClient.testplans().create(tplan);
                    }
                    testplanId = tplan.getId();
                    if(testrunName == null) {
                        testrunName = tplan.getName();
                    }
                }

                testrun = new Testrun();
                testrun.setName(testrunName);
                testrun.setTestplanId(testplanId);
                testrun.setProject(projectReference);
                testrun.setRelease(releaseReference);
                testrun.setBuild(buildReference);
                testrun = slickClient.testruns().create(testrun);

                usingSlick = true;
            } catch (SlickError e) {
                e.printStackTrace();
                System.err.println("!!!!!! Error occurred when initializing slick, no slick report will happen !!!!!!");
            }
        }
    }

    public boolean isUsingSlick() {
        return usingSlick;
    }

    public Testrun getTestrun() {
        return testrun;
    }

    public SlickClient getSlickClient() {
        if(usingSlick) {
            return slickClient;
        } else {
            return null;
        }
    }

    public String getAutomationId(Description testDescription) {
        String automationId = null;
        try {
            SlickMetaData metaData = testDescription.getAnnotation(SlickMetaData.class);
            if(metaData != null && metaData.automationId() != null && !"".equals(metaData.automationId())) {
                automationId = metaData.automationId();
            }
        } catch (RuntimeException e) {
            // ignore
        }
        if(automationId == null) {
            automationId = testDescription.getClassName() + ":" + testDescription.getMethodName();
        }

        return automationId;
    }

    public void addResultFor(Description testDescription) throws SlickError {
        if(isUsingSlick() && testDescription.isTest()) {
            SlickMetaData metaData = testDescription.getAnnotation(SlickMetaData.class);
            if(metaData != null) {
                String automationId = getAutomationId(testDescription);
                Testcase testcase = null;

                HashMap<String, String> query = new HashMap<>();
                query.put("project.id", project.getId());
                query.put("automationId", automationId);
                ProjectReference projectReference = new ProjectReference();
                projectReference.setName(project.getName());
                projectReference.setId(project.getId());

                try {
                    List<Testcase> testcases = slickClient.testcases(query).getList();
                    if(testcases != null && testcases.size() > 0) {
                        testcase = testcases.get(0);
                    }
                } catch (SlickError e) {
                    // ignore
                }

                if(testcase == null) {
                    testcase = new Testcase();
                    testcase.setName(metaData.title());
                    testcase.setProject(projectReference);
                    testcase = slickClient.testcases().create(testcase);
                }

                testcase.setName(metaData.title());
                testcase.setAutomated(true);
                testcase.setAutomationId(automationId);
                testcase.setAutomationKey(getValueOrNullIfEmpty(metaData.automationKey()));
                testcase.setAutomationTool("junit");
                ComponentReference componentReference = null;
                Component component = null;
                if(metaData.component() != null && !"".equals(metaData.component())) {
                    componentReference = new ComponentReference();
                    componentReference.setName(metaData.component());
                    if(project.getComponents() != null) {
                        for (Component possible : project.getComponents()) {
                            if (metaData.component().equals(possible.getName())) {
                                componentReference.setId(possible.getId());
                                componentReference.setCode(possible.getCode());
                                component = possible;
                                break;
                            }
                        }
                    }
                    if(componentReference.getId() == null) {
                        component = new Component();
                        component.setName(metaData.component());
                        try {
                            component = slickClient.project(project.getId()).components().create(component);
                            componentReference.setId(component.getId());
                            project = slickClient.project(project.getId()).get();
                        } catch (SlickError e) {
                            component = null;
                            componentReference = null;
                        }
                    }
                }
                testcase.setComponent(componentReference);
                FeatureReference featureReference = null;
                if(metaData.feature() != null && !"".equals(metaData.feature()) && component != null) {
                    featureReference = new FeatureReference();
                    featureReference.setName(metaData.feature());
                    Feature feature = null;
                    if(component.getFeatures() != null) {
                        for(Feature possible: component.getFeatures()) {
                            if(metaData.feature().equals(possible.getName())) {
                                featureReference.setId(possible.getId());
                                feature = possible;
                                break;
                            }
                        }
                    }
                    if(feature == null) {
                        feature = new Feature();
                        feature.setName(metaData.feature());
                        if(component.getFeatures() == null) {
                            component.setFeatures(new ArrayList<Feature>(1));
                        }
                        component.getFeatures().add(feature);
                        try {
                            component = slickClient.project(project.getId()).component(component.getId()).update(component);
                            project = slickClient.project(project.getId()).get();
                            if(component.getFeatures() != null) {
                                for(Feature possible: component.getFeatures()) {
                                    if(metaData.feature().equals(possible.getName())) {
                                        featureReference.setId(feature.getId());
                                    }
                                }

                            } else {
                                // this shouldn't be possible which probably means it'll happen
                                feature = null;
                                featureReference = null;
                            }
                        } catch (SlickError e) {
                            feature = null;
                            featureReference = null;
                        }
                    }
                }
                testcase.setFeature(featureReference);
                if(metaData.steps() != null && metaData.steps().length > 0) {
                    testcase.setSteps(new ArrayList<Step>(metaData.steps().length));
                    for(com.slickqa.junit.annotations.Step metaStep : metaData.steps()) {
                        Step slickStep = new Step();
                        slickStep.setName(metaStep.step());
                        slickStep.setExpectedResult(metaStep.expectation());
                        testcase.getSteps().add(slickStep);
                    }
                }
                testcase = slickClient.testcase(testcase.getId()).update(testcase);
                TestcaseReference testReference = new TestcaseReference();
                testReference.setName(testcase.getName());
                testReference.setAutomationId(testcase.getAutomationId());
                testReference.setAutomationKey(testcase.getAutomationKey());
                testReference.setTestcaseId(testcase.getId());
                testReference.setAutomationTool(testcase.getAutomationTool());

                TestrunReference testrunReference = new TestrunReference();
                testrunReference.setName(testrun.getName());
                testrunReference.setTestrunId(testrun.getId());

                Result result = new Result();
                result.setProject(projectReference);
                result.setTestrun(testrunReference);
                result.setTestcase(testReference);
                result.setStatus("NO_RESULT");
                result.setReason("not run yet...");
                result.setRecorded(new Date());
                result = slickClient.results().create(result);
                results.put(automationId, result);
            }
        }
    }

    public static String getValueOrNullIfEmpty(String value) {
        if(value == null || "".equals(value)) {
            return null;
        } else {
            return value;
        }
    }

    public Result getResultFor(Description testDescription) {
        String automationId = getAutomationId(testDescription);
        if(results.containsKey(automationId)) {
            return results.get(automationId);
        } else {
            return null;
        }
    }

    public Result getOrCreateResultFor(Description testDescription) {
        if(isUsingSlick()) {
            Result result = getResultFor(testDescription);
            if(result == null) {
                try {
                    addResultFor(testDescription);
                    return getResultFor(testDescription);
                } catch (SlickError e) {
                    e.printStackTrace();
                    System.err.println("!!!! ERROR creating slick result for " + testDescription.getDisplayName() + " !!!!");
                    return null;
                }
            } else {
                return result;
            }
        } else {
            return null;
        }
    }


    public void createSuiteResults(ArrayList<Description> children) {
        if(isUsingSlick()) {
            for (Description child : children) {
                if (child.isTest()) {
                    try {
                        addResultFor(child);
                    } catch (SlickError e) {
                        e.printStackTrace();
                        System.err.println("!!!! ERROR creating slick result for " + child.getDisplayName() + " !!!!");
                    }
                } else {
                    if (child.getChildren() != null) {
                        createSuiteResults(child.getChildren());
                    }
                }
            }
        }
    }
}

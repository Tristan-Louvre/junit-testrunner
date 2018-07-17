package io.louvre.junit.testrunner.extension;

import com.eviware.soapui.model.TestModelItem;
import com.eviware.soapui.model.testsuite.*;
import com.eviware.soapui.report.JUnitReport;
import com.eviware.soapui.report.JUnitSecurityReportCollector;
import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.xml.XmlUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import org.apache.log4j.Logger;

public class ReadyApiExtendedJUnitReportCollector extends JUnitSecurityReportCollector implements TestRunListener, TestSuiteRunListener, ProjectRunListener {
    private HashMap<String, JUnitReport> reports;
    private HashMap<TestCase, String> failures;
    private HashMap<TestCase, Integer> errorCount;

    private boolean includeTestPropertiesInReport = false;
    private int maxErrors = 0;

    private static final Logger log = Logger.getLogger(ReadyApiExtendedJUnitReportCollector.class);

    public ReadyApiExtendedJUnitReportCollector() {
        this(0);
    }

    private ReadyApiExtendedJUnitReportCollector(int maxErrors) {
        this.maxErrors = maxErrors;
        reports = new HashMap<>();
        errorCount = new HashMap<>();
        failures = new HashMap<>();
    }

    @Override
    public void setIncludeTestPropertiesInReport(boolean includeTestPropertiesInReport) {
        this.includeTestPropertiesInReport = includeTestPropertiesInReport;
    }

    @Override
    protected HashMap<String, String> getTestPropertiesAsHashMap(TestModelItem testCase) {
        HashMap<String, String> testProperties = new HashMap<>();
        for (Map.Entry<String, TestProperty> stringTestPropertyEntry : testCase.getProperties().entrySet()) {
            testProperties.put(stringTestPropertyEntry.getKey(), stringTestPropertyEntry.getValue().getValue());
        }
        return testProperties;
    }

    @Override
    public String getReport() {
        Set<String> keys = reports.keySet();
        if (keys.size() > 0) {
            String key = (String) keys.toArray()[0];
            return reports.get(key).toString();
        }
        return "No reports..:";
    }

    @Override
    public HashMap<String, JUnitReport> getReports() {
        return reports;
    }

    @Override
    public void saveReport(JUnitReport report, String filename) throws Exception {
        report.save(new File(filename));
    }

    @Override
    public List<String> saveReports(String path) throws Exception {

        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        List<String> result = new ArrayList<String>();

        for (String name : reports.keySet()) {
            JUnitReport report = reports.get(name);
            String fileName = path + File.separatorChar + "TEST-" + StringUtils.createFileName(name, '_') + ".xml";
            saveReport(report, fileName);
            result.add(fileName);
        }

        return result;
    }

    @Override
    public void reset() {
        reports.clear();
        failures.clear();
        errorCount.clear();
    }

    @Override
    public void beforeRun(ProjectRunner testScenarioRunner, ProjectRunContext runContext) {
    }

    @Override
    public void afterRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
    }

    @Override
    public void beforeRun(TestSuiteRunner testRunner, TestSuiteRunContext runContext) {
    }

    @Override
    public void afterRun(TestSuiteRunner testRunner, TestSuiteRunContext runContext) {
    }

    @Override
    public void beforeRun(TestCaseRunner testRunner, TestCaseRunContext runContext) {
        TestCase testCase = testRunner.getTestCase();
        TestSuite testSuite = testCase.getTestSuite();
        if (!reports.containsKey(testSuite.getName())) {
            JUnitReport report = new JUnitReport();
            report.setIncludeTestProperties(this.includeTestPropertiesInReport);
            report.setTestSuiteName(testSuite.getProject().getName() + " - " + testSuite.getName());
            reports.put(testSuite.getName(), report);
        }
    }

    @Override
    public void afterRun(TestCaseRunner testRunner, TestCaseRunContext runContext) {
        TestCase testCase = testRunner.getTestCase();
        JUnitReport report = reports.get(testCase.getTestSuite().getName());

        HashMap<String, String> testProperties = getTestPropertiesAsHashMap(testCase);

        if (TestRunner.Status.INITIALIZED != testRunner.getStatus() && TestRunner.Status.RUNNING != testRunner.getStatus()) {
            if (TestRunner.Status.CANCELED == testRunner.getStatus()) {
                report.addTestCase(testCase.getName(), testRunner.getTimeTaken(), testProperties);
            }
            if (TestRunner.Status.FAILED == testRunner.getStatus()) {
                String msg = "";
                if (failures.containsKey(testCase)) {
                    msg = failures.get(testCase);
                }
                report.addTestCaseWithFailure(testCase.getName(), testRunner.getTimeTaken(), testRunner.getReason(), msg, testProperties);
            }
            if (TestRunner.Status.FINISHED == testRunner.getStatus()) {
                report.addTestCase(testCase.getName(), testRunner.getTimeTaken(), testProperties);
            }
        }
    }

    @Override
    public void beforeTestSuite(ProjectRunner projectRunner, ProjectRunContext runContext, TestSuite testSuite) {
        testSuite.addTestSuiteRunListener(this);
    }

    @Override
    public void afterTestSuite(ProjectRunner projectRunner, ProjectRunContext runContext, TestSuiteRunner testRunner) {
        testRunner.getTestSuite().removeTestSuiteRunListener(this);
    }

    @Override
    public void beforeTestCase(TestSuiteRunner testRunner, TestSuiteRunContext runContext, TestCase testCase) {
        testCase.addTestRunListener(this);
    }

    @Override
    public void afterTestCase(TestSuiteRunner testRunner, TestSuiteRunContext runContext, TestCaseRunner testCaseRunner) {
        testCaseRunner.getTestCase().removeTestRunListener(this);
    }

    @Override
    public void beforeStep(TestCaseRunner testRunner, TestCaseRunContext runContext, TestStep testStep) {
    }

    @Override
    public void beforeStep(TestCaseRunner testRunner, TestCaseRunContext runContext) {
    }

    @Override
    public void afterStep(TestCaseRunner testRunner, TestCaseRunContext runContext, TestStepResult result) {
        TestStep currentStep = result.getTestStep();
        TestCase testCase = currentStep.getTestCase();
        TestStep dataSource = testRunner.getTestCase().getTestStepAt(0);


        if (result.getStatus() == TestStepResult.TestStepStatus.FAILED) {
            if (maxErrors > 0) {
                Integer errors = errorCount.get(testCase);
                if (errors == null) {
                    errors = 0;
                }

                if (errors >= maxErrors) {
                    return;
                }

                errorCount.put(testCase, errors + 1);
            }

            StringBuilder buf = new StringBuilder();
            if (failures.containsKey(testCase)) {
                buf.append(failures.get(testCase));
            }

            buf.append("<h3><b>").append(XmlUtils.entitize(result.getTestStep().getName()))
                    .append(" Failed</b></h3><pre>");
            for (String message : result.getMessages()) {
                if (message.toLowerCase().startsWith("url:")) {
                    String url = XmlUtils.entitize(message.substring(4).trim());
                    buf.append("URL: <a target=\"new\" href=\"").append(url).append("\">").append(url)
                            .append("</a>");
                } else {
                    buf.append(message);
                }

                buf.append("\r\n");
            }

            // use string value since constant is defined in pro.. duh..
            if (testRunner.getTestCase().getSettings().getBoolean("Complete Error Logs")) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);
                result.writeTo(writer);

                buf.append(XmlUtils.entitize(stringWriter.toString()));
            }

            buf.append("</pre><hr/>");

            int bufferIndex = 0;
            String propertyHeader = "----------------- Properties ------------------------------";
            int indexToInsert = buf.lastIndexOf(propertyHeader) + propertyHeader.length()+1;
            int propertyCount = dataSource.getPropertyCount();

            for (int i = 0; i < propertyCount; i++) {
                String propertyKey = dataSource.getPropertyAt(i).getName();
                String propertyValue = runContext.expand(dataSource.getPropertyAt(i).getValue());
                String propertyConcat = propertyKey + ": " + propertyValue + "\r\n";
                buf.insert(indexToInsert, propertyConcat);
                indexToInsert += propertyConcat.length();
            }

            failures.put(testCase, buf.toString());
        }
    }
}

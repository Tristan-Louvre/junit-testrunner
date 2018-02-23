package io.louvre.junit.testrunner.extension;

import com.eviware.soapui.model.testsuite.*;
import com.eviware.soapui.report.JUnitReport;
import com.eviware.soapui.report.JUnitSecurityReportCollector;
import com.eviware.soapui.support.xml.XmlUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

public class ReadyApiExtendedJUnitReportCollector extends JUnitSecurityReportCollector {
    private HashMap<TestCase, String> failures;
    private HashMap<TestCase, Integer> errorCount;
    private int maxErrors = 0;

    public ReadyApiExtendedJUnitReportCollector(int maxErrors) {
        super();
        this.maxErrors = maxErrors;
        errorCount = new HashMap<TestCase, Integer>();
        failures = new HashMap<TestCase, String>();
    }

    @Override
    public void beforeRun(TestCaseRunner testRunner, TestCaseRunContext runContext) {
        HashMap<String, JUnitReport> reports;

        TestCase testCase = testRunner.getTestCase();
        TestSuite testSuite = testCase.getTestSuite();
        reports = this.getReports();


        if (!reports.containsKey(testSuite.getName())) {
            JUnitReport report = new ReadyApiExtendedJUnitReport();
            report.setIncludeTestProperties(this.includeTestPropertiesInReport);
            report.setTestSuiteName(testSuite.getProject().getName() + " - " + testSuite.getName());
            reports.put(testSuite.getName(), report);
        }
    }

    @Override
    public void afterStep(TestCaseRunner testRunner, TestCaseRunContext runContext, TestStepResult result) {
        TestStep currentStep = result.getTestStep();
        TestCase testCase = currentStep.getTestCase();

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

            StringBuffer buf = new StringBuffer();
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

            buf.append("</pre><hr/>More");

            failures.put(testCase, buf.toString());
        }
    }
}

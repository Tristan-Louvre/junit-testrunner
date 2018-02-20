package io.louvre.junit.testrunner.extension;

import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.report.JUnitReport;
import com.eviware.soapui.report.JUnitSecurityReportCollector;

import java.util.HashMap;

public class ReadyApiExtendedJUnitReportCollector extends JUnitSecurityReportCollector {

    public ReadyApiExtendedJUnitReportCollector() {
        super();
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
}

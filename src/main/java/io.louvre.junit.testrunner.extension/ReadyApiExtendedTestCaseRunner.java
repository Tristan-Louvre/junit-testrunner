package io.louvre.junit.testrunner.extension;

import com.eviware.soapui.analytics.Analytics;
import com.eviware.soapui.analytics.ReadyApiActions;
import com.eviware.soapui.report.JUnitSecurityReportCollector;
import com.smartbear.ready.cmd.RunnerProductInfo;
import com.smartbear.ready.cmd.runner.pro.SoapUIProTestCaseRunner;


public class ReadyApiExtendedTestCaseRunner extends SoapUIProTestCaseRunner {

    public ReadyApiExtendedTestCaseRunner() {
        super();
    }

    public static void main (String [] args) throws Exception {
        RunnerProductInfo.setRunnerName("TestCaseRunner");
        Analytics.trackAction(ReadyApiActions.START_COMMAND_LINE_RUNNER, "Type", "SoapUIProTestCaseRunner");
        System.exit((new ReadyApiExtendedTestCaseRunner()).init(args));
    }

    @Override
    protected JUnitSecurityReportCollector createJUnitSecurityReportCollector() {
        return new ReadyApiExtendedJUnitReportCollector();
    }
}

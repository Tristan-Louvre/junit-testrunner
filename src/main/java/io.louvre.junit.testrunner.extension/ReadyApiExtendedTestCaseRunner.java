package io.louvre.junit.testrunner.extension;

import com.eviware.soapui.analytics.Analytics;
import com.eviware.soapui.analytics.ReadyApiActions;
import com.smartbear.ready.cmd.RunnerProductInfo;
import com.smartbear.ready.cmd.runner.pro.SoapUIProTestCaseRunner;

public class ReadyApiExtendedTestCaseRunner extends SoapUIProTestCaseRunner {
    public static void main (String [] var0) throws Exception {
        RunnerProductInfo.setRunnerName("TestCaseRunner");
        Analytics.trackAction(ReadyApiActions.START_COMMAND_LINE_RUNNER, new String[]{"Type", "SoapUIProTestCaseRunner"});
        ReadyApiExtendedTestCaseRunner testCaseRunner = new ReadyApiExtendedTestCaseRunner();
        int result = testCaseRunner.init(var0);
        System.exit(result);
    }

    public ReadyApiExtendedTestCaseRunner() {
        super();
    }

    @Override
    protected SoapUIOptions initCommandLineOptions() {
        //SoapUIOptions var1 = super.initCommandLineOptions();
        //var1.addOption("Z", false, "ITS A FUCKEN Z BRAH");
        //return var1;
        SoapUIOptions options = new SoapUIOptions("testrunner");
        options.addOption("c", true, "Sets the testcase");
        options.addOption("E", true, "Sets the environment");
        options.addOption("f", true, "Sets the output folder to export results to");
        options.addOption("I", false, "Do not stop if error occurs, ignore them");
        options.addOption("j", false, "Sets the output to include JUnit XML reports");
        options.addOption("J", false, "Sets the output to include JUnit XML reports adding test properties to the report");
        options.addOption("l", true, "Installs an activated license file");
        options.addOption("r", false, "Prints a small summary report");
        options.addOption("s", true, "Sets the testsuite");
        return options;
    }
}

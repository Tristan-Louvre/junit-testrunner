package io.louvre.junit.testrunner.extension;

import com.eviware.soapui.analytics.Analytics;
import com.eviware.soapui.analytics.ReadyApiActions;
import com.smartbear.ready.cmd.RunnerProductInfo;
import com.smartbear.ready.cmd.runner.pro.SoapUIProTestCaseRunner;
import com.smartbear.ready.core.ReadyApiCoreModule;


public class ReadyApiExtendedTestCaseRunner extends SoapUIProTestCaseRunner {

    private ReadyApiExtendedTestCaseRunner() {
        super();
    }

    public static void main (String [] var0) throws Exception {
        RunnerProductInfo.setRunnerName("TestCaseRunner");
        Analytics.trackAction(ReadyApiActions.START_COMMAND_LINE_RUNNER, "Type", "SoapUIProTestCaseRunner");
        System.exit((new ReadyApiExtendedTestCaseRunner()).init(var0));
    }

    @Override
    protected SoapUIOptions initCommandLineOptions() {
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

/*    @Override
    public void printReport(long var1) {
        this.outputHandler.outputLine("");
        this.outputHandler.outputLine(ReadyApiCoreModule.READY_API_NAME + " " + ReadyApiCoreModule.READY_API_VERSION + " TestCaseRunner Summary");
        this.outputHandler.outputLine("-----------------------------");
        this.outputHandler.outputLine("Time Taken: " + var1 + "ms");
        this.outputHandler.outputLine("Total TestSuites: " + this.i);
        this.outputHandler.outputLine("Total TestCases: " + this.j + " (" + this.h.size() + " failed)");
        this.outputHandler.outputLine("Total TestSteps: " + this.k);
        this.outputHandler.outputLine("Total Request Assertions: " + this.l);
        this.outputHandler.outputLine("Total Failed Assertions: " + this.e.size());
        this.outputHandler.outputLine("Total Exported Results: " + this.s);
        this.outputHandler.outputLine("--------------THIS WAS ADDED BY ME---------------");
    }*/
}

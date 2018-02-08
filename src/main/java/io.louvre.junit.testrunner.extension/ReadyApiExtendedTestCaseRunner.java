package io.louvre.junit.testrunner.extension;

import com.smartbear.ready.cmd.runner.pro.SoapUIProTestCaseRunner;

public class ReadyApiExtendedTestCaseRunner extends SoapUIProTestCaseRunner {
    public ReadyApiExtendedTestCaseRunner() {
        super();
    }

    @Override
    protected SoapUIOptions initCommandLineOptions() {
        SoapUIOptions var1 = new SoapUIOptions("testrunner");
        var1.addOption("c", true, "Sets the testcase");
        var1.addOption("E", true, "Sets the environment");
        var1.addOption("f", true, "Sets the output folder to export results to");
        var1.addOption("I", false, "Do not stop if error occurs, ignore them");
        var1.addOption("j", false, "Sets the output to include JUnit XML reports");
        var1.addOption("J", false, "Sets the output to include JUnit XML reports adding test properties to the report");
        var1.addOption("l", true, "Installs an activated license file");
        var1.addOption("r", false, "Prints a small summary report");
        var1.addOption("s", true, "Sets the testsuite");
        return var1;
    }
}

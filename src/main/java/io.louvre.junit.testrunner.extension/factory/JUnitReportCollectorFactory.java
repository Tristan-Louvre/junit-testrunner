package io.louvre.junit.testrunner.extension.factory;

import com.eviware.soapui.report.JUnitSecurityReportCollector;
import io.louvre.junit.testrunner.extension.ReadyApiExtendedJUnitReportCollector;

public class JUnitReportCollectorFactory {

    public static JUnitSecurityReportCollector newJUnitReportCollector() {
        return new ReadyApiExtendedJUnitReportCollector();
    }
}

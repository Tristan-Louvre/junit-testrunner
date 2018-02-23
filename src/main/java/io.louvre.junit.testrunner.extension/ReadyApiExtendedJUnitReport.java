package io.louvre.junit.testrunner.extension;

import com.eviware.soapui.junit.*;
import com.eviware.soapui.report.JUnitReport;
import org.apache.xmlbeans.XmlOptions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReadyApiExtendedJUnitReport extends JUnitReport {

    /*int noOfTests;*/
    private boolean includeTestProperties;

    @Override
    public void setIncludeTestProperties(boolean includeTestProperties) {
        this.includeTestProperties = includeTestProperties;
    }

    private void setDatasourceProperties(HashMap<String, String> dataSourceProperties, Testcase testcase) {
        if(!this.includeTestProperties)
            return;

        Properties properties = testcase.addNewProperties();
//        setProperties(properties, dataSourceProperties);
    }

}

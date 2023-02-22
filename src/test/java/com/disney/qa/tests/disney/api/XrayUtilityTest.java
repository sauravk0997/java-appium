package com.disney.qa.tests.disney.api;

import com.disney.qa.api.xray.XrayUtility;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class XrayUtilityTest {

    private static final String JQL = "issue in TestRepositoryFolderTests(XCDQA,'CDQA Disney+/Disney Roku/Disney Roku Cross-Project Suite/Global Navigation','true')";

    @Test
    public void testTotalManualTestCaseCount() throws IOException, URISyntaxException {
        XrayUtility xrayUtility = new XrayUtility();
        String total = xrayUtility.retrieveXrayTestCases(JQL, false);
        Assert.assertTrue(total.equalsIgnoreCase("22"), "Total number of manual for Roku D+ Global Nav should be 22");
    }

    @Test
    public void testTotalAutomatedTestCount() throws IOException, URISyntaxException {
        XrayUtility xrayUtility = new XrayUtility();
        String total = xrayUtility.retrieveXrayTestCases(JQL, true);
        Assert.assertTrue(total.equalsIgnoreCase("20"), "Total number of automated tests for Roku D+ Global Nav should be 20");
    }

    @Test
    public void percentageOfAutomatedTestCases() throws IOException, URISyntaxException {
        XrayUtility xrayUtility = new XrayUtility();
        double epsilon = 0.000001d;
        double totalAutomated = Double.parseDouble(xrayUtility.retrieveXrayTestCases(JQL, true));
        double totalManual = Double.parseDouble(xrayUtility.retrieveXrayTestCases(JQL, false));
        double percentage = totalAutomated * 100 / totalManual;
        Assert.assertTrue(Precision.equals(percentage, 90.9090909090909, epsilon), "Percentage of total automated tests for Roku D+ Global Nav should be 90.9090909090909");
    }
}

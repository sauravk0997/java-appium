package com.disney.qa.tests.espn.ios;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.disney.qa.espn.ios.pages.settings.EspnSettingsCustomerSupportCodeIOSPageBase;
import com.disney.qa.espn.ios.pages.settings.EspnSettingsIOSPageBase;
import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.core.foundation.report.testrail.TestRailCases;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class EspnSettingsAcceptanceIOSTest extends BaseMobileTest {


    @TestRailCases(testCasesId = "1514510")
    @Test(description = "Navigate to Customer Support Code from Settings")
    public void testCustomerSupportCode() {

        SoftAssert softAssert = new SoftAssert();

        initPage(EspnIOSPageBase.class).getSettingsPage();

        EspnSettingsIOSPageBase espnSettingsIOSPageBase = initPage(EspnSettingsIOSPageBase.class);

        espnSettingsIOSPageBase.getCustomerSupportCode();

        Screenshot.capture(getDriver(), "Customer Support Code");

        EspnSettingsCustomerSupportCodeIOSPageBase espnSettingsCustomerSupportCodeIOSPageBase =
                initPage(        EspnSettingsCustomerSupportCodeIOSPageBase.class);

        softAssert.assertTrue(espnSettingsCustomerSupportCodeIOSPageBase.isCustomerSupportCodeTitlePresent(),
                "Expected = Customer Support Code to be displayed");

        espnSettingsCustomerSupportCodeIOSPageBase.getSettingsPage();

        espnSettingsIOSPageBase.clickCloseBtn();

        softAssert.assertAll();

    }


}

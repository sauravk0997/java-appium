package com.disney.qa.tests.disney.web.commerce;

import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusUILoad extends DisneyPlusBaseTest {

    @Test(threadPoolSize = 30, invocationCount = 300, description = "2p Bundle Load Test", groups = {"US", "AD", "AG", "AI", "AL", "AR", "AS", "AT", "AU", "AW", "AX", "BA", "BB", "BE", "BG", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CC", "CH", "CK", "CL", "CO", "CR", "CW", "CX", "CZ", "DE", "DK", "DM", "DO", "EC", "EE", "ES", "FI", "FK", "FO", "FR", "GB", "GD", "GF", "GG", "GI", "GL", "GP", "GR", "GS", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "IE", "IM", "IO", "IS", "IT", "JE", "JM", "JP", "KN", "KR", "KY", "LC", "LI", "LT", "LU", "LV", "MC", "ME", "MF", "MH", "MK", "MP", "MQ", "MS", "MT", "MU", "MX", "NC", "NF", "NI", "NL", "NO", "NU", "NZ", "PA", "PE", "PF", "PL", "PM", "PN", "PR", "PT", "PY", "RE", "RO", "RS", "RU", "SE", "SG", "SH", "SI", "SJ", "SK", "SM", "SR", "SV", "SX", "TC", "TF", "TK", "TR", "TT", "TW", "UM", "UY", "VA", "VC", "VE", "VG", "VI", "WF", "YT", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.STAR_SMOKE})
    public void ariel2pBundleLoadTest() throws JSONException {
        skipTestByEnv(QA);
        SoftAssert sa = new SoftAssert();
        boolean isCtaPresent = false;
        int retry = 0;

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        while (!isCtaPresent && retry <= 10) {

            commercePage = new DisneyPlusCommercePage(getDriver());
            String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

            commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
            commercePage.navigateToUrl();
            commercePage.waitForPageToFinishLoading();

            isCtaPresent = commercePage.isDplus2pBundleCtaPresent(TIMEOUT);

            if (!isCtaPresent) {
                commercePage.getDriver().manage().deleteAllCookies();
                restartDriver();
            }
            retry++;
        }

        sa.assertTrue(commercePage.isDplus2pBundleCtaPresent(EXPLICIT_TIMEOUT), "Hulu D+ 2p CTA not present");
        commercePage.clickDplus2pBundleCtaBtn();

        commercePage.isCreateAccountHuluContinueButtonPresent();
        commercePage.waitForPageToFinishLoading();
        pause(HALF_TIMEOUT);
        sa.assertAll();
    }
}

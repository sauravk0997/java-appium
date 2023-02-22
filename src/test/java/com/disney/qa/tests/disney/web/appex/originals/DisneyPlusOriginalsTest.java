package com.disney.qa.tests.disney.web.appex.originals;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.headermenu.DisneyPlusOriginalsPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisneyPlusOriginalsTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20112", "XWEBQAS-20114"})
    @Test(description = "Verify Logo/Content", groups = {"US", "MX", "JP" ,TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE})
    public void  verifyLogoPresent() {
        skipTestByEnv(QA);
        
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1512"));
        skipTestByProjectLocale(locale, new ArrayList<>(Arrays.asList("MX")) , EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        disneyAccount.set(account);

        DisneyPlusOriginalsPage originalPage = new DisneyPlusOriginalsPage(getDriver());
        originalPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        originalPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        originalPage.clickOnOriginalMenuOption();
        analyticPause();

        List<String> originals = originalPage.getTileAssetAttributes(10, "alt");

        sa.assertTrue(originalPage.isdPlusOriginalsCollectionIdPresent(),
                "Collections Id not present on: " + originalPage.getCurrentUrl());
        sa.assertTrue(!originals.isEmpty(),
                "No content tiles present under headers");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(sa);
    }
}

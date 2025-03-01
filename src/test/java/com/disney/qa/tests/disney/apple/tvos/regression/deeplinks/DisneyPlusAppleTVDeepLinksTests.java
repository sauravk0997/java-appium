package com.disney.qa.tests.disney.apple.tvos.regression.deeplinks;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVCollectionPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusAppleTVDeepLinksTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112888"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULK, US})
    public void verifyHuluHubDeeplinks() {
        DisneyPlusAppleTVCollectionPage collectionPage = new DisneyPlusAppleTVCollectionPage(getDriver());
        String networkName = "ABC";

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE));
        logIn(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_abc_network_language_deeplink"));
        Assert.assertTrue(collectionPage.isOpened(networkName),
                "ABC network page was not opened");
        Assert.assertTrue(collectionPage.getCollectionLogo(networkName).isPresent(),
                "ABC network logo was not present");
    }
}

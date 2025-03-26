package com.disney.qa.tests.disney.apple.tvos.regression.deeplinks;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVCollectionPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.US;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVDeepLinksTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112888"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, US})
    public void verifyHuluHubDeeplinks() {
        DisneyPlusAppleTVCollectionPage collectionPage = new DisneyPlusAppleTVCollectionPage(getDriver());
        String networkName = "ABC";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_abc_network_language_deeplink"));
        Assert.assertTrue(collectionPage.isOpened(networkName),
                "ABC network page was not opened");
        Assert.assertTrue(collectionPage.getCollectionLogo(networkName).isPresent(),
                "ABC network logo was not present");
    }
}

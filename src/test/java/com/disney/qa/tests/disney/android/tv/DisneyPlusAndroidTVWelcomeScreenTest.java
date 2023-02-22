package com.disney.qa.tests.disney.android.tv;

import com.disney.alice.AliceDriver;
import com.disney.alice.AliceUtilities;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVWelcomePage;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVWelcomeScreenTest extends DisneyPlusAndroidTVBaseTest {

    @Test(description = "Ensure welcome screen launches as a fresh instance of the app is installed")
    public void welcomeScreenIsLaunched(){
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66570"));
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-101271"));

        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);
    }

    @Test(description = "Verification of welcome screen buttons, texts and images")
    public void verifyWelcomeScreen() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66580"));

        SoftAssert sa = new SoftAssert();
        JsonNode paywallDictionary = getPaywallDictionary(language);
        JsonNode applicationDictionary = getApplicationDictionary(language);
        String mobileSpecText = apiProvider.get().getDictionaryItemValue(applicationDictionary,
                DisneyPlusAndroidTVWelcomePage.WelcomePageItems.PAYWALL_MOBILE_LINK.getText());

        List<String> welcomeScreenExpected = new ArrayList<>();

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);

        // Most devices have Sign Up enabled but others (ie Star Hub) will only be able to Log In.
        if (!disneyPlusAndroidTVWelcomePage.get().isPartnerDevice()) {
            // TODO: Figure a way to launch application without causing sign up button to be unfocused
            // sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isSignUpBtnFocused(), "Sign up button is not focused")

            DisneyPlusAndroidTVWelcomePage.WelcomePageItems.getWelcomeScreenItems().forEach(item ->
                    welcomeScreenExpected.add(apiProvider.get().getDictionaryItemValue(paywallDictionary, item)));

            updateList(welcomeScreenExpected);
        } else {
            DisneyPlusAndroidTVWelcomePage.WelcomePageItems.getWelcomeScreenItemsNoSignUp().forEach(item ->
                    welcomeScreenExpected.add(apiProvider.get().getDictionaryItemValue(paywallDictionary, item)));
        }

        welcomeScreenExpected.add(mobileSpecText);

        new AliceDriver(getCastedDriver()).screenshotAndRecognize()
                .isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText())
                .isLabelPresent(sa, AliceLabels.PIXAR_LOGO.getText())
                .isLabelPresent(sa, AliceLabels.MARVEL_LOGO.getText());

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isDeviceImagePresent(),
                "Cell phone icon element should be present.");

        List<String> welcomeScreenActual = disneyPlusAndroidTVWelcomePage.get().getWelcomeScreenTexts();

        IntStream.range(0, welcomeScreenExpected.size()).forEach(i ->
                sa.assertEquals(welcomeScreenActual.get(i), welcomeScreenExpected.get(i)));

        sa.assertTrue(new AliceUtilities(getCastedDriver()).getNumberOfLabelsPresent(AliceLabels.DISNEY_LOGO.getText()) >= 2,
                "Expected at least two disney_logo labels");

        sa.assertAll();
    }

    private void updateList(List<String> expectedList) {
        String item = expectedList.get(1);
        StringBuilder stringBuilder = new StringBuilder(item);
        item = stringBuilder.toString().replaceAll("\\$.*?}", "%s");
        expectedList.set(1, String.format(item, "$7.99", "month"));
    }
}

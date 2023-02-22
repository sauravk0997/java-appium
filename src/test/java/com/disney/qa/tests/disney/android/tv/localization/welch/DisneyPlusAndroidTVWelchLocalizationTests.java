package com.disney.qa.tests.disney.android.tv.localization.welch;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.utility.Environments;
import com.disney.qa.tests.disney.android.tv.DisneyPlusAndroidTVBaseTest;
import com.disney.util.ZipUtils;
import com.zebrunner.agent.core.registrar.Artifact;
import org.json.JSONException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Locale;

public class DisneyPlusAndroidTVWelchLocalizationTests extends DisneyPlusAndroidTVBaseTest {

    private String baseFile = "Screenshots/";

    @BeforeMethod
    public void setUp() throws IOException, URISyntaxException, JSONException {
        initiateProxy(new Locale("", country).getDisplayCountry());
        switchEnv(Environments.QA);
        new AndroidUtilsExtended().clearAppCache();

        disneyAccountApi.get().addFlex(entitledUser.get());
        disneyAccountApi.get().addProfile(entitledUser.get(), "test", language, null, false);
        disneyAccountApi.get().overrideLocations(entitledUser.get(), country);
    }

    @Test
    public void welchExistingIAP() throws IOException {
        String flow = "Existing-Sub-CD-IAP";
        String directory = baseFile + flow + "/";
        String pathToZip = String.format("Screenshots_%s_%s-%s.zip",flow, language, country);
        int count = 0;
        disneyPlusAndroidTVWelcomePage.get().isOpened();
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(entitledUser.get().getUserPass());

        disneyPlusAndroidTVProfilePageBase.get().isAddProfilePresent();
        disneyPlusAndroidTVProfilePageBase.get().selectDefaultProfile();

        disneyPlusAndroidTVWelchPageBase.get().isOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        //Select Now on Disney+ STAR continue button
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.get().isFullCatalogScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        //Verify the focus is on the Not now button on Access Full catalog screen
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        //Select the Not now button on Access Full catalog screen
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.get().isMaturityRatingConfirmationScreenOpen();
        //Verify the focus is on got it button on rating confirmation screen
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        //Select the back button on maturity rating confirmation screen
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        //Select continue on the you have access the full catalog screen
        disneyPlusAndroidTVWelchPageBase.get().isYouHaveNowFullAccessScreenOpened();
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();


        disneyPlusAndroidTVWelchPageBase.get().isConfirmPasswordScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        //Attempt to login with the wrong password
        disneyPlusAndroidTVLoginPage.get().logInWithPassword("1234");
        if (disneyPlusAndroidTVWelchPageBase.get().isErrorViewPresent())
            UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());

        //Focus the input password screen
        disneyPlusAndroidTVWelchPageBase.get().pressUp(1);
        //Login to the start welcome screen
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(entitledUser.get().getUserPass());

        disneyPlusAndroidTVWelchPageBase.get().isWelcomeToStarScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        //Open Create Profile pin screen
        disneyPlusAndroidTVWelchPageBase.get().selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.get().isCreatePinScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        //Generate the error message
        disneyPlusAndroidTVWelchPageBase.get().clickSetProfilePin();
        if (disneyPlusAndroidTVWelchPageBase.get().isPinErrorMessagePresent())
            UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());

        disneyPlusAndroidTVWelchPageBase.get().typeUsingKeyEvents("1235");
        //move down to set profile pin button
        disneyPlusAndroidTVWelchPageBase.get().pressDown(3);
        disneyPlusAndroidTVWelchPageBase.get().selectFocusedElement();

        //Wait for pin message and take screenshot
        if (disneyPlusAndroidTVWelchPageBase.get().isPinSetMessagePresent())
            UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());

        disneyPlusAndroidTVWelchPageBase.get().isMaturityRatingScreenOpened();
        //Select rating for 2nd profile
        disneyPlusAndroidTVWelchPageBase.get().selectFocusedElement();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());

        ZipUtils.zipDirectory(directory, pathToZip);
        Artifact.attachToTest(pathToZip, Path.of(pathToZip));
    }

    @Test
    public void welchAddProfileFlow() throws IOException {
        String flow = "Add-Profile";
        String directory = baseFile + flow + "/";
        int count = 0;
        String pathToZip = String.format("Screenshots_%s_%s-%s.zip",flow, language, country);
        disneyPlusAndroidTVWelcomePage.get().isOpened();
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(entitledUser.get().getUserPass());

        disneyPlusAndroidTVProfilePageBase.get().isAddProfilePresent();
        //From Default profile press right twice to focus add profile btn and select it
        disneyPlusAndroidTVProfilePageBase.get().pressRight(2);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        disneyPlusAndroidTVProfilePageBase.get().isSkipBtnFocused();
        //Select Skip btn to get the default avatar on avatar selection
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        disneyPlusAndroidTVProfilePageBase.get().typeUsingKeyEvents("test1");
        disneyPlusAndroidTVProfilePageBase.get().focusOptionEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileDoneBtn());
        //Select done button on create profile screen
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.get().isAccessFullCatalogScreenFromAddProfileOpen();
        //Avatar takes time to laod
        pause(5);
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        //Select continue button on Access the full catalog screen
        disneyPlusAndroidTVWelchPageBase.get().selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.get().isConfirmPasswordScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());
        disneyPlusAndroidTVWelchPageBase.get().typeUsingKeyEvents(entitledUser.get().getUserPass());
        //Press down and select login button from Confirm password screen
        disneyPlusAndroidTVWelchPageBase.get().pressDown(1);
        disneyPlusAndroidTVWelchPageBase.get().selectFocusedElement();

        //Want to create a pin screen
        disneyPlusAndroidTVWelchPageBase.get().isSecureYourProfileTitleOpen();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());

        //Press back to open Skip this step screen
        disneyPlusAndroidTVWelchPageBase.get().pressBackTimes(1);
        disneyPlusAndroidTVWelchPageBase.get().isSkipThisStepScreenOpen();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());

        //Get Back to Create a pin screen
        disneyPlusAndroidTVWelchPageBase.get().pressDown(1);
        disneyPlusAndroidTVWelchPageBase.get().selectFocusedElement();

        //Select Set Profile PIN after backing from skip this step screen
        disneyPlusAndroidTVWelchPageBase.get().isSecureYourProfileTitleOpen();
        disneyPlusAndroidTVWelchPageBase.get().selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.get().isCreatePinScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, directory, getCastedDriver());

        ZipUtils.zipDirectory(directory, pathToZip);
        Artifact.attachToTest(pathToZip, Path.of(pathToZip));
    }
}

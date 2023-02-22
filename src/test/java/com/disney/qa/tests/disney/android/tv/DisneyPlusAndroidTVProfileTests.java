package com.disney.qa.tests.disney.android.tv;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.alice.model.CompareImagesMetadata;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVProfilePageBase;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.PROFILE;
import static com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVProfilePageBase.ProfileItems.*;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.DIS;

public class DisneyPlusAndroidTVProfileTests extends DisneyPlusAndroidTVBaseTest {

    private ThreadLocal<List<ContentSet>> avatarSets = new ThreadLocal<>();

    private String aliceCaptionFormat = "%s %s";
    private static final String DEFAULT_PROFILE = "Test";
    private static final String TEST_PROFILE = "testProfile";
    private static final String DARTH_MAUL = R.TESTDATA.get("disney_darth_maul_avatar_id");
    private static final String MICKEY_MOUSE = "Mickey Mouse";

    @BeforeMethod
    public void setUp() {
        avatarSets.set(disneySearchApi.get().getAllSetsInAvatarCollection(entitledUser.get(), country, language));
    }

    @Test(description = "Verify texts and add profile for less than 7 profiles", groups = {"smoke"})
    public void profileSelectionScreenVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67006", "XCDQA-67012"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1852"));
        SoftAssert sa = new SoftAssert();
        List<String> profileScreenTexts = profileSelection;
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        IntStream.range(0, profileScreenTexts.size()).forEach(i -> {
            String text;
            if (profileScreenTexts.get(i).equals(EDIT_PROFILE_BTN.getText())) {
                text = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), profileScreenTexts.get(i)).toUpperCase();
                aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, text, AliceLabels.BUTTON.getText());
            } else {
                text = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), profileScreenTexts.get(i));
                sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().profileSelectionTexts().get(i), text);
            }
        });

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getNumberOfImages(), 2);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isAddProfilePresent(), "Add profile button should be present");

        checkAssertions(sa);
    }

    @Test(description = "Max 7 profiles")
    public void verifyMaximumSevenProfiles() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67018"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1853"));
        SoftAssert sa = new SoftAssert();

        IntStream.range(0, 6).forEach(i -> {
            disneyAccountApi.get().addProfile(entitledUser.get(), DEFAULT_PROFILE + i, language, null, false);
        });

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(entitledUser.get().getUserPass());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        List<String> allProfileNames = disneyPlusAndroidTVProfilePageBase.get().getAllProfileNames();
        //Maximum Seven profiles allowed per account
        IntStream.range(0, 7).forEach(i -> {
            String profileName = i == 0 ? DEFAULT_PROFILE : DEFAULT_PROFILE + (i - 1);
            sa.assertEquals(allProfileNames.get(i), profileName);
        });

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getNumberOfImages(), 7);
        sa.assertFalse(disneyPlusAndroidTVProfilePageBase.get().isAddProfilePresent(), "Add Profile button should not be present");

        checkAssertions(sa);
    }

    @Test(groups = {"smoke"})
    public void defaultProfileFocusProfileSelection() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67002", "XCDQA-67004"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1854"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isDefaultProfileFocused(DEFAULT_PROFILE), "Default profile not focused");

        checkAssertions(sa);
    }

    @Test
    public void profileSelectionAndAvatarVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67008", "XCDQA-67010"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1855"));
        SoftAssert sa = new SoftAssert();
        NavHelper nav = new NavHelper(getCastedDriver());
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, DARTH_MAUL, false);

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectDefaultProfileAfterFocused();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        int index = disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus();

        // TODO: Replace below with .isLabelPresent() and check for the mickey mouse avatar once QCE-1017 is resolved.
        aliceDriver.screenshotAndRecognize().isLabelNotPresent(sa, AliceLabels.DARTH_MAUL_AVATAR.getText());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, index);
        disneyPlusAndroidTVProfilePageBase.get().selectProfile(TEST_PROFILE);

        nav.keyUntilElementFocused(()->disneyPlusAndroidTVProfilePageBase.get().getUpdateBirthdateOption(), AndroidKey.DPAD_DOWN);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        //Handle DOB, gender and kids mode prompts...
        disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972" );
        nav.keyUntilElementFocused(()->disneyPlusAndroidTVProfilePageBase.get().getUpdateGenderOption(), AndroidKey.DPAD_DOWN);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
        disneyPlusAndroidTVCommonPage.get().clickConfirmIfPresent();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus();

        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DARTH_MAUL_AVATAR.getText());

        checkAssertions(sa);
    }

    @Test
    public void chooseIconAndEditProfileLanding() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67014", "XCDQA-67016"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1856"));
        SoftAssert sa = new SoftAssert();
        String iconSelectionTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CHOOSE_ICON_TITLE.getText());
        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILES_TITLE.getText());
        String addProfile = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CREATE_PROFILE.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().clickOnContentDescExact(addProfile);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnPresent(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), iconSelectionTitle);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        disneyPlusAndroidTVProfilePageBase.get().clickEditProfiles();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle().toLowerCase(), editProfileTitle.toLowerCase());

        checkAssertions(sa);
    }

    @Test(groups = {"smoke"})
    public void createSingleProfileAndVerify() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66780", "XCDQA-66782", "XCDQA-66784", "XCDQA-66786", "XCDQA-66788"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1857"));
        NavHelper nav = new NavHelper(getCastedDriver());
        SoftAssert sa = new SoftAssert();
        String chooseProfile = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), PROFILE_SELECTION_TITLE.getText());

        login(entitledUser.get());
        nav.openNavBarAndSelect(PROFILE.getText());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnFocused(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        //Select default Icon by skipping
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        // Ensure edit box is visible before attempting to enter text.
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), "Profile name page should open");
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();

        //Handle DOB, gender and kids mode prompts...
        disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972" );
        disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
        disneyPlusAndroidTVProfilePageBase.get().selectKidsMode(sa, false);

        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileDoneBtn());
        // Handle maturity screen
        disneyPlusAndroidTVWelchPageBase.get().dismissFullCatalogScreen(sa);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), chooseProfile);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAllProfileNames().get(0), DEFAULT_PROFILE);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAllProfileNames().get(1), TEST_PROFILE);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus();
        sa.assertEquals(disneyPlusAndroidTVCommonPage.get().getProfileNameOnGlobalNav(), TEST_PROFILE);

        checkAssertions(sa);
    }

    @Test(groups = {"smoke"})
    public void createMultipleProfilesAndVerifyAddProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66790"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1858"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        IntStream.range(0, 6).forEach(i -> {
            sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
            disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();
            sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnFocused(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
            disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

            sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), "Profile name page should open");
            disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE + i, true);
            disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();

            disneyPlusAndroidTVCommonPage.get().pressTabToMoveToTheNextField();
            disneyPlusAndroidTVCommonPage.get().pressRight(1);
            disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
            // Handle maturity screen
            disneyPlusAndroidTVWelchPageBase.get().dismissFullCatalogScreen(sa);
        });
        sa.assertFalse(disneyPlusAndroidTVProfilePageBase.get().isAddProfilePresent(), "Add profile button should not be present");

        checkAssertions(sa);
    }

    @Test
    public void chooseAvatarPageVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66832", "XCDQA-66834", "XCDQA-66836", "XCDQA-66838"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1859"));
        SoftAssert sa = new SoftAssert();

        String chooseProfileIconTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CHOOSE_ICON_TITLE.getText());
        String profileIconSkipBtn = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CHOOSE_ICON_SKIP_BTN.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnPresent(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), chooseProfileIconTitle);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getSkipBtnText(), profileIconSkipBtn);

        disneyPlusAndroidTVProfilePageBase.get().navigateAvatars(disneySearchApi.get(), entitledUser.get().getProfileLang().toLowerCase(), entitledUser.get(), sa);

        checkAssertions(sa);
    }

    @Test
    public void selectIconAndVerifyAddProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66840"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1860"));
        SoftAssert sa = new SoftAssert();
        NavHelper nav = new NavHelper(getCastedDriver());
        String addProfile = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CREATE_PROFILE.getText());
        List<String> avatarNames = avatarSets.get().get(1).getTitles();

        login(entitledUser.get());
        nav.openNavBarAndSelect(PROFILE.getText());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();

        //Select the second Icon
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnPresent(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().clickOnContentDescContains(avatarNames.get(0));
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), "Profile name page should open");
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();

        //Handle DOB, gender and kids mode prompts...
        disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972" );
        disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
        disneyPlusAndroidTVProfilePageBase.get().selectKidsMode(sa, false);

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), addProfile);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().getProfileImageContentDesc().contains(avatarNames.get(0)),
                String.format("Expected: %s icon to be present", avatarNames.get(0)));

        checkAssertions(sa);
    }

    @Test
    public void selectSkipBtnAndVerifyAddProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66842"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1861"));
        SoftAssert sa = new SoftAssert();
        NavHelper nav = new NavHelper(getCastedDriver());
        String addProfile = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CREATE_PROFILE.getText());
        List<String> avatarNames = avatarSets.get().get(0).getTitles();

        login(entitledUser.get());
        nav.openNavBarAndSelect(PROFILE.getText());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnFocused(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), "Profile name page should open");
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();

        //Handle DOB, gender and kids mode prompts...
        disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972" );
        disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
        disneyPlusAndroidTVProfilePageBase.get().selectKidsMode(sa, false);

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), addProfile);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().getProfileImageContentDesc().contains(avatarNames.get(1)),
                String.format("Expected: %s icon to be present", avatarNames.get(1)));

        checkAssertions(sa);
    }

    @Test
    public void verifyNoIconDuplication() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66844"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1862"));
        SoftAssert sa = new SoftAssert();
        String mickeyMouse = avatarSets.get().get(0).getTitle(0);

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();

        IntStream.range(1, avatarSets.get().size()).forEach(i -> {
            JsonNode icons = avatarSets.get().get(i).getJsonNode().get(0);

            int iconsPerCategory = Integer.valueOf(DisneySearchApi.parseValueFromJson(icons.toString(), "$..items.size()").get(0));
            int rightMoves = 0;
            if (iconsPerCategory > 7) {
                rightMoves = iconsPerCategory / 6;
                rightMoves = iconsPerCategory % 6 > 0 ? rightMoves + 1 : rightMoves;
                rightMoves = i == 1 ? rightMoves : rightMoves - 1;
            }

            disneyPlusAndroidTVCommonPage.get().pressDown(1);
            int shelfIndex = i == 1 ? 1 : 2;
            int j = 0;
            disneyPlusAndroidTVProfilePageBase.get().getAllIconsContentDesc(shelfIndex).forEach(item ->
                    sa.assertFalse(item.contains(mickeyMouse), "Mickey Mouse Avatar Icon found"));
            while (j < rightMoves) {
                disneyPlusAndroidTVCommonPage.get().pressRight(6);
                disneyPlusAndroidTVProfilePageBase.get().getAllIconsContentDesc(shelfIndex).forEach(item ->
                        sa.assertFalse(item.contains(MICKEY_MOUSE), "Mickey Mouse Avatar Icon found"));
                j++;
            }
        });
        checkAssertions(sa);
    }

    @Test
    public void duplicateProfileNameError() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66846", "XCDQA-66848"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1863"));
        SoftAssert sa = new SoftAssert();
        NavHelper nav = new NavHelper(getCastedDriver());
        String profileError = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DUPLICATE_PROFILE_NAME_ERROR.getText());

        login(entitledUser.get());
        nav.openNavBarAndSelect(DEFAULT_PROFILE);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnFocused(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        // Ensure edit box is visible before attempting to enter text...
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), "Profile name page should open");
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(DEFAULT_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().inputProfileErrorText(), profileError);

        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().pressTab();
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        //Handle DOB, gender and kids mode prompts...
        disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972" );
        disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
        disneyPlusAndroidTVProfilePageBase.get().selectKidsMode(sa, false);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), "Edit profile page should be open");
        disneyPlusAndroidTVProfilePageBase.get().selectEditProfileDoneBtn();

        // Handle maturity screen
        disneyPlusAndroidTVWelchPageBase.get().dismissFullCatalogScreen(sa);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAllProfileNames().get(1), TEST_PROFILE);

        checkAssertions(sa);
    }

    @Test
    public void verifyEditProfileDoneBtnFunctionality() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65770", "XCDQA-65772"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1864"));
        SoftAssert sa = new SoftAssert();
        String profileSelection = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), PROFILE_SELECTION_TITLE.getText());
        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILES_TITLE.getText());
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, null, false);

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().clickEditProfiles();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle().toLowerCase(), editProfileTitle.toLowerCase());
        //The button below is the done button on editProfiles
        disneyPlusAndroidTVProfilePageBase.get().clickEditProfiles();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), profileSelection);

        checkAssertions(sa);
    }

    @Test
    public void verifyProfileSelectionFromEditProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65774"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1865"));
        SoftAssert sa = new SoftAssert();
        String profileSelection = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), PROFILE_SELECTION_TITLE.getText());
        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILES_TITLE.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().pressDownAndSelect();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle().toLowerCase(), editProfileTitle.toLowerCase());
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        disneyPlusAndroidTVProfilePageBase.get().selectEditProfileDoneBtn();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), profileSelection);

        checkAssertions(sa);
    }

    @Test
    public void verifyAutoPlayOnDefault() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65776", "XCDQA-65778"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1866"));
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        SoftAssert sa = new SoftAssert();

        String autoPlay = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), AUTO_PLAY_EDIT_PROFILE.getText());
        String toggleOn = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), TOGGLE_ON.getText());
        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILE_TITLE.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), editProfileTitle);
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, String.format(aliceCaptionFormat, autoPlay, toggleOn), AliceLabels.VERTICAL_MENU_ITEM.getText());
        checkAssertions(sa);
    }

    @Test
    public void verifyAutoPlayPersistsOnAppRelaunch() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65780", "XCDQA-65782"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1867"));
        SoftAssert sa = new SoftAssert();
        String toggleOn = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), TOGGLE_ON.getText());
        String toggleOff = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), TOGGLE_OFF.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAutoPlayState(), toggleOn);
        disneyPlusAndroidTVProfilePageBase.get().clickAutoPlay();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAutoPlayState(), toggleOff);
        disneyPlusAndroidTVProfilePageBase.get().selectEditProfileDoneBtn();

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(),
                "Who's watching page should show after saving profile settings.");

        closeApp();
        // Let app close and Android Home load...
        pause(5);
        launchTVapp(APP_PACKAGE_DISNEY, APP_LAUNCH_ACTIVITY);

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAutoPlayState(), toggleOff);

        checkAssertions(sa);
    }

    @Test
    public void profileLanguageOptionsVerification() throws Exception {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65784"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1868"));
        SoftAssert sa = new SoftAssert();
        JsonNode remoteConfig = disneyAccountApi.get().getGlobalizationConfig(language);
        List<String> getAllSupportedLanguages = apiProvider.get().queryResponse(remoteConfig, SUPPORTED_LANGUAGE_CODES.getText());
        List<String> languageNames = getAllSupportedLanguages.stream().map(code -> apiProvider.get().queryResponse(remoteConfig,
                String.format(SUPPORTED_LANGUAGE_NAME.getText(), code)).get(0)).sorted().collect(Collectors.toList());
        int englishIndex = languageNames.indexOf(apiProvider.get().queryResponse(remoteConfig,
                String.format(SUPPORTED_LANGUAGE_NAME.getText(), entitledUser.get().getProfileLang().toLowerCase())).get(0));

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), EDIT_PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileLanguageOption());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isLanguageScreenOpen(), LANGUAGE_SETTINGS_PAGE_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isLanguageFocused(englishIndex), "English (US) should be focused but is not");
        disneyPlusAndroidTVProfilePageBase.get().focusFirstLanguage(languageNames.get(0));

        languageNames.forEach(item -> {
            sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getFocusedLanguageText(), item);
            disneyPlusAndroidTVProfilePageBase.get().pressDown(1);
        });
        checkAssertions(sa);
    }

    @Test
    public void verifyFocusOnIconSelectionLandingFromEditProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66888", "XCDQA-66890"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1869"));
        SoftAssert sa = new SoftAssert();
        String iconSelectionTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CHOOSE_ICON_TITLE.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileIconOption());
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), iconSelectionTitle);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isFirstIconFocused(), "First icon should be focused");

        checkAssertions(sa);
    }

    //TODO: There is a specific issue on FireTV cube where the XML doesn't match the UI, this will fail on FireTV cube
    @Test
    public void verifyProfileNameAndIconEditProfileIconSelection() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66892"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1870"));
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        NavHelper nav = new NavHelper(getCastedDriver());

        login(entitledUser.get());
        // We need to focus the profile image on the navbar so its brightness matches that of the icon used on
        // the edit profile page. Open NavBar and focus on profile before taking screenshot.
        nav.keyUntilElementVisible(()->disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        nav.navigateNavBar("Profile");
        BufferedImage getNavbarProfileAvatar = disneyPlusAndroidTVCommonPage.get().getProfileImage();

        nav.press(AndroidKey.ENTER);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileIconOption());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isIconSelectionFromEditProfileOpened(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getIconSelectionProfileName(), DEFAULT_PROFILE);

        // Get active profile icon from edit profile page...
        BufferedImage currentIconSelection = disneyPlusAndroidTVProfilePageBase.get().getCurrentIconOnIconSelection();
        File outputFile1 = new File("im1.png");
        File outputFile2 = new File("im2.png");

        try {
            ImageIO.write(getNavbarProfileAvatar, "png", outputFile1);
            ImageIO.write(currentIconSelection, "png", outputFile2);
        }
        catch (IOException e) {
            LOGGER.error("IOException writing profile icon images...", e);
        }

        // Since the avatar icon on the NavBar has a focus highlight ring
        // and the one on the profile edit page does not, similarity will not be 100%.
        CompareImagesMetadata imageData = aliceDriver.compare(outputFile1, outputFile2);
        sa.assertTrue(imageData.getDnn().getSimilarity() > 95, "Image similarity between avatar icons should exceed 95%");

        checkAssertions(sa);
    }

    @Test
    public void verifyAvatarDuplicationAndFocusFromEditProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66894", "XCDQA-66896", "XCDQA-66898"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1871"));
        SoftAssert sa = new SoftAssert();
        String mickeyMouse =  avatarSets.get().get(0).getTitle(0);
        String ironMan =  avatarSets.get().get(0).getTitle(1);

        String ironManAvatarId = avatarSets.get().get(0).getAvatarIds().get(1);
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, ironManAvatarId, false);

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileIconOption());
        disneyPlusAndroidTVProfilePageBase.get().navigateAvatarsAndVerifyFocus(avatarSets.get(), sa, mickeyMouse, ironMan);

        checkAssertions(sa);
    }

    @Test
    public void verifyIconUpdateOnEditProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66900"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1872"));
        SoftAssert sa = new SoftAssert();
        String secondFeaturedAvatar = avatarSets.get().get(1).getTitle(0);
        String iconSelectionTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CHOOSE_ICON_TITLE.getText());
        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILE_TITLE.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileIconOption());
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), iconSelectionTitle);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), editProfileTitle);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().getProfileImageContentDesc().contains(secondFeaturedAvatar), String.format("Avatar %s not properly updated", secondFeaturedAvatar));

        checkAssertions(sa);
    }

    @Test
    public void verifyBackFromEditProfileIconSelection() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66902"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1873"));
        SoftAssert sa = new SoftAssert();

        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILE_TITLE.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileIconOption());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isIconSelectionFromEditProfileOpened(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().pressBackTimes(1);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), editProfileTitle);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().getProfileImageContentDesc().contains(MICKEY_MOUSE));

        checkAssertions(sa);
    }

    @Test
    public void verifyProfileDeletionScreen() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66850", "XCDQA-66854"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1874"));
        SoftAssert sa = new SoftAssert();
        List<String> expectedText = new ArrayList<>();
        deleteProfileTexts.forEach(item -> {
            String expectedItem = apiProvider.get().getDictionaryItemValue(getApplicationDictionary(language), item);
            if (item.equals(DELETE_PROFILE_TITLE.getText()))
                expectedText.add(expectedItem.replace("{user_profile}", TEST_PROFILE));
            else
                expectedText.add(expectedItem);
        });
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, DARTH_MAUL, false);

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(1);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), EDIT_PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectDeleteProfile(sa);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isDeleteBtnFocused(), "Delete button is not focused");

        List<String> actualTexts = disneyPlusAndroidTVProfilePageBase.get().getDeleteProfileTexts();
        IntStream.range(0, expectedText.size()).forEach(i -> sa.assertEquals(actualTexts.get(i), expectedText.get(i)));

        checkAssertions(sa);
    }

    @Test
    public void cancelButtonFromDeleteProfileReturnsToEditProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66852"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1875"));
        SoftAssert sa = new SoftAssert();

        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILE_TITLE.getText());
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, DARTH_MAUL, false);

        loginWithoutHomeCheck(entitledUser.get());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().editProfile(1);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), EDIT_PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().closeKeyboard();

        disneyPlusAndroidTVProfilePageBase.get().selectDeleteProfile(sa);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isDeleteProfileOpened(), DELETE_PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().deleteProfileCancelBtnClick();

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), editProfileTitle);

        checkAssertions(sa);
    }

    @Test
    public void deleteProfileReturnsToEditProfiles() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66856", "XCDQA-66858"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1876"));
        SoftAssert sa = new SoftAssert();
        String editProfilesTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILES_TITLE.getText());
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, DARTH_MAUL, false);

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(1);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), EDIT_PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectDeleteProfile(sa);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isDeleteProfileOpened(), DELETE_PROFILE_PAGE_LOAD_ERROR);
        //Delete Profile
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), EDIT_PROFILE_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle().toLowerCase(), editProfilesTitle.toLowerCase());

        checkAssertions(sa);
    }

    @Test
    public void verifyActiveNonPrimaryProfileDeleted() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66860"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1877"));
        SoftAssert sa = new SoftAssert();
        NavHelper nav = new NavHelper(getCastedDriver());

        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, null, false);
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE + 1, language, null, false);

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVProfilePageBase.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectProfile(TEST_PROFILE);

        // DOB/Gender handler
        if(disneyPlusAndroidTVProfilePageBase.get().getUpdateBirthdateOption().isVisible()) {
            nav.keyUntilElementFocused(() -> disneyPlusAndroidTVProfilePageBase.get().getUpdateBirthdateOption(), AndroidKey.DPAD_DOWN);
            disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
            disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972");
            nav.keyUntilElementFocused(() -> disneyPlusAndroidTVProfilePageBase.get().getUpdateGenderOption(), AndroidKey.DPAD_DOWN);
            disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
            disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
            disneyPlusAndroidTVProfilePageBase.get().selectEditProfileDoneBtn();
        }

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVProfilePageBase.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), EDIT_PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectDeleteProfile(sa);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isDeleteProfileOpened(), DELETE_PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().getAllProfileNames().forEach(profileName -> sa.assertNotEquals(profileName, TEST_PROFILE));

        checkAssertions(sa);
    }

    @Test
    public void verifyPrimaryProfileIndelible() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66862"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1878"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, DARTH_MAUL, false);
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        sa.assertFalse(disneyPlusAndroidTVProfilePageBase.get().isDeleteProfileBtnPresentOnEditProfile(),
                "Delete profile btn should not be present");

        checkAssertions(sa);
    }

    @Test
    public void modifyExistingProfileName() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66904", "XCDQA-66906"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1879"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), EDIT_PROFILE_PAGE_LOAD_ERROR);

        // Edit the profile name
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileNameOption());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), PROFILE_NAME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().typeUsingKeyEvents(TEST_PROFILE);
        if (!AndroidTVUtils.isAmazon()) {
            disneyPlusAndroidTVCommonPage.get().closeKeyboard();
        }
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileOpen(), EDIT_PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectEditProfileDoneBtn();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAllProfileNames().get(0), DEFAULT_PROFILE + TEST_PROFILE);

        checkAssertions(sa);
    }

    @Test
    public void verifyDuplicateAndEmptyNameEditProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66908", "XCDQA-66910", "XCDQA-66868"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1880"));
        SoftAssert sa = new SoftAssert();
        String duplicateName = DEFAULT_PROFILE + TEST_PROFILE + 0;
        String profileError = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DUPLICATE_PROFILE_NAME_ERROR.getText());
        String emptyProfileNameError = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EMPTY_PROFILE_NAME_ERROR.getText());
        disneyAccountApi.get().addProfile(entitledUser.get(), duplicateName, language, null, false);
        loginWithoutHomeCheck(entitledUser.get());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileNameOption());
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(duplicateName, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().inputProfileErrorText(), profileError);
        disneyPlusAndroidTVProfilePageBase.get().clearProfileNameInputField();
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().inputProfileErrorText(), emptyProfileNameError);

        checkAssertions(sa);
    }

    @Test
    public void verifyUpdateProfileNameAfterRemovingDuplicate() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66870"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1881"));
        SoftAssert sa = new SoftAssert();
        String profileError = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DUPLICATE_PROFILE_NAME_ERROR.getText());
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, null, false);

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        //Select default profile
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        disneyPlusAndroidTVProfilePageBase.get().selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.get().getEditProfileNameOption());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), PROFILE_NAME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().inputProfileErrorText(), profileError);
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName("new", false);

        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();

        checkAssertions(sa);
    }

    @Test
    public void verifyKidsModeToggledOffCreateProfile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66878", "XCDQA-66880"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1882"));
        SoftAssert sa = new SoftAssert();
        NavHelper nav = new NavHelper(getCastedDriver());

        String toggleOff = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), TOGGLE_OFF.getText());
        String toggleOn = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), TOGGLE_ON.getText());
        String addProfile = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CREATE_PROFILE.getText());

        login(entitledUser.get());
        nav.openNavBarAndSelect(PROFILE.getText());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnFocused(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), "Profile name page should open");
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();

        //Handle DOB, gender and kids mode prompts...
        disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972" );
        disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
        disneyPlusAndroidTVProfilePageBase.get().selectKidsMode(sa, false);

        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), addProfile);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAutoPlayState(), toggleOff);
        disneyPlusAndroidTVProfilePageBase.get().toggleKidsMode();
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getAutoPlayState(), toggleOn);

        checkAssertions(sa);
    }

    @Test
    public void verifyKidsProfileIsSet() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66882"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1883"));
        SoftAssert sa = new SoftAssert();
        NavHelper nav = new NavHelper(getCastedDriver());
        String mickey = "Mickey and Friends";

        login(entitledUser.get());
        nav.openNavBarAndSelect(PROFILE.getText());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectAddProfile();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnFocused(), PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isEditProfileNameOpen(), "Profile name page should open");
        disneyPlusAndroidTVProfilePageBase.get().inputProfileName(TEST_PROFILE, true);
        disneyPlusAndroidTVProfilePageBase.get().selectProfileNameContinueButton();

        //Handle DOB, gender and kids mode prompts...
        disneyPlusAndroidTVLoginPage.get().enterDOB(sa, "01011972" );
        disneyPlusAndroidTVProfilePageBase.get().selectGender(sa, DisneyPlusAndroidTVProfilePageBase.Gender.NO_SAY);
        disneyPlusAndroidTVProfilePageBase.get().selectKidsMode(sa, false);

        disneyPlusAndroidTVProfilePageBase.get().toggleKidsMode();
        disneyPlusAndroidTVProfilePageBase.get().selectEditProfileDoneBtn();
        disneyPlusAndroidTVProfilePageBase.get().selectProfile(TEST_PROFILE);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isKidsHomePageOpen(), HOME_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVDiscoverPage.get().getBrandTilesContentDesc(1).get(0), mickey);

        checkAssertions(sa);
    }

    //TODO:Edit profile doesn't have a kids mode toggle, look what to switch this with
    @Test(enabled = false)
    public void verifyAutoPlayToggleOffAfterKidsProfileMode() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66884"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1884"));
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        String autoPlay = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), AUTO_PLAY_EDIT_PROFILE.getText());
        String toggleOff = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), TOGGLE_OFF.getText());
        String toggleOn = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), TOGGLE_ON.getText());
        String kidsProfile = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), KIDS_PROFILE.getText());
        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILE_TITLE.getText());
        disneyAccountApi.get().addProfile(entitledUser.get(), TEST_PROFILE, language, null, false);

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(entitledUser.get().getUserPass());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        //Select Default profile
        disneyPlusAndroidTVProfilePageBase.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(1);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), editProfileTitle);
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, String.format(aliceCaptionFormat, kidsProfile, toggleOff), AliceLabels.VERTICAL_MENU_ITEM.getText())
                .assertLabelContainsCaption(sa, String.format(aliceCaptionFormat, autoPlay, toggleOn), AliceLabels.VERTICAL_MENU_ITEM.getText());
        disneyPlusAndroidTVProfilePageBase.get().toggleKidsMode();
        //Required because screenshot is taken faster than the UI is updated
        pause(5);
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, String.format(aliceCaptionFormat, kidsProfile, toggleOn), AliceLabels.VERTICAL_MENU_ITEM.getText())
                .assertLabelContainsCaption(sa, String.format(aliceCaptionFormat, autoPlay, toggleOff), AliceLabels.VERTICAL_MENU_ITEM.getText());

        checkAssertions(sa);
    }

    @Test
    public void verifyPrimaryProfileNoKidsMode() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66886"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1885"));
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        String kidsProfile = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), KIDS_PROFILE.getText());
        String editProfileTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), EDIT_PROFILE_TITLE.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(PROFILE, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().editProfile(0);
        sa.assertEquals(disneyPlusAndroidTVProfilePageBase.get().getProfileScreenTitle(), editProfileTitle);
        aliceDriver.screenshotAndRecognize().assertNoLabelContainsCaption(sa, kidsProfile, AliceLabels.VERTICAL_MENU_ITEM.getText());

        checkAssertions(sa);
    }
}

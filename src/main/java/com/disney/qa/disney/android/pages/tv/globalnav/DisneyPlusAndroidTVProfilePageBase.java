package com.disney.qa.disney.android.pages.tv.globalnav;

import com.disney.exceptions.FailedToFocusElementException;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:CallToDeprecatedMethod" })
public class DisneyPlusAndroidTVProfilePageBase extends DisneyPlusAndroidTVCommonPage {

    public enum Gender {
        MAN,
        WOMAN,
        NON_BINARY,
        NO_SAY
    }

    private AndroidTVUtils androidTVUtils;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/gender']")
    private ExtendedWebElement genderId;

    @FindBy(id = "titleTextView")
    private ExtendedWebElement profilePageTitle;

    @FindBy(id = "kidsModeTitle")
    private ExtendedWebElement kidsModePageTitle;

    @FindBy(id = "onButton")
    private ExtendedWebElement kidsModeOnButton;

    @FindBy(id = "offButton")
    private ExtendedWebElement kidsModeOffButton;

    @FindBy(id = "profileNameInput")
    private ExtendedWebElement editProfileProfileNameField;

    @FindBy(id = "editAllProfilesButton")
    private ExtendedWebElement editProfileBtn;

    @FindBy(id = "labelTextView")
    private ExtendedWebElement profileLabel;

    @FindBy(id = "avatarImageView")
    private ExtendedWebElement avatarImages;

    @FindBy(id = "doneButton")
    private ExtendedWebElement editProfileDoneBtn;

    @FindBy(id = "profileStandardButton")
    private ExtendedWebElement addProfileContinueButton;

    @FindBy(id = "shelfTitle")
    private ExtendedWebElement iconCategoryTitles;

    @FindBy(id = "backgroundVideoOption")
    private ExtendedWebElement backgroundVideoOption;

    @FindBy(xpath = "//*[@content-desc='Add Profile']")
    private ExtendedWebElement addProfileBtn;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/rootView']")
    private ExtendedWebElement rootView;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/recyclerView']/*[%d]/*[2]/*/*")
    private ExtendedWebElement avatarIcons;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/recyclerView']/*[%d]/*[2]/*")
    private ExtendedWebElement avatarIconsShelf;

    @FindBy(id = "profileOnOffContainer")
    private ExtendedWebElement autoPlayToggle;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/profileOnOffContainer']")
    private ExtendedWebElement profileOnOffContainer;

    @FindBy(id = "profileOnOffStatusText")
    private ExtendedWebElement autoPlayToggleState;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/caretContainer']")
    private ExtendedWebElement caretContainer;

    @FindBy(id = "languageName")
    private ExtendedWebElement languageNames;

    @FindBy(id = "profileIconOption")
    private ExtendedWebElement profileIconOption;

    @FindBy(id = "rootView")
    private ExtendedWebElement languageNameLinearLayout;

    @FindBy(id = "profileNameText")
    private ExtendedWebElement profileName;

    @FindBy(id = "profileImageView")
    private ExtendedWebElement profileImageIconSelection;

    @FindBy(id = "deleteButton")
    private ExtendedWebElement deleteProfileBtn;

    @FindBy(id = "skipButton")
    private ExtendedWebElement skipBtn;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/rootView'][%d]/*[@resource-id='com.disney.disneyplus:id/languageName']")
    private ExtendedWebElement languageBasedOnFocus;

    @Override
    public boolean isOpened() {
        boolean isPresent = profilePageTitle.isElementPresent() && editProfileBtn.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void clickEditProfiles() {
        editProfileBtn.click();
    }

    public boolean isDefaultProfileFocused(String defaultProfileName) {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(contentDescContains.format(defaultProfileName));
    }

    public void selectDefaultProfile() {
        //This will always be Profile
        isDefaultProfileFocused("Profile");
        androidTVUtils.pressSelect();
    }

    public List<String> getAllProfileNames() {
        List<ExtendedWebElement> list = findExtendedWebElements(profileLabel.getBy());
        UniversalUtils.captureAndUpload(getCastedDriver());
        return list.stream().
                map(ExtendedWebElement::getText).
                collect(Collectors.toList());
    }

    public int getNumberOfImages() {
        List<ExtendedWebElement> list = findExtendedWebElements(avatarImages.getBy());
        return list.size();
    }

    public String getProfileScreenTitle() {
        String text = profilePageTitle.getText();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public List<String> getAllIconsContentDesc(int index) {
        List<ExtendedWebElement> list = findExtendedWebElements(avatarIconsShelf.format(index).getBy());
        UniversalUtils.captureAndUpload(getCastedDriver());
        return list.stream()
                .map(androidTVUtils::getContentDescription)
                .collect(Collectors.toList());
    }

    public boolean isAddProfilePresent() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return addProfileBtn.isPresent(DELAY);
    }

    public boolean isProfileEditTextFieldPresent() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return editTextField.isPresent(DELAY);
    }

    public void selectAddProfile() {
        androidTVUtils.pressRight();
        androidTVUtils.pressSelect();
    }

    public boolean inputProfileName(String profileName, boolean clear) {
        AndroidUtilsExtended androidUtilsExtended = new AndroidUtilsExtended();
        UniversalUtils.captureAndUpload(getCastedDriver());
        editTextField.click();
        boolean isOpened = androidUtilsExtended.isKeyboardShown();
        androidTVUtils.hideKeyboardIfPresent();
        if (clear) {
            DisneyPlusCommonPageBase.fluentWait(getCastedDriver(), DELAY, ONE_SEC_TIMEOUT, "Keyboard should be dismissed")
                    .until(it -> {
                        editTextField.isElementPresent();
                        return true;
                    });
            editTextField.getElement().clear();
        }
        androidTVUtils.sendInput(profileName);
        androidTVUtils.hideKeyboardIfPresent();
        return isOpened;
    }

    public void selectGender(SoftAssert sa, Gender g) {
        NavHelper nav = new NavHelper(getCastedDriver());
        nav.waitUntilTrue(() -> findExtendedWebElements(genderId.getBy()).get(g.ordinal()).isVisible(), 15, 1);
        sa.assertTrue(findExtendedWebElements(genderId.getBy()).get(g.ordinal()).isVisible(), "Gender item should be visible.");

        nav.keyUntilElementFocused(() -> findExtendedWebElements(genderId.getBy()).get(g.ordinal()), AndroidKey.DPAD_DOWN);
        UniversalUtils.captureAndUpload(getCastedDriver());
        nav.press(AndroidKey.ENTER);
    }

    /**
     * If enable is true then set kids mode on. Otherwise, set kids mode off.
     * @param enable - Name of the nav item you want to go to.
     */
    public void selectKidsMode(SoftAssert sa, boolean enable) {
        NavHelper nav = new NavHelper(getCastedDriver());
        nav.waitUntilTrue(() -> findExtendedWebElement(kidsModePageTitle.getBy()).isVisible(), 15, 1);
        sa.assertTrue(findExtendedWebElement(kidsModePageTitle.getBy()).isVisible(), "Kids mode selection page should be visible.");

        By target = enable ? kidsModeOnButton.getBy() : kidsModeOffButton.getBy();
        nav.keyUntilElementFocused(() -> findExtendedWebElement(target), AndroidKey.DPAD_DOWN);

        UniversalUtils.captureAndUpload(getCastedDriver());
        nav.press(AndroidKey.ENTER);
    }

    public void clearProfileNameInputField() {
        editTextField.getElement().clear();
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    // These getters are used for the Edit Profile page
    public ExtendedWebElement getEditProfileNameOption() {
        return findExtendedWebElements(caretContainer.getBy()).get(0);
    }

    public ExtendedWebElement getEditProfileIconOption() {
        return findExtendedWebElements(caretContainer.getBy()).get(1);
    }

    public ExtendedWebElement getEditProfileLanguageOption() {
        return findExtendedWebElements(caretContainer.getBy()).get(2);
    }

    public ExtendedWebElement getParentalControlOption() {
        return findExtendedWebElements(caretContainer.getBy()).get(3);
    }

    // These getters are used for the Update Profile page
    public ExtendedWebElement getUpdateBirthdateOption() {
        return findExtendedWebElements(caretContainer.getBy()).get(2);
    }

    public ExtendedWebElement getUpdateGenderOption() {
        return findExtendedWebElements(caretContainer.getBy()).get(3);
    }
   public boolean isEditProfileOpen() {
        boolean isPresent = getEditProfileNameOption().isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isEditProfileNameOpen() {
        boolean isPresent = editProfileProfileNameField.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public ExtendedWebElement getBackgroundVideoOption() {
        return findExtendedWebElements(profileOnOffContainer.getBy()).get(0);
    }

    public String inputProfileErrorText() {
        String text = errorTextView.getText();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public String getInputProfileNameGhost() {
        return textViewEditText.getText();
    }

    public String getInputProfileName() {
        return editTextField.getText();
    }

    public ExtendedWebElement getEditProfileDoneBtn() {
        return editProfileDoneBtn;
    }

    public void selectEditProfileDoneBtn() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        editProfileDoneBtn.click();
    }

    public void selectProfileNameContinueButton() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        addProfileContinueButton.click();
    }

    public boolean isDoneBtnPresent() {
        return editProfileDoneBtn.isElementPresent();
    }

    public void clickAutoPlay() {
        autoPlayToggle.click();
    }

    public ExtendedWebElement getAutoPlayToggle() {
        return autoPlayToggle;
    }

    public String getAutoPlayState() {
        String text = autoPlayToggleState.getText();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public void toggleKidsMode() {
        autoPlayToggle.click();
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public String getSkipBtnText() {
        return String.valueOf(getTextViewInElement(standardButton));
    }

    public boolean isSkipBtnFocused() {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), 30, 2, "Skip button not focused")
                .until(it -> androidTVUtils.isFocused(skipBtn));
        UniversalUtils.captureAndUpload(getCastedDriver());
        return true;
    }

    public boolean isSkipBtnPresent() {
        boolean isPresent = skipBtn.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void selectProfile(String profileName) {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), 30, 2, "Target profile not found.")
                .until(it -> genericTextElementExact.format(profileName).isVisible());
        genericTextElementExact.format(profileName).click();
    }

    public void selectProfileWithRemote(String profileName) {
        NavHelper navHelper = new NavHelper(getCastedDriver());
        DisneyPlusCommonPageBase.fluentWait(getDriver(), 30, 2, "Target profile not found.")
                .until(it -> genericTextElementExact.format(profileName).isVisible());
        navHelper.keyUntilElementFocused(() -> contentDescContains.format(profileName), AndroidKey.DPAD_RIGHT);
        selectFocusedElement();
    }

    public enum ProfileItems {
        PROFILE_SELECTION_TITLE("choose_profile_title"),
        EDIT_PROFILE_BTN("edit_profile_title"),
        CREATE_PROFILE("create_profile_add_profile"),
        CHOOSE_ICON_TITLE("chooseprofileicon_title"),
        EDIT_PROFILES_TITLE("edit_profile_title"),
        EDIT_PROFILE_TITLE("edit_profile_title_2"),
        PROFILE_NAME_GHOST("profile_name_placeholder"),
        CHOOSE_ICON_SKIP_BTN("btn_chooseprofileicon_skip"),
        DUPLICATE_PROFILE_NAME_ERROR("error_duplicate_profile_name"),
        EMPTY_PROFILE_NAME_ERROR("empty_profile_name_error"),
        AUTO_PLAY_EDIT_PROFILE("create_profile_autoplay"),
        TOGGLE_ON("toggle_on"),
        TOGGLE_OFF("toggle_off"),
        SUPPORTED_LANGUAGE_CODES("$..supportedUiLanguages[*]"),
        SUPPORTED_LANGUAGE_NAME("$..[?(@.language == '%s')].format.name.primary"),
        DELETE_BTN("btn_delete"),
        DELETE_PROFILE_TITLE("delete_profile_title"),
        DELETE_PROFILE_SUB_TEXT("delete_profile_copy"),
        KIDS_PROFILE("kidsprofile"),
        CANCEL_BTN("cancel"),
        SET_ID_QUERY("$..setId"),
        REF_ID_QUERY("$..refId");

        String dictionaryKey;

        ProfileItems(String dictionaryKey) {
            this.dictionaryKey = dictionaryKey;
        }

        public static final List<String> deleteProfileTexts = Stream.of(DELETE_PROFILE_TITLE.getText(), DELETE_PROFILE_SUB_TEXT.getText(),
                DELETE_BTN.getText(), CANCEL_BTN.getText()).collect(Collectors.toList());

        public static final List<String> profileSelection = Stream.of(PROFILE_SELECTION_TITLE.getText(), EDIT_PROFILE_BTN.getText(),
                CREATE_PROFILE.getText()).collect(Collectors.toList());

        public String getText() {
            return dictionaryKey;
        }
    }

    public DisneyPlusAndroidTVProfilePageBase(WebDriver driver) {
        super(driver);
        androidTVUtils = new AndroidTVUtils(getDriver());
    }

    public List<String> profileSelectionTexts() {
        List<ExtendedWebElement> list = findExtendedWebElements(profileLabel.getBy());
        return Stream.of(profilePageTitle.getText(), standardButton.getText(),
                list.get(1).getText()).collect(Collectors.toList());
    }

    public String getProfileImageContentDesc() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.getContentDescription(profileImage);
    }

    public void navigateAvatars(DisneySearchApi searchApi, String language, DisneyAccount account, SoftAssert sa) {
        List<ContentSet> allAvatarSets = searchApi.getAllSetsInAvatarCollection(account, account.getCountryCode(), language);

        for (int i=1; i<allAvatarSets.size(); i++) {
            ContentSet avatarRowData = allAvatarSets.get(i);
            pressDown(1);
            UniversalUtils.captureAndUpload(getCastedDriver());
            if (i != 1) {
                pressLeft(5);
                UniversalUtils.captureAndUpload(getCastedDriver());
            }

            List<ExtendedWebElement> iconCategories = findExtendedWebElements(iconCategoryTitles.getBy());
            for (ExtendedWebElement element : iconCategories) {
                if (element.getText().equals(avatarRowData.getSetName())) {
                    sa.assertEquals(element.getText(),avatarRowData.getSetName());
                }
            }

            int numOfIconsInRow =  avatarRowData.getNumberOfItems();
            List<String> avatarNames = avatarRowData.getTitles();
            pressRight(numOfIconsInRow - 1);
            UniversalUtils.captureAndUpload(getCastedDriver());
            List<ExtendedWebElement> shelfItem = findExtendedWebElements(mediaPoster.getBy());
            int focusValue;

            // If not on the last row
            if (i != allAvatarSets.size() - 1) {
                focusValue = numOfIconsInRow < 7 ? numOfIconsInRow - 1 : 6;
            } else {
                focusValue = shelfItem.size() - 1;
            }

            String shelfDesc = androidTVUtils.getContentDescription(shelfItem.get(focusValue));

            sa.assertTrue(androidTVUtils.isFocused(shelfItem.get(focusValue)),
                    String.format("Expected focus to be on Avatar Icon: %s in %s category", avatarNames.get(avatarNames.size() - 1), allAvatarSets.get(i).getSetName()));
            sa.assertTrue(shelfDesc.contains(avatarNames.get(avatarNames.size() - 1)),
                    String.format("Expected %s to be last Avatar Icon in %s category but shelf description was %s", avatarNames.get(avatarNames.size() - 1), allAvatarSets.get(i).getSetName(), shelfDesc));
        }
    }

    public List<String> getAllAppLanguages() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return findExtendedWebElements(languageNames.getBy())
                .stream()
                .map(ExtendedWebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isLanguageFocused(int index) {
        return androidTVUtils.isFocused(findExtendedWebElements(languageNameLinearLayout.getBy()).get(index));
    }

    public void editProfile(int profileNumber) {
        pressDownAndSelect();
        //Active Profile is 0
        isOpened();
        if (profileNumber != 0)
            pressRight(profileNumber);
        selectFocusedElement();
    }

    public void selectDeleteProfile(SoftAssert sa) {
        androidTVUtils.pressRight();
        sa.assertTrue(androidTVUtils.isElementFocused(editProfileDoneBtn), "Done button should be focused");
        androidTVUtils.pressDown();
        sa.assertTrue(androidTVUtils.isElementFocused(deleteProfileBtn), "Delete button should be focused");
        androidTVUtils.pressSelect();
    }

    public void deleteProfileCancelBtnClick() {
        tierTwoSecondButton.click();
    }

    public void selectFirstProfileIcon() {
        pressDownAndSelect();
    }

    public void selectOptionFromEditProfile(ExtendedWebElement ele) {
        focusOptionEditProfile(ele);
        selectFocusedElement();
    }

    public void focusOptionEditProfile(ExtendedWebElement element) {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), (long) LONG_TIMEOUT * 2, ONE_SEC_TIMEOUT,
                "Unable to focus element " + element.getName()).until(it -> {
            if (androidTVUtils.isElementFocused(element)) {
                UniversalUtils.captureAndUpload(getCastedDriver());
                return true;
            } else {
                pressTabToMoveToTheNextField();
                UniversalUtils.captureAndUpload(getCastedDriver());
                return false;
            }
        });
    }

    public boolean isIconSelectionFromEditProfileOpened() {
        boolean isPresent = profileImageIconSelection.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public String getIconSelectionProfileName() {
        return profileName.getText();
    }

    public BufferedImage getCurrentIconOnIconSelection() {
        return new UniversalUtils().getElementImage(profileImageIconSelection);
    }

    public boolean isFirstIconFocused() {
        boolean isFocused = androidTVUtils.isElementFocused(findExtendedWebElements(mediaPoster.getBy()).get(0));
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isFocused;
    }

    public void navigateAvatarsAndVerifyFocus(List<ContentSet> allAvatarSets, SoftAssert sa, String... avatars) {
        IntStream.range(1, allAvatarSets.size()).forEach(i -> {
            if (i != 1) {
                pressDown(1);
                pressLeft(5);
                UniversalUtils.captureAndUpload(getCastedDriver());
            }

            List<ExtendedWebElement> iconCategories = findExtendedWebElements(iconCategoryTitles.getBy());
            for (ExtendedWebElement element : iconCategories) {
                if (element.getText().equals(allAvatarSets.get(i).getSetName())) {
                    sa.assertEquals(element.getText(), allAvatarSets.get(i).getSetName());
                }
            }

            var iconsPerCategory = allAvatarSets.get(i).getNumberOfItems();
            var totalIcons =  allAvatarSets.get(i).getHits();
            List<String> avatarNames = allAvatarSets.get(i).getTitles();
            LOGGER.info(String.format("Avatar set: %s icons hits: %s total: %s",  allAvatarSets.get(i).getSetName(), iconsPerCategory, totalIcons));
            List<String> avatarsNotPresentList = Arrays.stream(avatars).collect(Collectors.toList());

            var finalIconsAmount = removeDuplicateAvatars(new AtomicInteger(iconsPerCategory), avatarNames, avatarsNotPresentList);
            LOGGER.info(String.format("Final Icon Count: %s", finalIconsAmount));

            int shelfIndex = i == 1 ? 1 : 2;
            verifyFocusedAvatar(sa, finalIconsAmount, shelfIndex, totalIcons, avatarsNotPresentList);

            UniversalUtils.captureAndUpload(getCastedDriver());
        });
    }

    private void verifyFocusedAvatar(SoftAssert sa, int finalIconsAmount, int shelfIndex, int totalIcons, List<String> avatarsNotPresentList) {
        IntStream.range(0, finalIconsAmount).forEach(j -> {
            List<ExtendedWebElement> list = findExtendedWebElements(avatarIconsShelf.format(shelfIndex).getBy());

            if (j < 7) {
                sa.assertTrue(androidTVUtils.isFocused(list.get(j)), String.format("Avatar at index %s not focused.", j));
                verifyListDoesNotContainAvatar(sa, avatarsNotPresentList, androidTVUtils.getContentDescription(list.get(j)));
            } else if (j <= finalIconsAmount - 2) {
                sa.assertTrue(androidTVUtils.isFocused(list.get(list.size() - 2)), String.format("Avatar at index %s not focused", j));
                verifyListDoesNotContainAvatar(sa, avatarsNotPresentList, androidTVUtils.getContentDescription(list.get(list.size() - 2)));
            } else {
                sa.assertTrue(androidTVUtils.isFocused(list.get(list.size() - 1)), String.format("Avatar at index %s not focused", j));
                verifyListDoesNotContainAvatar(sa, avatarsNotPresentList, androidTVUtils.getContentDescription(list.get(list.size() - 1)));
            }
            pressRight(1);
        });
    }

    public int removeDuplicateAvatars(AtomicInteger iconsPerCategory, List<String> avatarNames, List<String> avatarsNotPresentList) {
        avatarsNotPresentList.forEach(item -> {
            if (avatarNames.contains(item))
                iconsPerCategory.addAndGet(-1);
        });
        return iconsPerCategory.intValue();
    }

    public void verifyListDoesNotContainAvatar(SoftAssert sa, List<String> listToCheck, String stringToCheck) {
        List<String> listContains = listToCheck
                .stream()
                .filter(stringToCheck::contains)
                .collect(Collectors.toList());
        sa.assertTrue(listContains.isEmpty(), "Avatar found.. Avatar isn't supposed to be present as it's already selected");
    }

    public boolean isDeleteProfileBtnPresentOnEditProfile() {
        boolean isPresent = deleteProfileBtn.isElementPresent(DELAY);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isDeleteBtnFocused() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(tierTwoButtonOne);
    }

    public boolean isDeleteProfileOpened() {
        boolean isPresent = tierTwoTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public List<String> getDeleteProfileTexts() {
        return Stream.of(tierTwoTitle.getText(), tierTwoSubtitle.getText(), tierTwoButtonOne.getText(), tierTwoSecondButton.getText())
                .collect(Collectors.toList());
    }

    public void clickDeleteProfile() {
        deleteProfileBtn.click();
    }

    public boolean isLanguageScreenOpen() {
        boolean isPresent = languageNames.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void focusFirstLanguage(String language) {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, 0,
                "Unable to focus first language option: " + language).until(it -> {
            androidTVUtils.pressUp();
            List<ExtendedWebElement> list = findExtendedWebElements(rootView.getBy());
            if (androidTVUtils.isFocused(list.get(0))) {
                return findExtendedWebElements(languageNames.getBy()).get(0).getText().equals(language);
            }
            return false;
        });
    }

    public String getFocusedLanguageText() {
        List<ExtendedWebElement> list = findExtendedWebElements(rootView.getBy());
        for (int i = 0; i < list.size(); i++) {
            if (androidTVUtils.isFocused(list.get(i))) {
                ExtendedWebElement element = languageBasedOnFocus.format(i + 1);
                UniversalUtils.captureAndUpload(getCastedDriver());
                return element.getText();
            }
        }
        throw new FailedToFocusElementException("No focus found on profile language options screen");
    }

    public void selectDefaultProfileAfterFocused() {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT, "Unable to focus default profile")
                .until(it -> isDefaultProfileFocused("Test"));
        selectFocusedElement();
    }

    public boolean isParentalScreenOpen() {
        return contentTitle.isElementPresent();
    }

    public void selectOkayOnTravelingScreenIfPresent(){
        if (tierTwoTitle.isElementPresent()){
            selectFocusedElement();
        }
    }
}

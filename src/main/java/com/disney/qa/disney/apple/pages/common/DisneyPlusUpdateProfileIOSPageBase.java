package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.LEARN_MORE_CONTENT_RATINGS_LINK_1_TEXT;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusUpdateProfileIOSPageBase extends DisneyPlusEditProfileIOSPageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "editProfile")
    private ExtendedWebElement editProfile;

    @ExtendedFindBy(accessibilityId = "submitButtonCellIdentifier")
    private ExtendedWebElement saveButton;

    @ExtendedFindBy(accessibilityId = "genderFormButtonCellIdentifier")
    private ExtendedWebElement genderFormButtonCellIdentifier;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$name='%s'$]/XCUIElementTypeCollectionView")
    private ExtendedWebElement contentRatingContainer;

    private String updateProfileTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMPLETE_PROFILE_TITLE.getText());
    private String completeProfileDescription = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMPLETE_PROFILE_DESCRIPTION.getText());

    //FUNCTIONS
    public DisneyPlusUpdateProfileIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {return doesUpdateProfileTitleExist(); }

    public boolean isEditProfilePresent() { return editProfile.isElementPresent(); }


    public boolean doesUpdateProfileTitleExist() {
        return staticTextByLabel.format(updateProfileTitle).isPresent(FIVE_SEC_TIMEOUT);
    }

    public boolean isCompleteProfileDescriptionPresent() {
        return textViewByLabel.format(completeProfileDescription).isPresent();
    }

    public ExtendedWebElement getSaveBtn() {
        return saveButton;
    }

    public void tapSaveButton(){
        saveButton.click();
    }

    public boolean isLearnMoreLinkTextPresent() {
        return getStaticTextViewValueContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                LEARN_MORE_CONTENT_RATINGS_LINK_1_TEXT.getText())).isPresent();
    }

    public ExtendedWebElement getContentRatingContainer() {
        String chooseContentRating = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.CHOOSE_CONTENT_RATING.getText());
        return contentRatingContainer.format(chooseContentRating);
    }

    public ExtendedWebElement getUpdateProfileTitleExist() {
        return staticTextByLabel.format(updateProfileTitle);
    }
}

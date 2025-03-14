package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVAddProfilePage extends DisneyPlusAddProfileIOSPageBase {

    @ExtendedFindBy(accessibilityId = "skipAvatarSelectionBarButton")
    private ExtendedWebElement skipAvatarSelectionBtn;

    ExtendedWebElement enterProfileNameTitle = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.APPLICATION, ADD_PROFILE_ENTER_PROFILE_NAME_TITLE.getText()));
    ExtendedWebElement enterProfileNameContinueButton = getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_ADD_PROFILE_ENTER_NAME_CONTINUE.getText()));
    ExtendedWebElement enterYourBirthdateTitle = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.APPLICATION, ADD_PROFILE_DATE_OF_BIRTH_TITLE.getText()));
    ExtendedWebElement enterDateOfBirthContinueButton = getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_ADD_PROFILE_DATE_OF_BIRTH_CONTINUE.getText()));
    ExtendedWebElement selectGenderTitle = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.APPLICATION, ADD_PROFILE_SELECT_GENDER_TITLE.getText()));

    public DisneyPlusAppleTVAddProfilePage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getEnterProfileNameTitle() {
        return enterProfileNameTitle;
    }

    public ExtendedWebElement getEnterProfileNameContinueButton() {
        return enterProfileNameContinueButton;
    }

    public ExtendedWebElement getEnterYourBirthdateTitle() {
        return enterYourBirthdateTitle;
    }

    public ExtendedWebElement getEnterDateOfBirthContinueButton() {
        return enterDateOfBirthContinueButton;
    }

    public ExtendedWebElement getSelectGenderTitle() {
        return selectGenderTitle;
    }

    public void clickSelectAvatarSkipBtn() { skipAvatarSelectionBtn.click(); }

    @Override
    public void enterDOB(DateHelper.Month month, String day, String year) {
        String monthNumber = month.getNum();
        String fullDate = String.join("", monthNumber, day, year);

        if (fullDate.length() != 8) {
            throw new InvalidArgumentException("Given Birthdate was invalid");
        }
        for (char number : fullDate.toCharArray()) {
            dynamicBtnFindByLabel.format(number).click();
        }
    }
}

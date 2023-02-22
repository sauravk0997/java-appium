package com.disney.qa.disney.android.pages.tv.pages;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVWelchPageBase extends DisneyPlusAndroidTVCommonPage {

    @FindBy(id = "setMaturityRatingIntroDescription")
    private ExtendedWebElement welchMainDescription;

    @FindBy(id = "setMaturityRatingIntroContinueButton")
    private ExtendedWebElement welchMainContinueBtn;

    @FindBy(id = "setMaturityRatingHeader")
    private ExtendedWebElement welchFullCatalogTitle;

    @FindBy(id = "setMaturityRatingContinueButton")
    private ExtendedWebElement maturityRatingContinueBtn;

    @FindBy(id = "setMaturityRatingNotNowButton")
    private ExtendedWebElement notNowMaturityRatingBtn;

    @FindBy(id = "maturityRatingConfirmationHeader")
    private ExtendedWebElement maturityRatingConfirmationText;

    @FindBy(id = "maturityRatingConfirmationButton")
    private ExtendedWebElement maturityRatingConfirmationGotItBtn;

    @FindBy(id = "confirmPasswordTitle")
    private ExtendedWebElement confirmPassScreenTitle;

    @FindBy(id = "welcomeToStarTitle")
    private ExtendedWebElement welcomeToStarTitle;

    @FindBy(id = "createProfilePinCta")
    private ExtendedWebElement createProfilePinCta;

    @FindBy(id = "createYourPinTitle")
    private ExtendedWebElement createYourPinTitle;

    @FindBy(id = "setProfilePinCta")
    private ExtendedWebElement setProfilePinCta;

    @FindBy(id = "maturityRatingTitle")
    private ExtendedWebElement maturityRatingTitle;

    @FindBy(id = "messageText")
    private ExtendedWebElement pinMessage;

    @FindBy(id = "pinCodeErrorTextView")
    private ExtendedWebElement pinCodeError;

    @FindBy(id = "addProfileMaturityRatingHeader")
    private ExtendedWebElement accessFullCatalogFromAddProfileText;

    @FindBy(id = "secureYourProfile")
    private ExtendedWebElement secureYourProfileTitle;


    public DisneyPlusAndroidTVWelchPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = welchMainDescription.isElementPresent(LONG_TIMEOUT);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isFullCatalogScreenOpened() {
        return welchFullCatalogTitle.isElementPresent();
    }

    public boolean isMaturityRatingConfirmationScreenOpen() {
        return maturityRatingConfirmationText.isElementPresent();
    }

    public boolean isConfirmPasswordScreenOpened() {
        return confirmPassScreenTitle.isElementPresent();
    }

    public boolean isWelcomeToStarScreenOpened() {
        return welcomeToStarTitle.isElementPresent();
    }

    public boolean isMaturityRatingScreenOpened() {
        return maturityRatingTitle.isElementPresent();
    }

    public boolean isYouHaveNowFullAccessScreenOpened() {
        return maturityRatingContinueBtn.isElementPresent();
    }

    public boolean isCreatePinScreenOpened() {
        return createYourPinTitle.isElementPresent();
    }

    public void clickSetProfilePin() {
        setProfilePinCta.click();
    }

    public boolean isPinSetMessagePresent() {
        return pinMessage.isElementPresent();
    }

    public boolean isPinErrorMessagePresent() {
        return pinCodeError.isElementPresent();
    }

    public boolean isAccessFullCatalogScreenFromAddProfileOpen() {
        return accessFullCatalogFromAddProfileText.isElementPresent();
    }
    public boolean isSecureYourProfileTitleOpen() {
        return secureYourProfileTitle.isElementPresent();
    }
     public boolean isSkipThisStepScreenOpen() {
        return tierTwoTitle.isElementPresent();
    }

    /**
     * Runs a few steps to dismisses the Full Catalog Screen.
     * @param sa - soft assert used in test.
     */
    public void dismissFullCatalogScreen(SoftAssert sa) {
        sa.assertTrue(isAccessFullCatalogScreenFromAddProfileOpen());
        pressBackTimes(1);
        sa.assertTrue(isSkipThisStepScreenOpen());
        // Occasionally the page can open without any of the buttons focused. Pressing Select puts focus on the button.
        if (!getAndroidTVUtilsInstance().isElementFocused(tierTwoButtonOne)) {
            selectFocusedElement();
        }
        selectFocusedElement();
    }
}

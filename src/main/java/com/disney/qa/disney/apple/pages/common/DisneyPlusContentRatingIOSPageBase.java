package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusContentRatingIOSPageBase extends DisneyPlusApplePageBase {

    private static String ratingDescription = "Features titles rated %s and below.";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$type = 'XCUIElementTypeStaticText' AND label = 'Content Rating'$]/XCUIElementTypeButton")
    private ExtendedWebElement contentRatingInfoButton;

    @ExtendedFindBy(accessibilityId = "saveButton")
    protected ExtendedWebElement saveButton;

    private ExtendedWebElement contentRatingHeader = getStaticTextByLabel(
            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.MATURITY_RATING_SETTINGS_LABEL.getText()));

    private ExtendedWebElement textRecommended =  getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.PCON,
            DictionaryKeys.RECOMMENDED_RATING.getText()));

    private ExtendedWebElement contentRatingText = getStaticTextByLabel(
            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                    DictionaryKeys.CONTENT_RATING.getText()));

    public DisneyPlusContentRatingIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return contentRatingHeader.isPresent(THREE_SEC_TIMEOUT);
    }

    private ExtendedWebElement gotItButton = xpathNameOrName.format(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                    DictionaryKeys.BTN_GOT_IT.getText()), DictionaryKeys.BTN_GOT_IT.getText());

    public ExtendedWebElement getGotItButton() {
        return gotItButton;
    }

    public ExtendedWebElement getContentRatingInfoButton() {
        return contentRatingInfoButton;
    }

    public void scrollToRatingValue(String rating){
        scrollToItem((String.format(ratingDescription, rating)));
    }

    public boolean isContentRatingDisplyed(String rating){
        scrollToRatingValue(rating);
        return getStaticTextByLabel(String.format(ratingDescription, rating)).isPresent();
    }

    public boolean verifyLastContentRating(String rating){
        scrollToRatingValue(rating);
        return dynamicRowColumnContent.format(2, -1).getText().contains((String.format(ratingDescription, rating)));
    }

    public void selectContentRating(String rating){
        scrollToRatingValue(rating);
        getStaticTextByLabel(String.format(ratingDescription, rating)).click();
    }

    public void clickSaveButton(){
        saveButton.click();
    }

    public String getRecommendedRating() {
       return getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.RECOMMENDED_RATING.getText());
    }

    public ExtendedWebElement getRecommendedText() {
        return textRecommended;
    }

    public boolean isContentRatingPresent() {
        return contentRatingText.isPresent();
    }
}

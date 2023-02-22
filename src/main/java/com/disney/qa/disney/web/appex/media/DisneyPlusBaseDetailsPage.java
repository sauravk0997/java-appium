package com.disney.qa.disney.web.appex.media;

import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusBaseDetailsPage extends DisneyPlusBaseTilesPage {

    @FindBy(xpath = "//*[@data-testid='play-button']")
    private ExtendedWebElement detailsPagePlayBtn;

    @FindBy(xpath = "//*[@data-testid='play-trailer-button']")
    private ExtendedWebElement trailerBtn;

    @FindBy(xpath = "//*[@data-gv2key='extras']")
    private ExtendedWebElement extrasBtn;

    @FindBy(xpath = "//*[contains(@aria-label,'%s')]")
    private ExtendedWebElement dyanmicContentName;

    @FindBy(xpath = "//img[@alt='%s']")
    private ExtendedWebElement dynamicLogoName;

    @FindBy(xpath = "//*[@data-testid='details-button']")
    private ExtendedWebElement detailsButton;

    /*
     * Button appears upon episode conclusion
     * Condition: Autoplay turned off in settings
     */
    @FindBy(xpath = "//*[@aria-label='play']")
    private ExtendedWebElement playNextEpisodeBtn;

    /*
     * Button appears upon episode conclusion
     * Condition: Autoplay turned off in settings
     */
    //Button appears upon episode conclusion
    @FindBy(id = "SEE ALL EPISODES")
    private ExtendedWebElement seeAllEpisodesBtn;

    public ExtendedWebElement getDetailsButton(){
        return detailsButton;
    }

    //Click Elements

    public boolean isDetailsBtnPresent() {
        return detailsButton.isElementPresent();
    }

    public DisneyPlusBaseDetailsPage clickDetailsPagePlayBtn() {
        if(detailsPagePlayBtn.isClickable()){
            PAGEFACTORY_LOGGER.info("Play button is present");
            waitUntil(ExpectedConditions.elementToBeSelected(detailsPagePlayBtn.getElement()), 10);
            detailsPagePlayBtn.click();
        }
        return this;
    }

    public void clickTrailerBtn() {
        trailerBtn.click();
    }

    public boolean isPlayNextEpisodeBtnPresent(int timeout) {
        return playNextEpisodeBtn.isElementPresent(timeout);
    }

    public void clickPlayNextEpisodeBtn() {
        playNextEpisodeBtn.click();
    }

    public void clickSeeAllEpisodesBtn() {
        seeAllEpisodesBtn.click();
    }

    //State Booleans

    public boolean isDetailsPagePlayBtnPresent() {
        return detailsPagePlayBtn.isElementPresent();
    }

    public boolean isDetailsPagePlayBtnOrTrailerBtnPresent(){
        if(getCurrentEnvironment().equalsIgnoreCase("QA") && detailsPagePlayBtn.isElementNotPresent(5)){
            return getDetailsButton().isElementPresent();
        }
        else {
            return detailsPagePlayBtn.isElementPresent() || trailerBtn.isElementPresent();
        }
    }

    public boolean isTrailerBtnPresent() {
        return trailerBtn.isElementPresent();
    }

    public void isDetailsPagePlayBtnPresent(JsonNode dictionary){
        getDictionaryElement(dictionary, DisneyWebKeys.MOVIE_PLAY_BUTTON.getText()).isElementPresent();
    }

    public DisneyPlusBaseDetailsPage clickExtras() {
        extrasBtn.click();
        return this;
    }

    public void clickContent(String contentName) {
        dyanmicContentName.format(contentName).click();
    }

    public boolean isLogoPresent(String contentName) {
        return dynamicLogoName.format(contentName).isElementPresent();
    }

    public DisneyPlusBaseDetailsPage(WebDriver driver) {
        super(driver);
    }
}

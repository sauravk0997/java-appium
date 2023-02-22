package com.disney.qa.disney.android.pages.tv.pages;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:CallToDeprecatedMethod"})
public class DisneyPlusAndroidTVDetailsPageBase extends DisneyPlusAndroidTVCommonPage {
    public DisneyPlusAndroidTVDetailsPageBase(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "assetItemDescription")
    private ExtendedWebElement assetItemDescription;

    @FindBy(xpath = "//*[@text = 'CONTINUE']")
    private ExtendedWebElement continueBtnByText;

    @FindBy(id = "detailDescriptionTextView")
    private ExtendedWebElement detailsContWatchingDescription;

    @FindBy(id = "detailDescriptionTitleTextView")
    private ExtendedWebElement detailsContWatchingTitle;

    @FindBy(id = "detailDescriptionTextView")
    private ExtendedWebElement detailsDescription;

    // Contains Rating, A/V badge(s), Year(s), Season(s), and Genre if available
    @FindBy(id = "detailMetadataRoot")
    private ExtendedWebElement detailsMetadata;

    @FindBy(id = "detailLogoImage")
    private ExtendedWebElement detailsPageBackgroundImage;

    @FindBy(id = "detailBookmarkProgressBar")
    private ExtendedWebElement detailsProgressBar;

    @FindBy(id = "detailBookmarkTextView")
    private ExtendedWebElement detailsRemainingTime;

    @FindBy(id = "description")
    private ExtendedWebElement episodeDescription;

    @FindBy(xpath = "//*[@text = 'PLAY']")
    private ExtendedWebElement playBtnByText;

    @FindBy(id = "detailPromoLabelTitle")
    private ExtendedWebElement promoText;

    @FindBy(xpath = "//*[@text = 'RESTART']")
    private ExtendedWebElement restartBtnByText;

    @FindBy(id = "detailAllMainButton")
    private ExtendedWebElement startPlayerBtn;

    @FindBy(id = "contentDetailTitleImage")
    private ExtendedWebElement titleImage;

    @FindBy(id = "trailerButton")
    private ExtendedWebElement trailerBtn;

    @FindBy(xpath = "//*[@text = 'TRAILER']")
    private ExtendedWebElement trailerBtnByText;

    @FindBy(id = "up_next_play_btn")
    private ExtendedWebElement upNextPlayBtn;

    @FindBy(id = "watchlistButton")
    private ExtendedWebElement watchListButton;

    public void clickStartPlayerBtn() {
        startPlayerBtn.click();
    }

    public void clickTrailerBtn() {
        trailerBtn.click();
    }

    public void clickWatchlistBtn() {
        watchListButton.click();
    }

    public void dismissGroupWatchMessage(){
        if(message.isElementPresent(DELAY))
            new AndroidTVUtils(getDriver()).pressSelect();
    }

    public List<String> getAssetDescriptions() {
        List<ExtendedWebElement> list = findExtendedWebElements(episodeDescription.getBy());
        return list.stream().map(ExtendedWebElement::getText).collect(Collectors.toList());
    }

    public String getDescriptionText() {
        return detailsDescription.getText();
    }

    public String getEpisodeContWatchingDescription() {
        return detailsContWatchingDescription.getText();
    }

    public String getEpisodeContWatchingTitle() {
        return detailsContWatchingTitle.getText();
    }

    public String getMainDetailsButtonContentDesc() {
        return getAndroidTVUtilsInstance().getContentDescription(startPlayerBtn);
    }

    public String getMetadataText() {
        return detailsMetadata.getAttribute("content-desc");
    }

    public ExtendedWebElement getPlayButton() {
        return playBtnByText;
    }

    public String getPromoText() {
        return promoText.getText();
    }

    public ExtendedWebElement getTrailerButton() {
        return trailerBtnByText;
    }

    public ExtendedWebElement getDetailsBackground() {
        return detailsPageBackgroundImage;
    }

    public String getTitleImageContentDesc(){
        return getAndroidTVUtilsInstance().getContentDescription(detailsPageBackgroundImage);
    }

    public ExtendedWebElement getWatchlistButton() {
        return watchListButton;
    }

    public String getWatchlistContentDesc() {
        return getAndroidTVUtilsInstance().getContentDescription(watchListButton);
    }

    public boolean isContinueButtonPresent() {
        return continueBtnByText.isElementPresent();
    }

    public boolean isDescriptionPresent() {
        return detailsDescription.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = detailsPageBackgroundImage.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isPlayBtnPresent() {
        return startPlayerBtn.isElementPresent();
    }

    public boolean isProgressBarPresent() {
        return detailsProgressBar.isElementPresent();
    }

    public boolean isStartPlayerBtnFocused() {
        return new AndroidTVUtils(getDriver()).isElementFocused(startPlayerBtn);
    }

    public boolean isRestartButtonPresent() {
        return restartBtnByText.isElementPresent();
    }

    public boolean isUpNextScreenOpen() {
        return upNextPlayBtn.isElementPresent(ONE_SEC_TIMEOUT);
    }

    public boolean isWatchlistButtonPresent() {
        return watchListButton.isElementPresent();
    }

    public void navigateToEpisodeFromPlayBtnForSeries(int seasonNumber, int episodeNumber) {
        if (seasonNumber == 1) {
            pressDownAndSelect();
            pressDown(episodeNumber);
        } else {
            pressDownAndSelect();
            pressDown(1); // Takes you to the first season episodes
            pressLeft(1); // Takes you to the seasons tab
            getAndroidTVUtilsInstance().keyPressTimes(AndroidTVUtils::pressDown, seasonNumber - 1, 5); // Minus one here because we start at one
            pressRight(1);
            pressDown(episodeNumber - 1);  // Minus one here because we start at one
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void waitTillUpNextScreenIsGone(int timeOut, int polling) {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), timeOut, polling, "Up next screen did not close")
                .until(it -> !upNextPlayBtn.isElementPresent(ONE_SEC_TIMEOUT));
    }
}

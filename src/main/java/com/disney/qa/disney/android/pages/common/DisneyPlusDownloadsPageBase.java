package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDownloadsPageBase extends DisneyPlusCommonPageBase {

    private static final long DOWNLOAD_TIME_WAIT_TEN_MINUTES = 600_000;

    @FindBy(id = "downloadToolbarTitle")
    private ExtendedWebElement pageHeader;

    @FindBy(xpath = "//*[contains(@text, \"%s\")]/following-sibling::*")
    private ExtendedWebElement downloadText;

    @FindBy(id = "editButton")
    private ExtendedWebElement editButton;

    @FindBy(id = "downloadsItemCheckbox")
    private ExtendedWebElement downloadItemCheckbox;

    @FindBy(id = "selectAllButton")
    private ExtendedWebElement selectAllButton;

    @FindBy(id = "trashButton")
    private ExtendedWebElement trashButton;

    @FindBy(id = "emptyStateImage")
    private ExtendedWebElement emptyStateBackgroundImg;

    @FindBy(id = "emptyStateTvTitle")
    private ExtendedWebElement emptyStateTitle;

    @FindBy(id = "emptyStateTvDetails")
    private ExtendedWebElement emptyStateDetails;

    @FindBy(id = "downloadsItemThumbnail")
    private ExtendedWebElement downloadsItemThumbnail;

    @FindBy(xpath = "//*[@content-desc='%s']/following-sibling::*[contains(@resource-id, 'downloadsItemPlayButton')]")
    private ExtendedWebElement downloadAssetPlayBtn;

    @FindBy(id = "closeButton")
    private ExtendedWebElement cancelEditModeBtn;

    @FindBy(id = "descriptionTextView")
    private ExtendedWebElement cancelEditModeDescriptionText;

    @FindBy(id  = "downloadsItemDownloadStatus")
    private ExtendedWebElement downloadStatusIcon;

    @FindBy(id = "design_bottom_sheet")
    private ExtendedWebElement downloadsBottomSheet;

    @FindBy(id = "downloadableProgressLabel")
    private ExtendedWebElement downloadProgressStatus;

    @FindBy(id = "recyclerView")
    private ExtendedWebElement downloadPageRecyclerView;


    public DisneyPlusDownloadsPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return pageHeader.isElementPresent();
    }

    public String getHeaderText() {
        return pageHeader.getText();
    }

    public boolean isSingleEpisodeDownloadCorrect(String mediaTitle, String singleEpisode){
        return downloadText.format(mediaTitle).getText().contains("1" + singleEpisode);
    }

    public boolean isSeasonDownloadCorrect(String mediaTitle, String multiEpisodes){
        return downloadText.format(mediaTitle).getText().contains("20" + multiEpisodes);
    }

    public boolean isEditButtonDisplayed() {
        return editButton.isElementPresent();
    }

    public String getEditButtonText() {
        return getElementText(editButton);
    }

    public void clickEditButton() {
        editButton.click();
    }

    public void clickSelectAllButton() {
        selectAllButton.click();
    }

    public boolean isSelectAllEnabled() {
        return selectAllButton.isElementPresent() && selectAllButton.getAttribute("selected").equals("false");
    }

    public boolean isDeselectAllEnabled() {
        return selectAllButton.isElementPresent() && selectAllButton.getAttribute("selected").equals("true");
    }

    public boolean isSelectItemCheckboxDisplayed() {
        return downloadItemCheckbox.isElementPresent() && downloadItemCheckbox.getAttribute("checked").equals("false");
    }

    public boolean isDeselectItemCheckboxDisplayed() {
        return downloadItemCheckbox.isElementPresent() && downloadItemCheckbox.getAttribute("checked").equals("true");
    }

    public void clickSelectItemCheckbox() {
        downloadItemCheckbox.click();
    }

    public boolean isTrashButtonDisplayed() {
        return trashButton.isElementPresent();
    }

    public void deleteAllDownloads(){
        if(editButton.isElementPresent(SHORT_TIMEOUT)) {
            editButton.click();
            selectAllButton.click();
            trashButton.click();
        }
    }

    public String getEpisodeMediaListingMetadataRating(String seriesTitle, String episodeTitle){
        new AndroidUtilsExtended().scroll(seriesTitle, getAppRootDisplay());
        genericTextElement.format(seriesTitle).click();
        return getMetaData().format(episodeTitle).getText().split(" ")[0];
    }

    public boolean isEmptyStateBackgroundImageDisplayed() {
        return emptyStateBackgroundImg.isElementPresent();
    }

    public boolean isEmptyStateTitleDisplayed() {
        return emptyStateTitle.isElementPresent();
    }

    public String getEmptyStateTitle() {
        return getElementText(emptyStateTitle);
    }

    public boolean isEmptyStateDetailsDisplayed() {
        return emptyStateDetails.isElementPresent();
    }

    public String getEmptyStateDetails() {
        return getElementText(emptyStateDetails);
    }

    public boolean isMediaDownloadThumbnailDisplayed() {
        return downloadsItemThumbnail.isElementPresent();
    }

    public void clickMediaDownloadThumbnail() {
        downloadsItemThumbnail.click();
    }

    public boolean isDownloadsPlayButtonDisplayed(String asset) {
        return downloadAssetPlayBtn.format(asset).isElementPresent();
    }

    public boolean isCancelEditModeButtonDisplayed() {
        return cancelEditModeBtn.isElementPresent();
    }

    public void clickCancelEditModeButton() {
        cancelEditModeBtn.click();
    }

    public boolean isCancelModeDescriptionDisplayed() {
        return cancelEditModeDescriptionText.isElementPresent();
    }

    public String getCancelModeDescriptionText() {
        return cancelEditModeDescriptionText.getText();
    }

    public boolean isDownloadStatusIconPresent() {
        return downloadStatusIcon.isElementPresent();
    }

    public String getDownloadStatusText() {
        return downloadProgressStatus.getText();
    }

    public boolean waitForDownloadInProgress() {
        return fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Status did not change to 'Download in progress'").until(it -> getDownloadStatusText().contains("Download in progress"));
    }

    public void clickDownloadStatus() {
        downloadStatusIcon.click();
        if(!isBottomSheetMenuDisplayed()) {
            Assert.fail("ERROR: Sheet menu was not opened after clicking the status icon.");
        }
    }

    public void openDownloadedMedia() {
        downloadStatusIcon.click();
    }

    public boolean isBottomSheetMenuDisplayed() {
        return downloadsBottomSheet.isElementPresent();
    }

    public void waitForDownload(String title) {
        long endTime = System.currentTimeMillis() + DOWNLOAD_TIME_WAIT_TEN_MINUTES;
        String metadata;
        String progress;
        do {
            metadata = getMetaData().format(title).getText();
            LOGGER.info("Found: {}", metadata);
            pause(30);
            progress = getMetaData().format(title).getText();
        } while (!metadata.equals(progress) && System.currentTimeMillis() < endTime);
        if (System.currentTimeMillis() > endTime)
            Assert.fail("Error in downloading content " + title + " within " + DurationFormatUtils.formatDuration(DOWNLOAD_TIME_WAIT_TEN_MINUTES, "m'm'"));
    }
}

package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.CacheLookup;

import java.time.temporal.ValueRange;
import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDownloadsIOSPageBase extends DisneyPlusApplePageBase {
	public DisneyPlusDownloadsIOSPageBase(WebDriver driver) {
		super(driver);
	}

	//LOCATORS
	@ExtendedFindBy(accessibilityId = "Downloads")
	@CacheLookup
	private ExtendedWebElement downloadsHeader;

	private ExtendedWebElement editButton = getStaticTextByLabel(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT.getText()));
	private ExtendedWebElement downloadCompleteButton = getDynamicAccessibilityId(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_COMPLETE.getText()));
	private ExtendedWebElement downloadsEmptyHeader = getDynamicAccessibilityId(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_EMPTY_HEADER.getText()));
	private ExtendedWebElement uncheckedCheckbox = getDynamicAccessibilityId(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.CHECKBOX_UNCHECKED.getText()));
	private ExtendedWebElement checkedCheckbox = getDynamicAccessibilityId(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.CHECKBOX_CHECKED.getText()));
	private ExtendedWebElement resumeDownload = getDynamicAccessibilityId(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_PAUSED.getText()));
	private ExtendedWebElement stopDownload = getDynamicAccessibilityId(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_STOP.getText()));

	@ExtendedFindBy(accessibilityId = "deleteDownloadButton")
	private ExtendedWebElement deleteDownloadButton;

	@ExtendedFindBy(accessibilityId = "downloadDelete24")
	private ExtendedWebElement downloadDelete24Button;
	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"offlineContentCell[%s, " +
			"%s]\"`]/**/XCUIElementTypeStaticText[3]")
	private ExtendedWebElement episodeDescription;
	@ExtendedFindBy(accessibilityId = "offlineContentCell[%s, %s]")
	private ExtendedWebElement episodeDownloadCell;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"offlineContentCell[%s, " +
			"%s]\"`]/**/XCUIElementTypeOther[`name == \"progressBar\"`]")
	private ExtendedWebElement progressBarOnDownload;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"offlineContentCell[%s, " +
			"%s]\"`]/**/XCUIElementTypeOther[`name == \"progressBar\"`]/XCUIElementTypeOther")
	private ExtendedWebElement progressBarBookmarkPositionOnDownload;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"emptyView\"`]/XCUIElementTypeScrollView" +
			"/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeImage")
	private ExtendedWebElement emptyDownloadImage;

	//FUNCTIONS
	@Override
	public boolean isOpened() {
		return downloadsHeader.isPresent();
	}

	public ExtendedWebElement getEmptyDownloadImage() {
		return emptyDownloadImage;
	}

	public ExtendedWebElement getDownloadedAssetImage(String downloadedAsset) {
		return dynamicBtnFindByLabelContains.format("Play " + downloadedAsset);
	}

	public ExtendedWebElement getDownloadAssetFromListView(String downloadAsset) {
		return staticTextByLabel.format(downloadAsset);
	}

	public ExtendedWebElement getEditButton() {
		return editButton;
	}

	public ExtendedWebElement getDownloadCompleteButton() {
		return downloadCompleteButton;
	}

	public void waitForDownloadToStart() {
		fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT,
				"Download tab notification badge was not present")
				.until(it -> downloadsTabNotificationBadge.isPresent(ONE_SEC_TIMEOUT));
	}
	public void tapDownloadedAsset(String downloadedAsset) {
		dynamicBtnFindByLabelContains.format("Play " + downloadedAsset).click();
	}

	public boolean isDownloadHeaderPresent() {
		return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION,
				DictionaryKeys.DOWNLOADS_TITLE.getText()))
				.isPresent();
	}

	public boolean isDownloadsEmptyHeaderPresent() {
		return downloadsEmptyHeader.isPresent();
	}

	public boolean isDownloadsEmptyCopyPresent() {
		String downloadsCopy = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
				DictionaryKeys.DOWNLOADS_EMPTY_COPY.getText());
		return getDynamicAccessibilityId(downloadsCopy).isPresent();
	}
	public void clickEditButton() {
		getTypeButtonContainsLabel("Edit").click();
	}

	public void clickUncheckedCheckbox() {
		uncheckedCheckbox.click();
	}

	public ExtendedWebElement getUncheckedCheckbox() {
		return uncheckedCheckbox;
	}

	public boolean isCheckedCheckboxPresent() {
		return checkedCheckbox.isElementPresent();
	}

	public ExtendedWebElement getCheckedCheckbox() {
		return checkedCheckbox;
	}

	public void clickDeleteDownloadButton() {
		//TODO: Temp fix to accomodate for both deleteButton accessibility IDs - QAA-12365
		if (deleteDownloadButton.isPresent()) {
			deleteDownloadButton.click();
		} else {
			downloadDelete24Button.click();
		}
	}

	public ExtendedWebElement getSizeAndRuntime() {
		String[] sizeRuntimeParts = getStaticTextByLabelContains("MB").getText().split(" ");
		String size = sizeRuntimeParts[1] + " " + sizeRuntimeParts[2];
		String runtime = sizeRuntimeParts[4] + " " + sizeRuntimeParts[5];
		return getStaticTextByLabelContains(getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
				DictionaryKeys.SIZE_RUNTIME_PLACEHOLDER.getText()), Map.of("S", size, "R", runtime)));
	}

	public ExtendedWebElement getRating() {
		String[] ratingSizeRuntimeParts = getStaticTextByLabelContains("MB").getText().split(" ");
		return getStaticTextByLabelContains(ratingSizeRuntimeParts[0]);
	}

	public void clickSeriesMoreInfoButton() {
		getImageLabelContains("Double tap for more info").click();
	}

	public boolean isEpisodeNumberDisplayed(String episodeNumber) {
		return getStaticTextByLabelContains(getLocalizationUtils().formatPlaceholderString(
				getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
						DictionaryKeys.EPISODE_PLACEHOLDER.getText()),
				Map.of(PLACEHOLDER_E, Integer.parseInt(episodeNumber)))).isPresent();
	}

	public ExtendedWebElement getEpisodeDescription(String seasonNumber, String episodeNumber) {
		return episodeDescription.format(seasonNumber, episodeNumber);
	}

	public boolean isEpisodeCellDisplayed(String seasonNumber, String episodeNumber) {
		return episodeDownloadCell.format(seasonNumber, episodeNumber).isPresent();
	}

	public boolean isProgressbarBookmarkDisplayedOnDownloads(String seasonNumber, String episodeNumber) {
		return progressBarBookmarkPositionOnDownload.format(seasonNumber, episodeNumber).isPresent();
	}

	public boolean isProgressBarIndicatingCorrectPosition(
			String seasonNumber, String episodeNumber, double scrubPercentage, int latency) {
		double expectedWidth = progressBarOnDownload.format(seasonNumber, episodeNumber)
				.getSize()
				.getWidth() / (100 / scrubPercentage);
		double actualWidth = progressBarBookmarkPositionOnDownload.format(seasonNumber, episodeNumber)
				.getSize()
				.getWidth();
		ValueRange range = ValueRange.of(-latency, latency);
		return range.isValidIntValue((long) (expectedWidth - actualWidth));
  }

	public boolean isDownloadInProgressTextPresent() {
		return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION,
				DictionaryKeys.DOWNLOAD_IN_PROGRESS.getText()))
				.isPresent();
	}

	public boolean isDownloadInProgressPluralTextPresent() {
		return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION,
				DictionaryKeys.DOWNLOAD_IN_PROGRESS_PLURAL.getText()))
				.isPresent();
	}

	public ExtendedWebElement getDownloadStopIcon() {
		return stopDownload;
	}

	public ExtendedWebElement getDownloadResumeIcon() {
		return resumeDownload;
	}

	public void clickDownloadHeader() {
		downloadsHeader.click();
	}
}

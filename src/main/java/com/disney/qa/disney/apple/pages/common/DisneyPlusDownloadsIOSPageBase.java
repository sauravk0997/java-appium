package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

	private ExtendedWebElement editButton = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT.getText()));

	private ExtendedWebElement selectAllButton = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SELECT_ALL_LABEL.getText()));

	private ExtendedWebElement cancelButton = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL.getText()));

	private ExtendedWebElement downloadCompleteButton = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_COMPLETE.getText()));

	private ExtendedWebElement downloadsEmptyHeader = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_EMPTY_HEADER.getText()));

	@ExtendedFindBy(accessibilityId = "Checkbox. Checked.")
	private ExtendedWebElement checkedCheckbox;

	@ExtendedFindBy(accessibilityId = "Checkbox. Unchecked.")
	private ExtendedWebElement uncheckedCheckbox;

	@ExtendedFindBy(accessibilityId = "deleteDownloadButton")
	private ExtendedWebElement deleteDownloadButton;

	@ExtendedFindBy(accessibilityId = "downloadDelete24")
	private ExtendedWebElement downloadDelete24Button;

	//FUNCTIONS
	@Override
	public boolean isOpened() {
		return downloadsHeader.isPresent();
	}

	public void waitForDownloadToComplete() {
		waitUntil(ExpectedConditions.elementToBeClickable(downloadCompleteButton.getBy()), THREE_HUNDRED_SEC_TIMEOUT);
	}

	public void tapDownloadedAssetFromListView(String downloadedAsset) {
		staticTextByLabel.format(downloadedAsset).click();
	}

	public ExtendedWebElement getDownloadAssetFromListView(String downloadAsset) {
		return staticTextByLabel.format(downloadAsset);
	}

	public void tapDownloadedAsset(String downloadedAsset) {
		dynamicBtnFindByLabelContains.format("Play " + downloadedAsset).click();
	}

	public ExtendedWebElement getDownloadedAssetImage(String downloadedAsset) {
		return dynamicBtnFindByLabelContains.format("Play " + downloadedAsset);
	}

	public void tapDownloadedAssetText(String downloadedAsset) {
		staticTextLabelContains.format(downloadedAsset).click();
	}

	public boolean isDownloadsEmptyHeaderPresent() {
		return downloadsEmptyHeader.isPresent();
	}

	public boolean isContentHeaderPresent(String downloadedAsset) {
		return dynamicBtnFindByLabelContains.format(downloadedAsset).isElementPresent();
	}

	public ExtendedWebElement getEditButton() {
		return editButton;
	}

	public ExtendedWebElement getSelectAllButton() {
		return selectAllButton;
	}

	public ExtendedWebElement getCancelButton() {
		return cancelButton;
	}

	public ExtendedWebElement getDownloadCompleteButton() {
		return downloadCompleteButton;
	}

	public void clickEditButton() {
		getTypeButtonContainsLabel("Edit").click();
	}

	public void clickUncheckedCheckbox() {
		uncheckedCheckbox.click();
	}

	public boolean isCheckedCheckboxPresent() {
		return checkedCheckbox.isElementPresent();
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
		return getStaticTextByLabelContains(getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
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
		return getStaticTextByLabelContains(getDictionary().formatPlaceholderString(
				getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
						DictionaryKeys.EPISODE_PLACEHOLDER.getText()),
				Map.of("E", Integer.parseInt(episodeNumber)))).isPresent();
	}
}

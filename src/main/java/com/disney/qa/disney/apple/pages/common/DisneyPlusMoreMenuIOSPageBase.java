package com.disney.qa.disney.apple.pages.common;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.COMMUNICATION_SETTINGS_LINK_1_TEXT;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.disney.config.DisneyConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:CallToDeprecatedMethod"})
public class DisneyPlusMoreMenuIOSPageBase extends DisneyPlusApplePageBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	//LOCATORS
	private ExtendedWebElement editProfilesBtn = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_EDIT_PROFILE.getText()));

	@FindBy(xpath = "//XCUIElementTypeApplication[@name=\"Disney+\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther")
	private ExtendedWebElement exitJuniorModePin;
	@FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"%s\"]/preceding-sibling::*")
	private ExtendedWebElement profileAvatar;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Access %s's pin protected profile\"`]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage")
	private ExtendedWebElement pinProtectedProfileLock;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"EDIT PROFILES\"`]/preceding-sibling::*")
	private ExtendedWebElement profilesTray;

	@FindBy(xpath = "//*[contains(@name, 'Version')]")
	private ExtendedWebElement appVersion;

	@FindBy(xpath = "//XCUIElementTypeCell[@name='accountTab']//XCUIElementTypeOther[2]/*/XCUIElementTypeImage")
	private ExtendedWebElement accountUnverifiedBadge;

	private ExtendedWebElement addProfileBtn = getDynamicCellByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PROFILE.getText()));

	@ExtendedFindBy(accessibilityId = "emptyView")
	private ExtendedWebElement watchlistEmpty;

	@FindBy(xpath = "//*[@name='accountView']/XCUIElementTypeCollectionView/XCUIElementTypeCell[%s]")
	private ExtendedWebElement moreMenuItemByIndex;

	//HELP WEBVIEW
	@ExtendedFindBy(accessibilityId = "TopBrowserBar")
	private ExtendedWebElement webviewBrowserBar;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Done\"`]")
	protected ExtendedWebElement webviewDoneBtn;

	@ExtendedFindBy(accessibilityId = "exitKidsProfileButton")
	private ExtendedWebElement exitKidsProfileButton;

	@ExtendedFindBy(accessibilityId = "accountTab")
	private ExtendedWebElement accountTab;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$type='XCUIElementTypeStaticText' AND label='%s'$]")
	private ExtendedWebElement downloadOverWifiOnly;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$type='XCUIElementTypeStaticText' AND label='%s'$]")
	private ExtendedWebElement deleteAllDownloadsCell;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[$type='XCUIElementTypeStaticText' AND label CONTAINS '%s'$]")
	private ExtendedWebElement deleteOneDownload;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"accountView\"`]/XCUIElementTypeOther[2]/XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeCollectionView")
	private ExtendedWebElement profileSelectionCollectionView;

	private ExtendedWebElement deleteAccountButton = getDynamicAccessibilityId(getDictionary()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
					COMMUNICATION_SETTINGS_LINK_1_TEXT.getText()));


	public ExtendedWebElement getExitKidsProfile() {
		return exitKidsProfileButton;
	}

	public ExtendedWebElement getExitJuniorModePin() {
		return exitJuniorModePin;
	}

	public enum MoreMenu {
		WATCHLIST(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_WATCHLIST.getText()), 1),
		APP_SETTINGS(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText()), 2),
		ACCOUNT(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_ACCOUNT.getText()), 3),
		LEGAL(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText()), 4),
		HELP(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_HELP.getText()), 5),
		LOG_OUT(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.LOGOUT_BTN.getText()), 6);

		private final String menuOption;
		private final int index;

		MoreMenu(String menuOption, int index) {
			this.menuOption = menuOption;
			this.index = index;
		}

		public String getMenuOption() {
			return menuOption;
		}

		public int getIndex() {
			return index;
		}
	}

	//FUNCTIONS

	public DisneyPlusMoreMenuIOSPageBase(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean isOpened() {
		return editProfilesBtn.isPresent();
	}

	public By getEditProfilesBtnBy() {
		return editProfilesBtn.getBy();
	}

	public ExtendedWebElement getProfileAvatar(String profile) {
		return profileAvatar.format(profile);
	}

	public boolean isMenuOptionPresent(MoreMenu option) {
		return getDynamicCellByLabel(option.getMenuOption()).isElementPresent();
	}

	public boolean isMenuOptionNotPresent(MoreMenu option) {
		return getDynamicCellByLabel(option.getMenuOption()).isElementNotPresent(THREE_SEC_TIMEOUT);
	}

	public void clickMenuOption(MoreMenu option) {
		try {
			getDynamicCellByLabel(option.getMenuOption()).click();
		} catch (NoSuchElementException e) {
			LOGGER.debug("ElementTypeCell located by Label Equals value not found. Falling back to Xpath");
			getDynamicXpathContainsName(option.getMenuOption()).click();
		}
	}

	public void  clickMenuOptionByIndex(MoreMenu option) {
		moreMenuItemByIndex.format(option.getIndex()).click();
	}

	public ExtendedWebElement getDeleteAccountButton() {
		return deleteAccountButton;
	}

	public boolean isEditProfilesBtnPresent() {
		return editProfilesBtn.isElementPresent();
	}

	public DisneyPlusEditProfileIOSPageBase clickEditProfilesBtn() {
		clickElementAtLocation(editProfilesBtn, 50, 50);
		return initPage(DisneyPlusEditProfileIOSPageBase.class);
	}

	public ExtendedWebElement getProfileCell(String profile, boolean secured) {
		if(secured) {
			return getDynamicCellByLabel(getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), Map.of(USER_PROFILE, profile)));
		} else {
			return getDynamicCellByLabel(getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, profile)));
		}
	}

	public boolean isProfileSwitchDisplayed(String profileName) {
		return getProfileCell(profileName, false).isElementPresent();
	}

	public boolean isSecureProfileSwitchDisplayed(String profileName) {
		return getProfileCell(profileName, true).isElementPresent();
	}

	public void swipeCells(String profile, int swipes, Direction direction) {
		swipeInContainer(getProfileCell(profile, false), direction, swipes, 500);
	}

	public boolean isHelpWebviewOpen() {
		ExtendedWebElement addressbar = "Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType()) ? phoneWebviewAddressBar : tabletWebviewAddressBar;
		return addressbar.getText().contains("help.disneyplus.com");
	}

	public boolean isAppVersionDisplayed() {
		return getTypeCellLabelContains("Version").isPresent();
	}

	public String getAppVersionText() {
		return appVersion.getText();
	}

	public ExtendedWebElement getAppVersionNumber() {
		return appVersion;
	}

	public void toggleStreamOverWifiOnly(IOSUtils.ButtonStatus status) {
		ExtendedWebElement wifiContainer = getDynamicXpathContainsName(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.STREAM_WIFI_ONLY.getText()));
		if(!wifiContainer.getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equalsIgnoreCase(status.toString())) {
			clickElementAtLocation(wifiContainer, 50, 90);
		}
	}

	public void toggleDownloadOverWifiOnly(IOSUtils.ButtonStatus status) {
		ExtendedWebElement downloadContainer = getDynamicXpathContainsName(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_WIFI_ONLY.getText()));
		if(!downloadContainer.getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equalsIgnoreCase(status.toString())) {
			clickElementAtLocation(downloadContainer, 50, 90);
		}
	}

	public boolean isDeviceStorageCorrectlyDisplayed() {
		ExtendedWebElement storageText = getTypeCellLabelContains(String.format("iPhone %s", getValueBeforePlaceholder(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE.getText()))));
		if(storageText.isElementPresent()) {
			return storageText.getText().contains(getValueBeforePlaceholder(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_APP.getText())))
					&& storageText.getText().contains(getValueBeforePlaceholder(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_FREE.getText())))
					&& storageText.getText().contains(getValueBeforePlaceholder(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_USED.getText())));
		} else {
			return false;
		}
	}

	public String getValueBeforePlaceholder(String rawValue) {
		rawValue = rawValue.trim();
		String substring = StringUtils.substringBefore(rawValue, "${").trim();
		if (substring.equals(rawValue)) {
			substring = StringUtils.substringBefore(rawValue, "{").trim();
		}
		return substring;
	}

	public String getDeleteOneDownloadValue(String rawValue) {
		rawValue = rawValue.trim();
		String substring = StringUtils.substringBetween(rawValue, " {", "({").trim();
		if (substring.equals(rawValue)) {
			substring = StringUtils.substringBetween(rawValue, " {", "({").trim();
		}
		return substring;
	}

	public boolean isDeleteDownloadsEnabled() {
		ExtendedWebElement deleteAllDownloads = deleteAllDownloadsCell.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_LABEL.getText()));
		if(deleteAllDownloads.isElementPresent()) {
			return deleteAllDownloads.getAttribute(IOSUtils.Attributes.ENABLED.getAttribute()).equalsIgnoreCase(Boolean.TRUE.toString());
		} else {
			return false;
		}
	}

	public void clickDeleteAllDownloads() {
		deleteAllDownloadsCell.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_LABEL.getText())).click();
	}

	public boolean areAllDeleteModalItemsPresent() {
		return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_LABEL.getText())).isElementPresent()
				&& deleteOneDownload.format(getDeleteOneDownloadValue(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_ONE_DOWNLOAD.getText()))).isElementPresent()
				&& getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL_BTN_NORMAL.getText())).isElementPresent()
				&& getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_DELETE_BTN.getText())).isElementPresent();
	}

	public boolean isDownloadOverWifiEnabled() {
		return downloadOverWifiOnly.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_WIFI_ONLY.getText())).getAttribute(Attributes.ENABLED.getAttribute()).equalsIgnoreCase(Boolean.TRUE.toString());
	}

	public boolean areWatchlistTitlesDisplayed(String... titles) {
		List<String> items = Arrays.asList(titles);
		List<ExtendedWebElement> entryCells = new ArrayList<>();
		List<Boolean> validations = new ArrayList<>();
		items.forEach(title -> entryCells.add(getTypeCellLabelContains(title)));

		entryCells.forEach(entry -> validations.add(entry.isElementPresent()));
		return !validations.contains(false);
	}

	public boolean areWatchlistTitlesProperlyOrdered(String... titles) {
		List<String> items = Arrays.asList(titles);
		List<ExtendedWebElement> entryCells = getCellsWithLabels();
		List<Boolean> validations = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			try {
				String entryCellTextOnlyTitle = entryCells.get(i).getText().split(",")[0];
				validations.add(entryCellTextOnlyTitle.equals(titles[i]));
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}
		return !validations.contains(false);
	}

	public boolean isWatchlistHeaderDisplayed() {
		return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.WATCHLIST_PAGE_HEADER.getText())).isElementPresent();
	}

	public boolean isWatchlistEmptyBackgroundDisplayed() {
		return watchlistEmpty.isPresent();
	}

	public boolean isAccountUnverifiedBadgeDisplayed() {
		return accountUnverifiedBadge.isPresent();
	}

	public boolean isAddProfileButtonPresent() {
		return addProfileBtn.isElementPresent();
	}

	public void clickAddProfile() {
		addProfileBtn.click();
	}

	public List<ExtendedWebElement> getDisplayedTitles() {
		pause(2);
		List<ExtendedWebElement> titles = findExtendedWebElements(cell.getBy());
		titles.subList(0, 4).clear();
		LOGGER.info("Titles: {}", titles);
		return titles;
	}

	public boolean isExitKidsProfileButtonPresent() {
		return exitKidsProfileButton.isPresent();
	}

	public String getExitKidsProfileButtonText() {
		return exitKidsProfileButton.getText();
	}

	public void tapExitKidsProfileButton() {
		exitKidsProfileButton.click();
	}

	public void tapAccountTab(){
		accountTab.click();
	}

	public ExtendedWebElement findSubtitleLabel(int num) {
		List<ExtendedWebElement> subtitleLabel = findExtendedWebElements(getStaticTextByName("subtitleLabel").getBy());
		return subtitleLabel.get(num);
	}

	public ExtendedWebElement findTitleLabel(int num) {
		List<ExtendedWebElement> titleLabel = findExtendedWebElements(getStaticTextByName("titleLabel").getBy());
		return titleLabel.get(num);
	}

	public void clickBackArrowFromWatchlist() {
		//TEMP solution for bugged watchlist
		backButton.click();
	}

	public boolean isBackArrowInWatchlistPresent() {
		//TEMP solution for bugged watchlist
		return getTypeButtonByLabel("iconNavBack24LightActive").isPresent();
	}

	public ExtendedWebElement getProfileSelectionCollectionView() {
		return profileSelectionCollectionView;
	}

	public boolean isPinLockOnProfileDisplayed(String profileName) {
		return pinProtectedProfileLock.format(profileName).isPresent();
	}
}

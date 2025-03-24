package com.disney.qa.disney.apple.pages.common;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.COMMUNICATION_SETTINGS_LINK_1_TEXT;
import static com.zebrunner.carina.utils.mobile.IMobileUtils.Direction.LEFT;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.common.constant.CollectionConstant;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.InvalidArgumentException;
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
	private ExtendedWebElement editProfilesBtn = getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_EDIT_PROFILE.getText()));

	@FindBy(xpath = "//XCUIElementTypeApplication[@name=\"Disney+\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther")
	private ExtendedWebElement exitJuniorModePin;

	@ExtendedFindBy(iosClassChain =  "**/XCUIElementTypeCell[`label == \"Access %s's profile\"`]/**/XCUIElementTypeImage")
	private ExtendedWebElement profileAvatar;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Access %s's pin protected profile\"`]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage")
	private ExtendedWebElement pinProtectedProfileLock;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"EDIT PROFILES\"`]/preceding-sibling::*")
	private ExtendedWebElement profilesTray;

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS \"Version\"`]")
	private ExtendedWebElement appVersion;

	@FindBy(xpath = "//XCUIElementTypeCell[@name='accountTab']//XCUIElementTypeOther[2]/*/XCUIElementTypeImage")
	private ExtendedWebElement accountUnverifiedBadge;

	private ExtendedWebElement addProfileBtn = getDynamicCellByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PROFILE.getText()));

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

	@ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[$type = 'XCUIElementTypeCell' and " +
			"name = 'unlockedProfileCell'$]")
	private ExtendedWebElement profileContainer;

	private ExtendedWebElement deleteAccountButton = getDynamicAccessibilityId(getLocalizationUtils()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
					COMMUNICATION_SETTINGS_LINK_1_TEXT.getText()));

	public ExtendedWebElement getExitKidsProfile() {
		return exitKidsProfileButton;
	}

	public ExtendedWebElement getExitJuniorModePin() {
		return exitJuniorModePin;
	}

	public ExtendedWebElement getDeviceStorageTitle() {
		return getStaticTextByLabel(getLocalizationUtils().formatPlaceholderString(
				getLocalizationUtils().getDictionaryItem(
						DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE.getText()),
				Map.of(DEVICE, "iPhone")));
	}

	public ExtendedWebElement getUsedStorageLabel() {
		return getStaticTextByLabelContains(getValueBeforePlaceholder(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_USED.getText())));
	}

	public ExtendedWebElement getAppStorageLabel() {
		return getStaticTextByLabelContains(getValueBeforePlaceholder(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_APP.getText())));
	}

	public ExtendedWebElement getFreeStorageLabel() {
		return getStaticTextByLabelContains(getValueBeforePlaceholder(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_FREE.getText())));
	}

	public ExtendedWebElement getDeleteAllDownloadsCell() {
		return deleteAllDownloadsCell.format(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_LABEL.getText()));
	}

	public enum MoreMenu {
		ACCOUNT,
		APP_SETTINGS,
		HELP,
		LEGAL,
		LOG_OUT,
		WATCHLIST
	}

	public String selectMoreMenu(MoreMenu option) {
		String selection;
		switch (option) {
			case WATCHLIST:
				selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_WATCHLIST.getText());
				break;
			case APP_SETTINGS:
				selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText());
				break;
			case ACCOUNT:
				selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_ACCOUNT.getText());
				break;
			case LEGAL:
				selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LEGAL_TITLE.getText());
				break;
			case HELP:
				selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_HELP.getText());
				break;
			case LOG_OUT:
				selection = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.LOGOUT_BTN.getText());
				break;
			default:
				throw new InvalidArgumentException("Invalid selection made");
		}
		return selection;
	}

	public DisneyPlusMoreMenuIOSPageBase(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean isOpened() {
		return editProfilesBtn.isPresent();
	}

	public ExtendedWebElement getAddProfileBtn() {
		return addProfileBtn;
	}

	public ExtendedWebElement getProfileContainer() {
		return profileContainer;
	}

	public ExtendedWebElement getProfileAvatar(String profile) {
		return profileAvatar.format(profile);
	}

	public boolean isMenuOptionPresent(MoreMenu option) {
		return dynamicCellByLabel.format(selectMoreMenu(option)).isElementPresent();
	}

	public boolean isMenuOptionNotPresent(MoreMenu option) {
		return dynamicCellByLabel.format(selectMoreMenu(option)).isElementNotPresent(THREE_SEC_TIMEOUT);
	}

	public void clickMenuOption(MoreMenu option) {
		try {
			dynamicCellByLabel.format(selectMoreMenu(option)).click();
		} catch (NoSuchElementException e) {
			LOGGER.debug("ElementTypeCell located by Label Equals value not found. Falling back to Xpath");
			dynamicXpathContainsName.format(selectMoreMenu(option)).click();
		}
	}

	public void  clickMenuOptionByIndex(MoreMenu option) {
		moreMenuItemByIndex.format(selectMoreMenu(option)).click();
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
			return getDynamicCellByLabel(getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), Map.of(USER_PROFILE, profile)));
		} else {
			return getDynamicCellByLabel(getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, profile)));
		}
	}

	public boolean isProfileSwitchDisplayed(String profileName) {
		return getProfileCell(profileName, false).isElementPresent();
	}

	public boolean isHelpWebviewOpen() {
		ExtendedWebElement addressBar = getAddressBar();
		return fluentWait(getDriver(), TWENTY_FIVE_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Help Webview is not open")
				.until(it -> addressBar.getText().contains("help.disneyplus.com"));
	}

	public String getAppVersion() {
		String[] versionNum = appVersion.getText().split(": ");
		return versionNum[1];
	}

	public boolean isAppVersionDisplayed() {
		String appVersionKey = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
				DictionaryKeys.APP_VERSION_NUMBER.getText()), Map.of("app_version_number_build_number", getAppVersion()));
		return getTypeCellLabelContains(appVersionKey).isPresent();
	}

	public void toggleStreamOverWifiOnly(IOSUtils.ButtonStatus status) {
		ExtendedWebElement wifiContainer = getDynamicXpathContainsName(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.STREAM_WIFI_ONLY.getText()));
		if(!wifiContainer.getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equalsIgnoreCase(status.toString())) {
			clickElementAtLocation(wifiContainer, 50, 90);
		}
	}

	public boolean isDeviceStorageCorrectlyDisplayed() {
		ExtendedWebElement storageText = getTypeCellLabelContains(String.format("iPhone %s", getValueBeforePlaceholder(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE.getText()))));
		if(storageText.isElementPresent()) {
			return storageText.getText().contains(getValueBeforePlaceholder(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_APP.getText())))
					&& storageText.getText().contains(getValueBeforePlaceholder(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_FREE.getText())))
					&& storageText.getText().contains(getValueBeforePlaceholder(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_USED.getText())));
		} else {
			return false;
		}
	}

	public boolean isStorageSizeStringValid(String labelText) {
		String storageSizeRegex = "(?i).*: \\d+(\\.\\d+)? (KB|MB|GB)";
		Pattern pattern = Pattern.compile(storageSizeRegex);
		Matcher matcher = pattern.matcher(labelText);
		return matcher.matches();
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
		ExtendedWebElement deleteAllDownloads = deleteAllDownloadsCell.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_LABEL.getText()));
		if(deleteAllDownloads.isElementPresent()) {
			return deleteAllDownloads.getAttribute(IOSUtils.Attributes.ENABLED.getAttribute()).equalsIgnoreCase(Boolean.TRUE.toString());
		} else {
			return false;
		}
	}

	public void clickDeleteAllDownloads() {
		deleteAllDownloadsCell.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_LABEL.getText())).click();
	}

	public boolean areAllDeleteModalItemsPresent() {
		String deleteDownloadsTitleLabel = getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_LABEL.getText());
		String deleteOneDownloadMessageLabel = getDeleteOneDownloadValue(getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_ONE_DOWNLOAD.getText()));
		String modalCancelButtonLabel = getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL_BTN_NORMAL.getText());
		String modalDeleteButtonLabel = getLocalizationUtils().getDictionaryItem(
				DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_DELETE_BTN.getText());
		String downloadsTotalSizeRegex = "(?i).*\\\\(.*(KB|MB|GB)\\\\).*";
		return getStaticTextByLabel(deleteDownloadsTitleLabel).isElementPresent()
				&& deleteOneDownload.format(deleteOneDownloadMessageLabel).isElementPresent()
				&& getTypeButtonByLabel(modalCancelButtonLabel).isElementPresent()
				&& getTypeButtonByLabel(modalDeleteButtonLabel).isElementPresent()
				&& staticTextLabelMatches.format(downloadsTotalSizeRegex).isElementPresent();
	}

	public boolean isDownloadOverWifiEnabled() {
		return downloadOverWifiOnly.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_WIFI_ONLY.getText())).getAttribute(Attributes.ENABLED.getAttribute()).equalsIgnoreCase(Boolean.TRUE.toString());
	}

	public boolean areWatchlistTitlesDisplayed(String... titles) {
		List<String> items = Arrays.asList(titles);
		List<Boolean> validations = new ArrayList<>();
		CollectionConstant.Collection watchlist = CollectionConstant.Collection.WATCHLIST;
		items.forEach(title -> {
			ExtendedWebElement watchlistItem = getTypeCellLabelContains(title);
			swipeInContainerTillElementIsPresent(getCollection(watchlist), watchlistItem, 1, LEFT);
			validations.add(watchlistItem.isElementPresent());
		});
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

	public ExtendedWebElement getProfileSelectionCollectionView() {
		return profileSelectionCollectionView;
	}

	public boolean isPinLockOnProfileDisplayed(String profileName) {
		return pinProtectedProfileLock.format(profileName).isPresent();
	}

	public ExtendedWebElement getAddressBar() {
		if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
			return phoneWebviewAddressBar;
		} else {
			return tabletWebviewAddressBar;
		}
	}
}

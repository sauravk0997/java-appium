package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.security.SecureRandom;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSearchIOSPageBase extends DisneyPlusApplePageBase {

	//LOCATORS

	private ExtendedWebElement moviesTile = xpathNameOrName.format(getDictionary()
					.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
							DictionaryKeys.NAV_MOVIES_TITLE.getText()),
			DictionaryKeys.NAV_MOVIES_TITLE.getText());

	private ExtendedWebElement originalsTile = xpathNameOrName.format(getDictionary()
					.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
							DictionaryKeys.NAV_ORIGINALS_TITLE.getText()),
			DictionaryKeys.NAV_ORIGINALS_TITLE.getText());

	private ExtendedWebElement seriesTile = xpathNameOrName.format(getDictionary()
					.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
							DictionaryKeys.NAV_SERIES_TITLE.getText()),
			DictionaryKeys.NAV_SERIES_TITLE.getText());

	@FindBy(id = "Search")
	private ExtendedWebElement keyboardSearchButton;

	@ExtendedFindBy(accessibilityId = "Explore")
	private ExtendedWebElement exploreHeader;

	@ExtendedFindBy(iosPredicate = "type == 'XCUIElementTypeSearchField'")
	private ExtendedWebElement searchBar;

	private ExtendedWebElement cancelButton = getStaticTextByLabelOrLabel(getDictionary()
			.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
					DictionaryKeys.CANCEL.getText()), DictionaryKeys.CANCEL.getText());

	//FUNCTIONS

	public DisneyPlusSearchIOSPageBase(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean isOpened() {
		return searchBar.isElementPresent();
	}

	public void clickMoviesTab() {
		moviesTile.click();
	}

	public void clickOriginalsTab() {
		originalsTile.click();
	}

	public void clickSeriesTab() {
		seriesTile.click();
	}

	public List<ExtendedWebElement> getDisplayedTitles() {
    	int tries = 0;
		List<ExtendedWebElement> titles;
    	do {
			pause(2);
			titles = findExtendedWebElements(cell.getBy());
			titles.subList(0, 4).clear();
			LOGGER.info("Titles: {}", titles);
			tries++;
		} while (tries < 3 && titles.isEmpty());
    	return titles;
	}

	public void searchForMedia(String query) {
		searchBar.type(query);
	}

	public void selectRandomTitle() {
		List<ExtendedWebElement> titleList = getDisplayedTitles();
		LOGGER.info("Random title selected.");
		titleList.get(new SecureRandom().nextInt(titleList.size() - 1)).click();
	}

	public ExtendedWebElement getKeyboardSearchButton() {
		return keyboardSearchButton;
	}

	public ExtendedWebElement getSearchBar() {
		return searchBar;
	}

	public ExtendedWebElement getCancelButton() {
		return cancelButton;
	}

	public ExtendedWebElement getOriginalsTile() {
		return originalsTile;
	}
}

package com.disney.qa.disney.web.appex.headermenu;

import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.util.disney.DisneyGlobalUtils;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusMoviesPage extends DisneyPlusBaseProfileViewsPage {

    private static String lion = "{L10N:";
    boolean booleanValue = false;

    @FindBy(xpath = "//div[@role='menu']")
    private ExtendedWebElement moviesFilter;

    @FindBy(xpath = "//div[@id='react-select-2-option-0']")
    private ExtendedWebElement moviesFilterOptionDefault;

    @FindBy(xpath = "//div[@id='react-select-2-option-1']")
    private ExtendedWebElement moviesFilterOptionChanged;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-0-%s']/..")
    private ExtendedWebElement disneyPlusMovieIterator;

    @FindBy(xpath = "(//*[@data-gv2elementkey='contentTile'])[%s]/div")
    private ExtendedWebElement disneyPlusMovieIteratorAfterFilterChange;

    @FindBy(xpath = "//p[contains(text(),'PLAY')]")
    private ExtendedWebElement movieDetailsPlayButton;

    @FindBy(xpath = "//div[@aria-selected='details']")
    private ExtendedWebElement moviesDetailsTab;

    @FindBy(xpath = "//p[contains(text(),'Duration')]")
    private ExtendedWebElement movieDetailsMetaData;

    @FindBy(xpath = "//*[text()='%s']")
    private ExtendedWebElement genericEqualsText;

    @FindBy(xpath = "//button[text()='%s']")
    private ExtendedWebElement genericContainsText;

    @FindBy(xpath = "//button[@aria-label='BUY NOW']")
    private ExtendedWebElement buyNowCtaButton;

    @FindBy(xpath = "//button[@id='unauth-target']")
    private ExtendedWebElement signUpNowCta;

    @FindBy(xpath = "//a[21]")
    private ExtendedWebElement moviesAssetAfterScroll;

    @FindBy(xpath = "//a[11]")
    private ExtendedWebElement contentAvailableAfterFilter;

    @FindBy(xpath = "//*[@data-testid='navigation-item-4-MOVIES']")
    private ExtendedWebElement moviesMenuOption;

    @FindBy(xpath = "//*[@data-testid='nested-collection-navigation-shelf']//button")
    private List<ExtendedWebElement> starMovieGenreFilter;

    @FindBy(xpath = "//*[@data-testid='groupwatch-button']")
    private ExtendedWebElement groupWatchBtn;

    @FindBy(xpath = "//button[@type='submit']")
    private ExtendedWebElement okBtn;

    @FindBy(xpath = "//h4[contains(text(), 'GroupWatch')]")
    private ExtendedWebElement groupWatchPopUpText;

    public DisneyPlusMoviesPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getMoviesMenuOption() {

        return moviesMenuOption;
    }

    public ExtendedWebElement getGenericEqualsText(String text) {
        return genericEqualsText.format(text);
    }

    public ExtendedWebElement getMoviesFilter() {
        return ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) ? starMovieGenreFilter.get(1) : moviesFilter;
    }

    public ExtendedWebElement getMoviesFilterOptionChanged() {
        return ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) ? starMovieGenreFilter.get(2) : moviesFilterOptionChanged;
    }

    public ExtendedWebElement getMovieDetailsPlayButton() {
        return movieDetailsPlayButton;
    }

    public ExtendedWebElement getMoviesDetailsTab() {
        return moviesDetailsTab;
    }

    public ExtendedWebElement getMovieDetailsMetaData() {
        return movieDetailsMetaData;
    }

    public ExtendedWebElement getBuyNowCtaButton() {
        return buyNowCtaButton;
    }

    public ExtendedWebElement getSignUpNowCta() {
        return signUpNowCta;
    }

    public ExtendedWebElement getMoviesFilterOptionDefault() {
        return ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) ? starMovieGenreFilter.get(1) : moviesFilterOptionDefault;
    }

    public ExtendedWebElement getMoviesAssetAfterScroll() {
        return moviesAssetAfterScroll;
    }

    protected ExtendedWebElement getContentAvailableAfterFilter() {
        return contentAvailableAfterFilter;
    }

    public ExtendedWebElement getDisneyPlusTileByIndex(int x) {
        waitFor(disneyPlusMovieIterator.format(Integer.toString(x)));
        return disneyPlusMovieIterator.format(Integer.toString(x));
    }

    //Is Present Methods

    public boolean isDisneyPlusMovieIndexPresent(int movieIndex) {
        waitFor(disneyPlusMovieIteratorAfterFilterChange.format(movieIndex));
        PAGEFACTORY_LOGGER.info("Media name: {}" , disneyPlusMovieIteratorAfterFilterChange.format(movieIndex).getAttribute(WebConstant.ARIA_LABEL));
        return disneyPlusMovieIteratorAfterFilterChange.format(movieIndex).isElementPresent();
    }

    //End is Present Methods

    public int getDisneyPlusMoviesLength() {
        int x;
        for (x = 1; x <= 100; x++) {
            if (disneyPlusMovieIterator.format(Integer.toString(x)).isElementPresent()) {
                x++;
            } else {
                break;
            }
        }
        PAGEFACTORY_LOGGER.info("Number of items grabbed: " + x);
        return x;
    }

    public enum MovieButton {

        MOVIE_NAV_BUTTON("nav_movies"),
        MOVIE_DETAILS_PLAY_BUTTON("btn_play"),
        MOVIES_SUGGESTED_TAB("nav_related"),
        MOVIES_EXTRAS_TAB("nav_extras"),
        MOVIES_DETAILS_TAB("nav_details"),
        HOMEPAGE_WELCOME_CTA("btn_buy_now");

        private String buttonTitle;

        MovieButton(String title) {
            this.buttonTitle = title;
        }

        public String getText() {
            String buttons = this.buttonTitle;
            if (buttons.contains(lion)) {
                String key = buttons.replace(lion, "").replace("}", "");
                buttons = L10N.getText(key);
            }
            return buttons;
        }
    }

    public void moviesPageScroll() {
        js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,1000)");
    }

    public void moviePageScrollUp() {
        js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(1000, 0)");
    }

    public void openDetailsPageUrl(String currentUrl) {
        pause(5);
        PAGEFACTORY_LOGGER.info("Going to " + currentUrl);
        getDriver().get(currentUrl);
    }

    public void clickOnTileByIndex(int index) {
        refresh();
        waitFor(disneyPlusMovieIteratorAfterFilterChange.format(index));
        disneyPlusMovieIteratorAfterFilterChange.format(index).clickByJs();
        PAGEFACTORY_LOGGER.info("Clicked on movies " + index);
    }

    public void clickOnMovieMenuOption() {
        PAGEFACTORY_LOGGER.info("Click 'Movie' menu option");
        getMoviesMenuOption().click();
    }

    public void clickMovieFilter(){
        PAGEFACTORY_LOGGER.info("Click on Movie filter");
        getMoviesFilter().click();
    }

    public void selectMovieFilterOption(){
        PAGEFACTORY_LOGGER.info("select on Movie filter options");
        getMoviesFilterOptionChanged().click();
    }

    public boolean isGroupWatchBtnVisible() {
        PAGEFACTORY_LOGGER.info("Verify if GroupWatch button is visible");
        return groupWatchBtn.isVisible();
    }

    public String getGroupWatchPopUpText() {
        PAGEFACTORY_LOGGER.info("Get groupwatch not available text from pop up");
        return groupWatchPopUpText.getText();
    }
}

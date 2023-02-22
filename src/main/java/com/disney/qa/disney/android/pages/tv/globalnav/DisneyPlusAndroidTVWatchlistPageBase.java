package com.disney.qa.disney.android.pages.tv.globalnav;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVWatchlistPageBase extends DisneyPlusAndroidTVCommonPage {

    @FindBy(id = "watchlistHeaderTextView")
    private ExtendedWebElement watchlistTitle;

    @FindBy(id = "emptyStateTvTitle")
    private ExtendedWebElement watchlistEmptyTitle;

    @FindBy(id = "emptyStateTvDetails")
    private ExtendedWebElement watchlistEmptySubText;

    @FindBy(id = "emptyStateImage")
    private ExtendedWebElement emptyWatchlistImage;

    @FindBy(id = "shelfContainer")
    private ExtendedWebElement assetRowContainer;

    public DisneyPlusAndroidTVWatchlistPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = watchlistTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public enum Watchlist {
        WATCHLIST_TITLE("watchlist_title"),
        WATCHLIST_COPY("watchlist_copy"),
        WATCHLIST_SUB_COPY("watchlist_subcopy");

        private String watchlistDictionaryKey;

        Watchlist(String watchlistDictionaryKey) {
            this.watchlistDictionaryKey = watchlistDictionaryKey;
        }

        public String getText() {
            return this.watchlistDictionaryKey;
        }
    }

    public List<String> getEmptyWatchlistTexts() {
        return Stream.of(watchlistTitle.getText(), watchlistEmptyTitle.getText(), watchlistEmptySubText.getText())
                .collect(Collectors.toList());
    }

    public List<String> getWatchlistAssetTitles() {
        return findExtendedWebElements(mediaPoster.getBy()).stream()
                .map(item -> getAndroidTVUtilsInstance().getContentDescription(item)).collect(Collectors.toList());
    }

    public boolean isEmptyWatchlistIconPresent(){
        return emptyWatchlistImage.isPresent();
    }

    public int getRowsInWatchlist(){
        return findExtendedWebElements(assetRowContainer.getBy()).size();
    }

    public static List<String> getEmptyWatchlistDictionaryKeys(){
        return Stream.of(Watchlist.WATCHLIST_TITLE.getText(), Watchlist.WATCHLIST_COPY.getText(),
                Watchlist.WATCHLIST_SUB_COPY.getText()).collect(Collectors.toList());
    }

    public void useActionToOpenWatchListPage(Consumer<AndroidTVUtils> action){
        fluentWait(getDriver(),(long) LONG_TIMEOUT * 2, 5, "Unable to open watchlist page")
                .until(it -> {
                    getAndroidTVUtilsInstance().keyPressTimes(action, 1, 1);
                    return watchlistTitle.isElementPresent(4);
                });
    }
}

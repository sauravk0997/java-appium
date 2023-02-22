package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVDetailsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusVideoPageBase.class)
public class DisneyPlusAndroidTVVideoPlayerPage extends DisneyPlusVideoPageBase {

    @FindBy(id = "fastForwardButton")
    private ExtendedWebElement fastForwardBtn;

    @FindBy(id = "jumpBackwardButton")
    private ExtendedWebElement jumpBackBtn;

    @FindBy(id = "video_frame")
    private ExtendedWebElement videoPlayerFrame;

    private AndroidTVUtils androidTVUtils;

    public DisneyPlusAndroidTVVideoPlayerPage(WebDriver driver) {
        super(driver);
        androidTVUtils = new AndroidTVUtils(getDriver());
    }

    public void selectFastForwardBtn(int forwardSpeed) {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT * 2L, ONE_SEC_TIMEOUT, "Cannot focus fast-forward button")
                .until(it -> {
                    androidTVUtils.pressRight();
                    return androidTVUtils.isElementFocused(fastForwardBtn);
                });
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressSelect, forwardSpeed, 1);
    }

    public void openPlayerControls() {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT, "Unable to open video player controls")
                .until(it -> {
                    androidTVUtils.pressDown();
                    if (fastForwardBtn.isVisible()) {
                        androidTVUtils.pressSelect();
                        return true;
                    }
                    return false;
                });
    }

    public void isVideoFinishedAfterFastForwarding(int timeout) {
        DisneyPlusAndroidTVDetailsPageBase disneyPlusAndroidTVDetailsPageBase = initPage(DisneyPlusAndroidTVDetailsPageBase.class);
        int count = 0;
        while (count < timeout) {
            if (disneyPlusAndroidTVDetailsPageBase.isUpNextScreenOpen())
                return;
            pause(1);
            count++;
        }

        DisneyPlusCommonPageBase.fluentWait(getDriver(),30,1,"Unable to focus jump back button")
                .until(it ->{
                    androidTVUtils.pressLeft();
                    return androidTVUtils.isElementFocused(jumpBackBtn);
                });
        androidTVUtils.pressSelect();
        pause(10);
        DisneyPlusCommonPageBase.fluentWait(getDriver(),20,1,"Up next Screen did not launch")
                .until(it -> disneyPlusAndroidTVDetailsPageBase.isUpNextScreenOpen());
    }

    public void playVideoTillUpNextScreen(int timeOut, int polling){
        DisneyPlusAndroidTVDetailsPageBase disneyPlusAndroidTVDetailsPageBase = initPage(DisneyPlusAndroidTVDetailsPageBase.class);
        DisneyPlusCommonPageBase.fluentWait(getDriver(),timeOut, polling,"Up Next screen did not launch")
                .until(it -> disneyPlusAndroidTVDetailsPageBase.isUpNextScreenOpen());
    }

}

package com.disney.qa.tests.hls.web.drm;

import com.disney.qa.hls.utilities.HlsParameter;
import com.disney.qa.hls.web.HlsPlayerPlaybackHelper;
import com.disney.qa.tests.hls.web.HlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HlsEventKeyFunctionalityTestDrmCtr extends HlsBaseTest {

    private HlsPlayerPlaybackHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerPlaybackHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());

    }

    @QTestCases(id = "6445")
    @Test(description = "Key Rotation - Fast Fail)", priority = 1)
    public void keyRotationFastFail() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerKeyRotationFastFail(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT_KR);

        sa.assertEquals(testResult, "Test completed"
                , String.format("Expected: Test Completed, Actual: %s"
                        , testResult));
        sa.assertAll();
    }

    @QTestCases(id = "6446")
    @Test(description = "Key Rotation - Play through change)", priority = 2)
    public void keyRotationPlayThroughChange() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerKeyRotationPlayThroughChange(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT_KR);

        sa.assertEquals(testResult, "Test completed"
                , String.format("Expected: Test Completed, Actual: %s"
                        , testResult));
        sa.assertAll();
    }

    @QTestCases(id = "6447")
    @Test(description = "Key Rotation - Play through change and seek back)", priority = 3)
    public void keyRotationPlayThroughChangeAndSeekBack() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerKeyRotationPlayThroughChangeAndSeekBack(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT_KR);

        sa.assertEquals(testResult, "Test completed"
                , String.format("Expected: Test Completed, Actual: %s"
                        , testResult));
        sa.assertAll();
    }
}
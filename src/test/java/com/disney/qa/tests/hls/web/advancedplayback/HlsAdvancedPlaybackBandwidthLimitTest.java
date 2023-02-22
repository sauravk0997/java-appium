package com.disney.qa.tests.hls.web.advancedplayback;

import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.hls.utilities.HlsAssertService;
import com.disney.qa.hls.utilities.HlsParameter;
import com.disney.qa.hls.web.HlsPlayerLateBoundAudioHelper;
import com.disney.qa.tests.hls.web.HlsBaseTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import net.lightbody.bmp.BrowserMobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class HlsAdvancedPlaybackBandwidthLimitTest extends HlsBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private HlsAssertService hlsAssert;
	
	@BeforeMethod
	public void startProxy() {
		GeoedgeProxyServer geoedgeProxyServer = new GeoedgeProxyServer();
		geoedgeProxyServer.setProxyHostForSelenoid();
		R.CONFIG.put("browsermob_proxy", "true");
	}

	@DataProvider(name = "testStreams")
	public Object[][] testStreams() {
		return new Object[][] { { "TUID: LIVE (LATE BOUND AUDIO)", HlsPlayerLateBoundAudioHelper.LATE_BOUND_AUDIO },
				{ "TUID: LIVE (SLIDE - LATE BOUND AUDIO)", HlsPlayerLateBoundAudioHelper.SLIDE_LATE_BOUND_AUDIO } };
	}

	/**
	 * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-3359
	 * 
	 */
	@QTestCases(id = "6406")
	@MethodOwner(owner = "lryzhikov")
	@Test(dataProvider = "testStreams")
	public void playerThrottleTest(String TUID, String stream) {
		SoftAssert softAssert = new SoftAssert();

		HlsPlayerLateBoundAudioHelper hlsPlayerLateBoundAudioHelper = new HlsPlayerLateBoundAudioHelper(getDriver());
		BrowserMobProxy proxy = ProxyPool.getProxy();

		hlsPlayerLateBoundAudioHelper.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
		hlsAssert = new HlsAssertService(getDriver());

		alterBandwidthRate(proxy, 1500000, "1.5");

		hlsPlayerLateBoundAudioHelper.hlsLateBoundConfirmAudioFeeds(stream);

		softAssert.assertTrue(isHighBitrateValid(), "Incorrect (Throttle to 1.5Mbps) Bitrate value!");

		alterBandwidthRate(proxy, 100000, "0.1");

		softAssert.assertTrue(isLowBitrateValid(), "Incorrect (Throttle to 0.1Mbps) Bitrate value!");

		alterBandwidthRate(proxy, 1500000, "1.5");

		softAssert.assertTrue(isHighBitrateValid(), "Incorrect (Throttle to 1.5Mbps) Bitrate value! #2");
		hlsAssert.getMediaCurrentBitrate();

		softAssert.assertAll();
	}

	private boolean isHighBitrateValid() {
		int correctBitRate = 0;

		for (int i = 0; i < 50; i++) {
			if (Integer.parseInt(hlsAssert.getMediaCurrentBitrate().toString()) > 1030) {
				if (correctBitRate >= 5) {
					return true;
				}
				correctBitRate = correctBitRate + 1;
			}
			pause(5);
		}
		return false;
	}

	private boolean isLowBitrateValid() {
		int correctBitRate = 0;

		for (int i = 0; i < 50; i++) {
			if (Integer.parseInt(hlsAssert.getMediaCurrentBitrate().toString()) < 700) {
				if (correctBitRate >= 5) {
					return true;
				}
				correctBitRate = correctBitRate + 1;
			}
			pause(5);
		}
		return false;
	}

	private void alterBandwidthRate(BrowserMobProxy proxy, long bandwidthRate, String updatedValue) {

		LOGGER.info(
				String.format("Checking Before Bandwidth Limit was Set to %s MB's: %s", updatedValue, proxy.getReadBandwidthLimit()));

		proxy.setReadBandwidthLimit(bandwidthRate);
		proxy.setWriteBandwidthLimit(bandwidthRate);

		LOGGER.info(
				String.format("Checking After Bandwidth Limit was Set to %s MB's: %s", updatedValue, proxy.getReadBandwidthLimit()));
	}
}

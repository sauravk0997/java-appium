package com.disney.qa.tests;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;

import com.qaprosoft.carina.core.foundation.AbstractTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.registrar.Xray;


public class BaseTest extends AbstractTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String ANALYTICS_PROJECT_KEY = "ANA01";
    public static final String ANALYTICS_COUNTRY_CODE = "US";
    public static final String ANALYTICS_SMOKE_TEST = "Smoke Test";
    public static final String ZEBRUNNER_XRAY_TEST_KEY = "com.zebrunner.app/tcm.xray.test-key";
    private static final String ZEBRUNNER_XRAY_EXECUTION_KEY = "reporting.tcm.xray.test-execution-key";

    protected String locale;
    protected String language;

    @BeforeSuite(alwaysRun = true)
    public void setXRayExecution() {
        // QCE-545 Jenkins- XML: Set x-ray execution key dynamically

        /*
         * Register custom parameter in TestNG suite xml
         * <parameter name="stringParam::reporting.tcm.xray.test-execution-key::XRay test execution value" value="XWEBQAS-31173"/>
         *
         * Run a job and provide updated execution key if necessary
         */

        String xrayExectionKey = R.CONFIG.get(ZEBRUNNER_XRAY_EXECUTION_KEY);
        if (!xrayExectionKey.isEmpty() && !xrayExectionKey.equalsIgnoreCase("null")) {
            LOGGER.info("{} {} will be assigned to run", ZEBRUNNER_XRAY_EXECUTION_KEY, xrayExectionKey);
            Xray.setExecutionKey(xrayExectionKey);
            // Xray.enableRealTimeSync();
        }
    }
    public boolean horaEnabled() {
        return R.CONFIG.getBoolean("enable_hora_validation");
    }
}

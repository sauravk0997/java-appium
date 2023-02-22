package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.statsapi.NhlPlayoffsDataCapture;
import org.testng.annotations.Test;

public class NhlCapturePlayoffsApiData {

    @Test
    public void capturePlayoffData(){
        NhlPlayoffsDataCapture dataCapture = new NhlPlayoffsDataCapture();
        dataCapture.capturePlayoffData();
    }
}

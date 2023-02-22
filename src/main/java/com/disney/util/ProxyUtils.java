package com.disney.util;

import net.lightbody.bmp.BrowserMobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;

/**
 * Proxy utility class allows for the manpipulation of data captured by the proxy at runtime for
 * verifying criteria in the event that production data is unavailable.
 *
 * @author bsuscavage
 */

public class ProxyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private BrowserMobProxy proxy;

    public ProxyUtils(BrowserMobProxy proxy){
        this.proxy = proxy;
    }

    /**
     * Overrides the response body of a given host address with the desired alteration.
     * The alteration is a complete replacement of the original. In order to modify a live
     * response file, the response body must be captured and edited prior to setting the override.
     *
     * @param hostToOverride - String value of the host or part of the host (ex. http://statsapi.web.nhl.com/api/v1/schedule)
     * @param alteredData - The modified http response body
     */
    public void rewriteHttpResponseBody(String hostToOverride, String alteredData){
        proxy.addResponseFilter((httpResponse, httpMessageContents, httpMessageInfo) -> {
            if(httpMessageInfo.getOriginalUrl().contains(hostToOverride)){
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new StringReader(alteredData));
                    StringBuilder builder = new StringBuilder();
                    String line = reader.readLine();
                    while(line != null){
                        builder.append(line + "\n");
                        line = reader.readLine();
                    }
                    reader.close();
                    httpMessageContents.setTextContents(builder.toString());
                } catch (Exception e){
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
    }
}

package com.disney.qa.tests;

import com.zebrunner.carina.core.AbstractTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

public class HelloDisney extends AbstractTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test()
    public void helloDisney() {
        LOGGER.info("Hello Disney!");
    }
}

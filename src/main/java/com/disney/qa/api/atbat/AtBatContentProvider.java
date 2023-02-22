package com.disney.qa.api.atbat;

import java.net.URI;

/**
 * Created by mk on 10/16/15.
 */
public interface AtBatContentProvider {

    <T> T getNewsReader(String team, Class<T> returnType);

    URI getNewsReaderUrl(String team);
}

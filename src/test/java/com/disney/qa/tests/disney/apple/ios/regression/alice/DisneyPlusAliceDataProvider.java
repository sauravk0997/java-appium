package com.disney.qa.tests.disney.apple.ios.regression.alice;

import lombok.Data;

public interface DisneyPlusAliceDataProvider {

    @Data
    class HulkContent {

        private String title;
        private String entityId;

        public HulkContent(String title, String entityId) {
            this.title = title;
            this.entityId = entityId;
        }
    }

    @Data
    class HulkContentS3 {
        private String title;
        private String entityId;
        private String s3file;

        public HulkContentS3(String title, String entityId, String s3File) {
            this.title = title;
            this.entityId = entityId;
            this.s3file = s3File;
        }
    }
}

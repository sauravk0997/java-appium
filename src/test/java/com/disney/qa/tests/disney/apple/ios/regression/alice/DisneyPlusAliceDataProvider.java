package com.disney.qa.tests.disney.apple.ios.regression.alice;

import lombok.Data;

public interface DisneyPlusAliceDataProvider {

    @Data
    public static class HulkContent {

        private String title;
        private String entityId;

        public HulkContent(String title, String entityId) {
            this.title = title;
            this.entityId = entityId;
        }

        public String getTitle() {
            return title;
        }

        public String getEntityId() {
            return entityId;
        }
    }

    @Data
    public static class HulkContentS3 {
        private String title;
        private String entityId;
        private String s3file;

        public HulkContentS3(String title, String entityId, String s3File) {
            this.title = title;
            this.entityId = entityId;
            this.s3file = s3File;
        }

        public String getEntityId() {
            return entityId;
        }

        public String getS3FileName() {
            return s3file;
        }
    }
}

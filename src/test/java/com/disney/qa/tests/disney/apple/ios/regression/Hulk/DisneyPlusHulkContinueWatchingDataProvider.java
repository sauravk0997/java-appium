package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import lombok.Data;

public interface DisneyPlusHulkContinueWatchingDataProvider {

    @Data
    class HulkContent {
        private String title;
        private String entityId;
        private String continueWatchingId;

        public HulkContent(String title, String entityId, String continueWatchingId) {
            this.title = title;
            this.entityId = entityId;
            this.continueWatchingId = continueWatchingId;
        }
    }

    @Data
    class HulkContentS3 {
        private String title;
        private String entityId;
        private String continueWatchingId;
        private String s3file;

        public HulkContentS3(String title, String entityId, String continueWatchingId, String s3File) {
            this.title = title;
            this.entityId = entityId;
            this.continueWatchingId = continueWatchingId;
            this.s3file = s3File;
        }
    }

    enum PlatformType {
        HANDSET("handsetS3FilePath"),
        TABLET("tabletS3FilePath");

        private final String s3Path;
        PlatformType(String s3path) {
            this.s3Path = s3path;
        }

        public String getS3Path() {
            return s3Path;
        }
    }
}


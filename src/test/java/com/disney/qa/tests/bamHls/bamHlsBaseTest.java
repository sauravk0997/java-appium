package com.disney.qa.tests.bamHls;

import com.disney.qa.bamhls.web.BamHlsAudioHelper;
import com.disney.qa.bamhls.web.BamHlsPlaybackHelper;
import com.disney.qa.tests.BaseTest;

import java.util.ArrayList;
import java.util.List;

public class bamHlsBaseTest extends BaseTest {

    protected BamHlsPlaybackHelper mP;
    protected BamHlsAudioHelper mA;

    protected String PLAYING = "PLAYING";
    protected String PAUSED = "PAUSED";
    protected String ENDED = "ENDED";

    //Test Suite

    protected static final String DRM_CMAF_UNENCRYPTED_ALL = "https://dev-basesite-static.fed-bam.com/mediaplayback-test/test-harness/test-streams/test-streams-cmaf-unenc.json";
    protected static final String BAM_HLS_WEB_QA_TEST_STREAMS = "https://dev-basesite-static.fed-bam.com/mediaplayback-test/test-harness/test-streams/test-streams-bam-hls-web.json";

    //End Test Suites

    //Test Label

    protected static final String CMAF_UNENCRYPTED_TOS6_LW_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset300/bam/ms02/hls/ext/tos6/unenc/master_1aud.m3u8";
    protected static final String CMAF_UNENCRYPTED_ONE_AUDIO_TOS6_LW_VOD = "https://lw.bamgrid.com/2.0/hls/vod/bam/ms02/hls/ext/tos6/unenc/master_1aud.m3u8";
    protected static final String WWE_TS_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC/wwe_master.m3u8";
    protected static final String WWE_TS_EVENT_1736 = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset-1736/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC/wwe_master.m3u8";
    protected static final String WWE_TS_SLIDE = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC/wwe_master.m3u8";
    protected static final String WWE_TS_SLIDE_1736 = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/offset-1736/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC/wwe_master.m3u8";
    protected static final String WWE_TS_VOD = "https://lw.bamgrid.com/2.0/hls/vod/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC/wwe_master.m3u8";
    protected static final String ESPN_SHORT_CHUNK_TS_SILK_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ls01/espn/event/2018/12/12/Wisconsin_Herd_vs_Greensb_20181212_1544632140437/master_desktop_complete_aeng-trimmed.m3u8";
    protected static final String ESPN_SHORT_CHUNK_TS_SILK_SLIDE = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ls01/espn/event/2018/12/12/Wisconsin_Herd_vs_Greensb_20181212_1544632140437/master_desktop_complete_aeng-trimmed.m3u8";
    protected static final String ESPN_SHORT_CHUNK_TS_SILK_VOD = "https://lw.bamgrid.com/2.0/hls/vod/nocookie/profile/default/bam/ls01/espn/event/2018/12/12/Wisconsin_Herd_vs_Greensb_20181212_1544632140437/master_desktop_complete_aeng-trimmed.m3u8";



    //End Test Label

    //Audio Streams

    protected String AUDIO = "Audio";

    protected static final String ENGLISH_ENG = "English 'eng' DA [0]";
    protected static final String ESPANOL_SPA = "Español (fox deportes) 'spa' A [1]";
    protected static final String PORTUGUESE_POR = "Português (Fox soccer plus) 'por' A [2]";
    protected static final String DEUTSCH_DEU = "Deutsch (Fox sports 1) 'deu' A [3]";
    protected static final String RUSSIAN_RUS = "русский (Fox sports 2) 'rus' A [4]";
    protected static final String JAPANESE_JPN = "日本語 (Fox college sports atlantic) 'jpn' A [5]";
    protected static final String ITALIAN_ITA = "Italiano (YES network) 'ita' A [6]";
    protected static final String FRENCH_FRA = "Français (Tone) 'fra' A [7]";

    protected static final List <String> audioStreamsList() {

        List <String> streamList = new ArrayList<>();

        streamList.add(ENGLISH_ENG);
        streamList.add(ESPANOL_SPA);
        streamList.add(PORTUGUESE_POR);
        streamList.add(DEUTSCH_DEU);
        streamList.add(RUSSIAN_RUS);
        streamList.add(JAPANESE_JPN);
        streamList.add(ITALIAN_ITA);
        streamList.add(FRENCH_FRA);

        return streamList;

    }

    //END Audio Streams

    //Playback Rates

    protected String PLAYBACK = "Playback";
    protected String BITRATE = "Bitrate";

    protected static final String BITRATE_348 = "348000 [0]";
    protected static final String BITRATE_698 = "698000 [1]";
    protected static final String BITRATE_1038 = "1038000 [2]";
    protected static final String BITRATE_1546 = "1546000 [3]";

}

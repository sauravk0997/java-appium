package com.disney.qa.api.espn.mobile.watch;

import java.util.List;

public class EspnIOSWatchBucketsPage {

    private String id;

    private String name;

    private String ratio;

    private String link = "";

    private String self;

    private int totalCount;

    private List<EspnIOSWatchContentsPage> contents;


    public void setContents(List<EspnIOSWatchContentsPage> contents){
        this.contents=contents;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getRatio() {
        return this.ratio;
    }

    public String getLink() {
        return this.link;
    }

    public String getSelf() {
        return this.self;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public List<EspnIOSWatchContentsPage> getContents(){
        return this.contents;
    }

}

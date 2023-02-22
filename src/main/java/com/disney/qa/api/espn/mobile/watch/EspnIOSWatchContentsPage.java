package com.disney.qa.api.espn.mobile.watch;

public class EspnIOSWatchContentsPage {

    private String status;

    private String name;

    private String type;

    private String id;

    public void setId(String id) {
        this.id=id;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setType(String type) {
        this.type=type;
    }

    public String getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }
}

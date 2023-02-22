package com.disney.qa.api.disney.pojo;

public class DisneyPayPalPayerName {

    private String firstName = "Joe";
    private String middleName = "QA";
    private String lastName = "Street";
    private String suffix = "Dr.";

    public DisneyPayPalPayerName() {
        //Needs to be empty
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSuffix() {
        return suffix;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}

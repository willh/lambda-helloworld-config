package com.willhamill;

public class HelloResponse {

    String responseLanguage;
    String greeting;

    public String getResponseLanguage() {
        return responseLanguage;
    }

    public void setResponseLanguage(String responseLanguage) {
        this.responseLanguage = responseLanguage;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public HelloResponse(String responseLanguage, String greeting) {
        this.responseLanguage = responseLanguage;
        this.greeting = greeting;
    }

    public HelloResponse() {
    }
}

package com.willhamill;

public class HelloResponse {

    String language;
    String greeting;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public HelloResponse(String language, String greeting) {
        this.language = language;
        this.greeting = greeting;
    }

    public HelloResponse() {
    }
}

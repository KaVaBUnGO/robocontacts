package com.robocontacts.domain;

public enum SocialPlatform {
    VK("Vkontakte"),
    GOOGLE("Google");

    private final String label;

    SocialPlatform(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

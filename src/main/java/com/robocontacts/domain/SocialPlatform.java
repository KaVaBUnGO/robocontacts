package com.robocontacts.domain;

/**
 * @author Artur Chernov
 */
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

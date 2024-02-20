package com.plugin.samusprogressbar;

import javax.swing.ImageIcon;

public enum Character {

    SAMUS_POWER_SUIT(Icons.SAMUS_POWER_SUIT, Icons.MORPH_POWER_SUIT),
    SAMUS_GRAVITY_SUIT(Icons.SAMUS_GRAVITY_SUIT, Icons.MORPH_GRAVITY_SUIT),
    SAMUS_ZERO_SUIT(Icons.SAMUS_ZERO_SUIT, Icons.MORPH_ZERO_SUIT),
    SAMUS_FUSION_SUIT(Icons.SAMUS_FUSION_SUIT, Icons.MORPH_FUSION_SUIT);

    private final ImageIcon iconDeterminate;
    private final ImageIcon iconIndeterminate;

    Character(ImageIcon iconDeterminate, ImageIcon iconIndeterminate) {
        this.iconDeterminate = iconDeterminate;
        this.iconIndeterminate = iconIndeterminate;
    }

    public String getDisplayNameDeterminate() {
        return iconDeterminate.getDescription();
    }

    public String getDisplayNameIndeterminate() {
        return iconIndeterminate.getDescription();
    }

    public ImageIcon getIconDeterminate() {
        return iconDeterminate;
    }

    public ImageIcon getIconIndeterminate() {
        return iconIndeterminate;
    }
}

package com.plugin.samusprogressbar;

import javax.swing.ImageIcon;
import java.awt.Color;

public enum Character {

    SAMUS_POWER_SUIT(Icons.SAMUS_POWER_SUIT, Icons.SAMUS_POWER_SUIT_BAR, Icons.MORPH_POWER_SUIT, ColorBar.COLOR_POWER_SUIT),
    SAMUS_GRAVITY_SUIT(Icons.SAMUS_GRAVITY_SUIT, Icons.SAMUS_GRAVITY_SUIT_BAR, Icons.MORPH_GRAVITY_SUIT, ColorBar.COLOR_GRAVITY_SUIT),
    SAMUS_ZERO_SUIT(Icons.SAMUS_ZERO_SUIT, Icons.SAMUS_ZERO_SUIT_BAR, Icons.MORPH_ZERO_SUIT, ColorBar.COLOR_ZERO_SUIT),
    SAMUS_FUSION_SUIT(Icons.SAMUS_FUSION_SUIT, Icons.SAMUS_FUSION_SUIT_BAR, Icons.MORPH_FUSION_SUIT, ColorBar.COLOR_FUSION_SUIT);

    private final ImageIcon iconSettings;
    private final ImageIcon iconDeterminate;
    private final ImageIcon iconIndeterminate;
    private final Color[] barColors;

    Character(ImageIcon iconSettings, ImageIcon iconDeterminate, ImageIcon iconIndeterminate, Color[] barColors) {
        this.iconSettings = iconSettings;
        this.iconDeterminate = iconDeterminate;
        this.iconIndeterminate = iconIndeterminate;
        this.barColors = barColors;
    }

    public String getDisplayNameDeterminate() {
        return iconDeterminate.getDescription();
    }

    public String getDisplayNameIndeterminate() {
        return iconIndeterminate.getDescription();
    }

    public ImageIcon getIconSettings() {
        return iconSettings;
    }

    public ImageIcon getIconDeterminate() {
        return iconDeterminate;
    }

    public ImageIcon getIconIndeterminate() {
        return iconIndeterminate;
    }

    public Color[] getBarColors() {
        return barColors;
    }
}

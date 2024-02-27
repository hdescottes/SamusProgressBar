package com.plugin.samusprogressbar;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Character {

    SAMUS_POWER_SUIT(Icons.SAMUS_POWER_SUIT, Icons.SAMUS_POWER_SUIT_BAR, Icons.MORPH_POWER_SUIT, ColorBar.COLOR_POWER_SUIT),
    SAMUS_GRAVITY_SUIT(Icons.SAMUS_GRAVITY_SUIT, Icons.SAMUS_GRAVITY_SUIT_BAR, Icons.MORPH_GRAVITY_SUIT, ColorBar.COLOR_GRAVITY_SUIT),
    SAMUS_ZERO_SUIT(Icons.SAMUS_ZERO_SUIT, Icons.SAMUS_ZERO_SUIT_BAR, Icons.MORPH_ZERO_SUIT, ColorBar.COLOR_ZERO_SUIT),
    SAMUS_FUSION_SUIT(Icons.SAMUS_FUSION_SUIT, Icons.SAMUS_FUSION_SUIT_BAR, Icons.MORPH_FUSION_SUIT, ColorBar.COLOR_FUSION_SUIT),
    RANDOM(Icons.RANDOM, Icons.SAMUS_POWER_SUIT_BAR, Icons.MORPH_POWER_SUIT, ColorBar.COLOR_POWER_SUIT);

    private final ImageIcon iconSettings;
    private ImageIcon iconDeterminate;
    private ImageIcon iconIndeterminate;
    private Color[] barColors;

    Character(ImageIcon iconSettings, ImageIcon iconDeterminate, ImageIcon iconIndeterminate, Color[] barColors) {
        this.iconSettings = iconSettings;
        this.iconDeterminate = iconDeterminate;
        this.iconIndeterminate = iconIndeterminate;
        this.barColors = barColors;
    }

    public String getDisplayNameIconSettings() {
        return iconSettings.getDescription();
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

    public static Character setRandomDeterminateCharacter() {
        Character character = Character.RANDOM;
        Character random = getRandomCharacter();
        character.iconDeterminate = random.getIconDeterminate();
        character.barColors = random.getBarColors();
        return character;
    }

    public static Character setRandomIndeterminateCharacter() {
        Character character = Character.RANDOM;
        character.iconIndeterminate = getRandomCharacter().getIconIndeterminate();
        return character;
    }

    private static Character getRandomCharacter() {
        List<Character> characters = Arrays.stream(Character.values())
                .filter(c -> c != Character.RANDOM)
                .collect(Collectors.toList());
        Collections.shuffle(characters);
        return characters.get(0);
    }
}

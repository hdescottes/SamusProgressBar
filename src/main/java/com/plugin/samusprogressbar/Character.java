package com.plugin.samusprogressbar;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Character {

    SAMUS_POWER_SUIT(Icons.SAMUS_POWER_SUIT, Icons.SAMUS_POWER_SUIT_BAR, Icons.MORPH_POWER_SUIT, ColorBar.COLOR_POWER_SUIT),
    SAMUS_GRAVITY_SUIT(Icons.SAMUS_GRAVITY_SUIT, Icons.SAMUS_GRAVITY_SUIT_BAR, Icons.MORPH_GRAVITY_SUIT, ColorBar.COLOR_GRAVITY_SUIT),
    SAMUS_ZERO_SUIT(Icons.SAMUS_ZERO_SUIT, Icons.SAMUS_ZERO_SUIT_BAR, Icons.MORPH_ZERO_SUIT, ColorBar.COLOR_ZERO_SUIT),
    SAMUS_FUSION_SUIT(Icons.SAMUS_FUSION_SUIT, Icons.SAMUS_FUSION_SUIT_BAR, Icons.MORPH_FUSION_SUIT, ColorBar.COLOR_FUSION_SUIT),
    SAMUS_VARIA_FP_SUIT(Icons.SAMUS_VARIA_FP_SUIT, Icons.SAMUS_VARIA_FP_SUIT_BAR, Icons.MORPH_VARIA_FP_SUIT, ColorBar.COLOR_VARIA_FP_SUIT),
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

    public static ImageIcon flipImageIcon(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bufferedImage.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bufferedImage = op.filter(bufferedImage, null);

        return new ImageIcon(bufferedImage);
    }
}

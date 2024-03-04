package com.plugin.samusprogressbar.settings;

import com.intellij.ui.components.JBRadioButton;
import com.plugin.samusprogressbar.Character;
import com.plugin.samusprogressbar.settings.SettingsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JComponent;

import static com.plugin.samusprogressbar.Icons.SAMUS_POWER_SUIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettingsComponentTest {

    private SettingsComponent settingsComponent;

    @BeforeEach
    void init() {
        settingsComponent = new SettingsComponent();
    }

    @Test
    void should_return_preferred_focus_component() {
        JComponent component = settingsComponent.getPreferredFocusedComponent();

        assertEquals(SAMUS_POWER_SUIT.getDescription(), ((JBRadioButton) component).getText());
    }

    @Test
    void should_return_default_character_if_nothing_selected() {
        Character character = settingsComponent.getChosenCharacter();

        assertEquals(Character.SAMUS_POWER_SUIT, character);
    }

    @Test
    void should_return_chosen_character() {
        settingsComponent.setChosenCharacter(Character.SAMUS_FUSION_SUIT);

        Character character = settingsComponent.getChosenCharacter();

        assertEquals(Character.SAMUS_FUSION_SUIT, character);
    }
}

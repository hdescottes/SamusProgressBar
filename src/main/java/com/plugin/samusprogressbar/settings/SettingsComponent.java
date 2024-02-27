package com.plugin.samusprogressbar.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import com.plugin.samusprogressbar.Character;
import com.plugin.samusprogressbar.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.plugin.samusprogressbar.Character.SAMUS_POWER_SUIT;

public class SettingsComponent {

    private final JPanel configMainPanel;

    private final List<JBRadioButton> charactersRadioButtons = new ArrayList<>();

    public SettingsComponent() {
        JBLabel title = new JBLabel("Choose your character :", UIUtil.ComponentStyle.REGULAR);
        ButtonGroup characterSelectGroup = new ButtonGroup();
        FormBuilder formBuilder = FormBuilder.createFormBuilder().addComponent(title);
        for (Character character : Character.values()) {
            JBRadioButton radioButton = new JBRadioButton(character.getDisplayNameIconSettings());
            characterSelectGroup.add(radioButton);
            charactersRadioButtons.add(radioButton);
            formBuilder.addLabeledComponent(new JLabel(character.getIconSettings()), radioButton);
        }
        configMainPanel = formBuilder.addComponentFillVertically(new JPanel(), 0).getPanel();
    }

    public JComponent getPreferredFocusedComponent() {
        try {
            return charactersRadioButtons.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public JPanel getPanel() {
        return configMainPanel;
    }

    @NotNull
    public Character getChosenCharacter() {
        String radioText = charactersRadioButtons.stream()
                .filter(AbstractButton::isSelected)
                .map(AbstractButton::getText)
                .findFirst()
                .orElse(Icons.SAMUS_POWER_SUIT.getDescription());

        return Arrays.stream(Character.values())
                .filter(character -> character.getIconSettings().getDescription().equals(radioText))
                .findFirst()
                .orElse(SAMUS_POWER_SUIT);
    }

    public void setChosenCharacter(@NotNull Character character) {
        for (JBRadioButton charactersRadioButton : charactersRadioButtons) {
            charactersRadioButton.setSelected(character.getDisplayNameIconSettings().equals(charactersRadioButton.getText()));
        }
    }
}

package com.plugin.samusprogressbar.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class Config implements Configurable {

    private SettingsComponent settingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Samus Progress Bar";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return settingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsComponent = new SettingsComponent();
        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        SettingsState settings = SettingsState.getInstance();
        return !settingsComponent.getChosenCharacter().equals(settings.selectedCharacter) ||
                settingsComponent.getChosenCharacter() != settings.selectedCharacter;
    }

    @Override
    public void apply() {
        SettingsState settings = SettingsState.getInstance();
        settings.selectedCharacter = settingsComponent.getChosenCharacter();
    }

    @Override
    public void reset() {
        SettingsState settings = SettingsState.getInstance();
        settingsComponent.setChosenCharacter(settings.selectedCharacter);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}

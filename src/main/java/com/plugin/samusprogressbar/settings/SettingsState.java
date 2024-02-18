package com.plugin.samusprogressbar.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.plugin.samusprogressbar.Character;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.plugin.samusprogressbar.Character.SAMUS_POWER_SUIT;

@State(
        name = "com.plugin.samusprogressbar.settings.SettingsState",
        storages = @Storage("PluginSettings.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {

    public Character selectedCharacter = SAMUS_POWER_SUIT;

    public static SettingsState getInstance() {
        return ApplicationManager.getApplication().getService(SettingsState.class);
    }

    @Nullable
    @Override
    public SettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}

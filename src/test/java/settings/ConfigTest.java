package settings;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBRadioButton;
import com.plugin.samusprogressbar.Character;
import com.plugin.samusprogressbar.settings.Config;
import com.plugin.samusprogressbar.settings.SettingsState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigTest {

    private Config config;

    @BeforeEach
    void init() {
        config = new Config();
    }

    @ParameterizedTest
    @MethodSource("characterSelection")
    void should_create_settings_component(Character character, int jLabel, int jbRadioButton) {
        JComponent component = config.createComponent();

        assertNotNull(component);
        assertEquals("Choose your character :", ((JBLabel) component.getComponents()[0]).getText());
        assertEquals(character.getIconDeterminate().getDescription(), ((JLabel) component.getComponents()[jLabel]).getIcon().toString());
        assertEquals(character.getIconDeterminate().getDescription(), ((JBRadioButton) component.getComponents()[jbRadioButton]).getText());
    }

    private static Stream<Arguments> characterSelection() {
        return Stream.of(
                Arguments.of(Character.SAMUS_POWER_SUIT, 1, 2),
                Arguments.of(Character.SAMUS_GRAVITY_SUIT, 3, 4),
                Arguments.of(Character.SAMUS_ZERO_SUIT, 5, 6),
                Arguments.of(Character.SAMUS_FUSION_SUIT, 7, 8)
        );
    }

    @ParameterizedTest
    @MethodSource("isModified")
    void should_check_if_component_is_modified(Character character, boolean value) {
        Application mockApp = mock(Application.class);
        SettingsState mockState = mock(SettingsState.class);
        mockState.selectedCharacter = character;
        ApplicationManager.setApplication(mockApp);
        when(mockApp.getService(SettingsState.class)).thenReturn(mockState);
        config.createComponent();

        boolean isModified = config.isModified();

        assertEquals(value, isModified);
    }

    private static Stream<Arguments> isModified() {
        return Stream.of(
                Arguments.of(Character.SAMUS_POWER_SUIT, false),
                Arguments.of(Character.SAMUS_FUSION_SUIT, true)
        );
    }
}

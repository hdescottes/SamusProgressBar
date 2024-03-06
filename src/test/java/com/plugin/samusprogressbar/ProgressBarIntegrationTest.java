package com.plugin.samusprogressbar;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.launcher.Ide;
import com.intellij.remoterobot.launcher.IdeDownloader;
import com.intellij.remoterobot.launcher.IdeLauncher;
import com.plugin.samusprogressbar.pages.DialogFixture;
import com.plugin.samusprogressbar.pages.IdeaFrame;
import com.plugin.samusprogressbar.steps.Steps;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.plugin.samusprogressbar.RemoteRobotExtKt.isAvailable;
import static com.plugin.samusprogressbar.pages.ActionMenuFixtureKt.actionMenu;
import static com.plugin.samusprogressbar.pages.ActionMenuFixtureKt.actionMenuItem;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static org.assertj.swing.timing.Pause.pause;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressBarIntegrationTest {

    private static Process ideaProcess;
    private static Path tmpDir;
    private static RemoteRobot remoteRobot;
    private final static Ide.BuildType buildType = Ide.BuildType.RELEASE;
    private final static String version = "2023.3.4";
    private final Steps sharedSteps = new Steps(remoteRobot);

    static {
        try {
            tmpDir = Files.createTempDirectory("launcher");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @BeforeAll
    public static void before() {
        final OkHttpClient client = new OkHttpClient();
        remoteRobot = new RemoteRobot("http://localhost:8082", client);
        final IdeDownloader ideDownloader = new IdeDownloader(client);
        final Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("robot-server.port", 8082);
        additionalProperties.put("jb.privacy.policy.text", "<!--999.999-->");
        additionalProperties.put("jb.consents.confirmation.enabled", false);
        final List<Path> plugins = new ArrayList<>();
        plugins.add(ideDownloader.downloadRobotPlugin(tmpDir));
        plugins.add(Paths.get("src","test","resources", "SamusProgressBar.jar"));
        ideaProcess = IdeLauncher.INSTANCE.launchIde(
                ideDownloader.downloadAndExtract(Ide.IDEA_COMMUNITY, tmpDir, buildType, version),
                additionalProperties,
                Collections.emptyList(),
                plugins,
                tmpDir
        );
        waitFor(ofSeconds(90), ofSeconds(5), () -> isAvailable(remoteRobot));
    }

    @AfterAll
    public static void after() throws IOException {
        ideaProcess.destroy();
        // Disable for the pipeline
        // FileUtils.cleanDirectory(tmpDir.toFile());
    }

    @Test
    public void select_a_character_and_launch_build() {
        sharedSteps.createNewProject();
        sharedSteps.closeTipOfTheDay();

        final IdeaFrame idea = remoteRobot.find(IdeaFrame.class, ofSeconds(10));
        waitFor(ofMinutes(5), () -> !idea.isDumbMode());

        step("Setup the plugin's settings", () -> {
            actionMenu(remoteRobot, "File").click();
            actionMenuItem(remoteRobot, "Settings...").click();
            pause(ofSeconds(2).toMillis());
            final DialogFixture settingsDialog = remoteRobot.find(DialogFixture.class,
                    DialogFixture.byTitle("Settings"), Duration.ofSeconds(10));
            settingsDialog.findText("Tools").click();
            settingsDialog.findText("Samus Progress Bar").click();
            settingsDialog.radioButton("Samus (Gravity Suit)").click();
            settingsDialog.button("OK").click();
        });

        pause(ofMinutes(1).toMillis());

        step("Trigger the progress bar", () -> {
            actionMenu(remoteRobot, "Build").click();
            actionMenuItem(remoteRobot, "Rebuild Project").click();
            List<ComponentFixture> list = remoteRobot.findAll(ComponentFixture.class, byXpath("//div[@class='JProgressBar']"));
            ComponentFixture componentFixture = list.get(0);
            assertEquals(20, componentFixture.getRemoteComponent().getHeight());
        });
    }
}

package com.plugin.samusprogressbar;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.launcher.Ide;
import com.intellij.remoterobot.launcher.IdeDownloader;
import com.intellij.remoterobot.launcher.IdeLauncher;
import com.intellij.remoterobot.utils.Keyboard;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.plugin.samusprogressbar.RemoteRobotExtKt.isAvailable;
import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_F9;
import static java.awt.event.KeyEvent.VK_S;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static org.assertj.swing.timing.Pause.pause;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgressBarIntegrationTest {

    private static RemoteRobot remoteRobot;
    private static Process ideaProcess;
    private static Path tmpDir;
    private static final Ide.BuildType buildType = Ide.BuildType.RELEASE;
    private static final String VERSION = "2025.2";

    static {
        try {
            tmpDir = Files.createTempDirectory("launcher");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @BeforeAll
    static void before() {
        final OkHttpClient client = new OkHttpClient();
        remoteRobot = new RemoteRobot("http://127.0.0.1:8082");
        final IdeDownloader ideDownloader = new IdeDownloader(client);
        final Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("robot-server.port", 8082);
        additionalProperties.put("jb.privacy.policy.text", "<!--999.999-->");
        additionalProperties.put("jb.consents.confirmation.enabled", false);
        final List<Path> plugins = new ArrayList<>();
        plugins.add(ideDownloader.downloadRobotPlugin(tmpDir));
        plugins.add(Paths.get("src","test","resources", "SamusProgressBar.jar"));
        ideaProcess = IdeLauncher.INSTANCE.launchIde(
                ideDownloader.downloadAndExtract(Ide.IDEA_COMMUNITY, tmpDir, buildType, VERSION),
                additionalProperties,
                List.of("-Dide.show.tips.on.startup.default.value=false", "-Didea.trust.all.projects=true"),
                plugins,
                tmpDir,
                stringMap -> null
        );
        waitFor(ofSeconds(90), ofSeconds(5), () -> isAvailable(remoteRobot));
    }

    @AfterAll
    static void after() throws IOException {
        ideaProcess.destroy();
        // Disable for the pipeline
        // FileUtils.cleanDirectory(tmpDir.toFile());
    }


    @Test
    void select_a_character_and_launch_build() {
        Steps sharedSteps = new Steps(remoteRobot);
        sharedSteps.createNewProject();
        sharedSteps.closeTipOfTheDay();
        sharedSteps.waitUntilAllTheBgTasksFinish();

        final IdeaFrame idea = remoteRobot.find(IdeaFrame.class, ofSeconds(10));
        waitFor(ofMinutes(5), () -> !idea.isDumbMode());

        step("Setup the plugin's settings", () -> {
            Keyboard keyboard = new Keyboard(remoteRobot);
            keyboard.hotKey(VK_CONTROL, VK_ALT, VK_S);
            pause(ofSeconds(2).toMillis());
            final DialogFixture settingsDialog = remoteRobot.find(DialogFixture.class,
                    DialogFixture.byTitle("Settings"), Duration.ofSeconds(10));
            settingsDialog.findText("Tools").click();
            settingsDialog.findText("Samus Progress Bar").click();
            settingsDialog.radioButton("Samus (Gravity Suit)").click();
            settingsDialog.button("OK").click();
        });

        step("Trigger the progress bar", () -> {
            Keyboard keyboard = new Keyboard(remoteRobot);
            keyboard.hotKey(VK_CONTROL, VK_F9);
            List<ComponentFixture> list = remoteRobot.findAll(ComponentFixture.class, byXpath("//div[@class='JProgressBar']"));
            ComponentFixture componentFixture = list.get(0);
            assertEquals(20, componentFixture.getRemoteComponent().getHeight());
        });
    }
}

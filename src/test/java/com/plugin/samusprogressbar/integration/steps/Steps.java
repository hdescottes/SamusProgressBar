package com.plugin.samusprogressbar.integration.steps;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.plugin.samusprogressbar.pages.DialogFixture;
import com.plugin.samusprogressbar.pages.IdeStatusBarFixture;
import com.plugin.samusprogressbar.pages.IdeaFrame;
import com.plugin.samusprogressbar.integration.pages.WelcomeFrameFixture;
import kotlin.Unit;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.plugin.samusprogressbar.pages.DialogFixture.byTitle;
import static java.time.Duration.ofSeconds;

public class Steps {

    private final RemoteRobot remoteRobot;

    public Steps(RemoteRobot remoteRobot) {
        this.remoteRobot = remoteRobot;
    }

    public void createNewProject() {
        step("Create New Project", () -> {
            final WelcomeFrameFixture welcomeFrame = remoteRobot.find(WelcomeFrameFixture.class, ofSeconds(10));
            welcomeFrame.createNewProjectLink().click();

            final DialogFixture newProjectDialog = welcomeFrame.find(DialogFixture.class, byTitle("New Project"), ofSeconds(20));
            newProjectDialog.findText("Java").click();
            newProjectDialog.button("Create").click();
        });
    }

    public void closeTipOfTheDay() {
        step("Close Tip of the Day if it appears", () -> {
            waitFor(ofSeconds(20), () -> remoteRobot.findAll(DialogFixture.class, byXpath("//div[@class='MyDialog'][.//div[@text='Running startup activities...']]")).isEmpty());
            final IdeaFrame idea = remoteRobot.find(IdeaFrame.class, ofSeconds(10));
            idea.dumbAware(() -> {
                try {
                    idea.find(DialogFixture.class, byTitle("Tip of the Day")).button("Close").click();
                } catch (Throwable ignore) {
                }
                return Unit.INSTANCE;
            });
        });
    }

    public void waitUntilAllTheBgTasksFinish() {
        step("Wait until all the background tasks finish", () -> {
            waitFor(Duration.ofSeconds(600), Duration.ofSeconds(10), "The background tasks did not finish in 10 minutes.", () -> didAllTheBgTasksFinish(remoteRobot));
        });
    }

    private static boolean didAllTheBgTasksFinish(RemoteRobot remoteRobot) {
        for (int i = 0; i < 5; i++) {
            final IdeStatusBarFixture ideStatusBarFixture = remoteRobot.find(IdeStatusBarFixture.class);
            List<RemoteText> inlineProgressPanelContent = ideStatusBarFixture.inlineProgressPanel().findAllText();
            if (!inlineProgressPanelContent.isEmpty()) {
                return false;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

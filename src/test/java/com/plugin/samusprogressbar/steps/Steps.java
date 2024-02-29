package com.plugin.samusprogressbar.steps;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.JListFixture;
import com.plugin.samusprogressbar.pages.DialogFixture;
import com.plugin.samusprogressbar.pages.IdeaFrame;
import com.plugin.samusprogressbar.pages.WelcomeFrameFixture;
import kotlin.Unit;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.plugin.samusprogressbar.pages.DialogFixture.byTitle;
import static java.time.Duration.ofSeconds;

public class Steps {

    final private RemoteRobot remoteRobot;

    public Steps(RemoteRobot remoteRobot) {
        this.remoteRobot = remoteRobot;
    }

    public void createNewProject() {
        step("Create New Project", () -> {
            final WelcomeFrameFixture welcomeFrame = remoteRobot.find(WelcomeFrameFixture.class, ofSeconds(10));
            welcomeFrame.createNewProjectLink().click();

            final DialogFixture newProjectDialog = welcomeFrame.find(DialogFixture.class, byTitle("New Project"), ofSeconds(20));
            newProjectDialog.find(JListFixture.class, byXpath("//div[@class='JBList']")).clickItem("New Project", true);
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
}

package com.plugin.samusprogressbar.pages

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.*
import com.intellij.remoterobot.search.locators.byXpath
import org.jetbrains.annotations.NotNull


@DefaultXpath(by = "IdeStatusBarImpl type", xpath = "//div[@class='IdeStatusBarImpl']")
@FixtureName(name = "Ide Status Bar")
class IdeStatusBarFixture(@NotNull remoteRobot: RemoteRobot, @NotNull remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {
    fun inlineProgressPanel(): ComponentFixture {
        return find(ContainerFixture::class.java, byXpath("//div[@class='InlineProgressPanel']"))
    }

    fun ideErrorsIcon(): ComponentFixture {
        return find(ComponentFixture::class.java, byXpath("//div[@class='IdeErrorsIcon']"))
    }
}
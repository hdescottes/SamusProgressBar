package com.plugin.samusprogressbar.progressbar;

import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.UIUtil;
import com.plugin.samusprogressbar.settings.SettingsState;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class DeterminateBar {

    private JProgressBar progressBar;

    public DeterminateBar() {
    }

    public void drawDeterminateBar(JProgressBar progressBar, Graphics graphics, JComponent component, int width, int height, int amountFull) {
        this.progressBar = progressBar;
        Color background = component.getParent() != null ? component.getParent().getBackground() : UIUtil.getPanelBackground();
        Graphics2D graphics2D = (Graphics2D) graphics;
        final float off = JBUIScale.scale(1f);

        if (component.isOpaque()) {
            graphics.fillRect(0, 0, width, height);
        }

        drawBar(component, width, height, graphics2D, background, off);
        drawGradiant(height, amountFull, graphics2D, off);
        drawDeterminateIcon(amountFull, graphics2D);

        graphics2D.translate(0, -(component.getHeight() - height) / 2);
    }

    private void drawGradiant(int height, int amountFull, Graphics2D graphics2D, float off) {
        LinearGradientPaint color = new LinearGradientPaint(new Point2D.Float(0, 0),
                new Point2D.Float(0, progressBar.getHeight()),
                new float[]{0f, 0.5f, 1.0f},
                SettingsState.getInstance().selectedCharacter.getBarColors(),
                MultipleGradientPaint.CycleMethod.REFLECT);
        graphics2D.setPaint(color);
        graphics2D.fill(new RoundRectangle2D.Float(2f * off, 2f * off, amountFull - JBUIScale.scale(5f), height - JBUIScale.scale(5f), JBUIScale.scale(7f), JBUIScale.scale(7f)));
    }

    private void drawDeterminateIcon(int amountFull, Graphics2D g2) {
        SettingsState.getInstance().selectedCharacter.getIconDeterminate().paintIcon(progressBar, g2, amountFull - JBUIScale.scale(20), -JBUIScale.scale(1));
    }

    private void drawBar(JComponent component, int width, int height, Graphics2D graphics2D, Color background, float off) {
        final float R = JBUIScale.scale(8f);
        final float R2 = JBUIScale.scale(9f);

        graphics2D.translate(0, (component.getHeight() - height) / 2);
        graphics2D.setColor(progressBar.getForeground());
        graphics2D.fill(new RoundRectangle2D.Float(0, 0, width - off, height - off, R2, R2));
        graphics2D.setColor(background);
        graphics2D.fill(new RoundRectangle2D.Float(off, off, width - 2f * off - off, height - 2f * off - off, R, R));
    }
}

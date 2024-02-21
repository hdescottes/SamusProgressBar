package com.plugin.samusprogressbar.progressbar;

import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.scale.JBUIScale;
import com.plugin.samusprogressbar.settings.SettingsState;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class IndeterminateBar {

    private volatile int offset = 0;
    private volatile int velocity = 1;

    private JProgressBar progressBar;

    public IndeterminateBar() {
    }

    public void drawIndeterminateBar(JProgressBar progressBar, JComponent component, Graphics2D graphics2D) {
        this.progressBar = progressBar;
        int width = component.getWidth();
        int height = component.getPreferredSize().height;

        if (!isEven(component.getHeight() - height)) {
            height++;
        }
        if (component.isOpaque()) {
            graphics2D.fillRect(0, (component.getHeight() - height) / 2, width, height);
        }
        graphics2D.setColor(new JBColor(Gray._165.withAlpha(50), Gray._88.withAlpha(50)));
        graphics2D.translate(0, (component.getHeight() - height) / 2);

        drawBar(component, graphics2D, width, height);
        drawIndeterminateIcon(graphics2D, width);
    }

    private void drawIndeterminateIcon(Graphics2D graphics2D, int width) {
        offset += velocity;
        if (offset <= 2) {
            offset = 2;
            velocity = 1;
        } else if (offset >= width - JBUIScale.scale(15)) {
            offset = width - JBUIScale.scale(15);
            velocity = -1;
        }
        SettingsState.getInstance().selectedCharacter.getIconIndeterminate().paintIcon(progressBar, graphics2D, offset - JBUIScale.scale(3), -JBUIScale.scale(-2));
    }

    private void drawBar(JComponent component, Graphics2D graphics2D, int width, int height) {
        final float R = JBUIScale.scale(8f);
        final Area containingRoundRect = new Area(new RoundRectangle2D.Float(1f, 1f, width - 2f, height - 2f, R, R));
        graphics2D.fill(containingRoundRect);

        graphics2D.draw(new RoundRectangle2D.Float(1f, 1f, width - 2f - 1f, height - 2f - 1f, R, R));
        graphics2D.translate(0, -(component.getHeight() - height) / 2);
    }

    private static boolean isEven(int value) {
        return value % 2 == 0;
    }
}

package com.plugin.samusprogressbar.progressbar;

import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBUI;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;

public class ProgressBarUI extends BasicProgressBarUI {

    private final IndeterminateBar indeterminateBar;

    private final DeterminateBar determinateBar;

    private static final int PROGRESS_BAR_HEIGHT = 20;

    public ProgressBarUI() {
        this.indeterminateBar = new IndeterminateBar();
        this.determinateBar = new DeterminateBar();
    }

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent component) {
        component.setBorder(JBUI.Borders.empty().asUIResource());
        return new ProgressBarUI();
    }

    @Override
    public Dimension getPreferredSize(JComponent component) {
        return new Dimension(super.getPreferredSize(component).width, JBUIScale.scale(PROGRESS_BAR_HEIGHT));
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        progressBar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
            }
        });
    }

    @Override
    protected void paintIndeterminate(Graphics graphics, JComponent component) {
        if (!(graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        Insets insets = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
        int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }
        final GraphicsConfig config = GraphicsUtil.setupAAPainting(graphics2D);

        indeterminateBar.drawIndeterminateBar(progressBar, component, graphics2D);

        if (progressBar.isStringPainted()) {
            if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
                paintString(graphics2D, insets.left, insets.top, barRectWidth, barRectHeight, boxRect.x, boxRect.width);
            } else {
                paintString(graphics2D, insets.left, insets.top, barRectWidth, barRectHeight, boxRect.y, boxRect.height);
            }
        }
        config.restore();
    }

    @Override
    protected void paintDeterminate(Graphics graphics, JComponent component) {
        if (!(graphics instanceof Graphics2D)) {
            return;
        }

        if (progressBar.getOrientation() != SwingConstants.HORIZONTAL || !component.getComponentOrientation().isLeftToRight()) {
            super.paintDeterminate(graphics, component);
            return;
        }

        final GraphicsConfig config = GraphicsUtil.setupAAPainting(graphics);
        Insets insets = progressBar.getInsets();
        int width = progressBar.getWidth();
        int height = progressBar.getPreferredSize().height;
        if (!isEven(component.getHeight() - height)) {
            height++;
        }
        int barRectWidth = width - (insets.right + insets.left);
        int barRectHeight = height - (insets.top + insets.bottom);
        int amountFull = getAmountFull(insets, barRectWidth, barRectHeight);
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        determinateBar.drawDeterminateBar(progressBar, graphics, component, width, height, amountFull);

        if (progressBar.isStringPainted()) {
            paintString(graphics, insets.left, insets.top, barRectWidth, barRectHeight, amountFull, insets);
        }
        config.restore();
    }

    private void paintString(Graphics graphics, int x, int y, int width, int height, int fillStart, int amountFull) {
        if (!(graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        String progressString = progressBar.getString();
        graphics2D.setFont(progressBar.getFont());
        Point renderLocation = getStringPlacement(graphics2D, progressString, x, y, width, height);
        Rectangle oldClip = graphics2D.getClipBounds();

        if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
            graphics2D.setColor(getSelectionBackground());
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(fillStart, y, amountFull, height);
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
        } else {
            graphics2D.setColor(getSelectionBackground());
            AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI / 2);
            graphics2D.setFont(progressBar.getFont().deriveFont(rotate));
            renderLocation = getStringPlacement(graphics2D, progressString, x, y, width, height);
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(x, fillStart, width, amountFull);
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
        }
        graphics2D.setClip(oldClip);
    }

    @Override
    protected int getBoxLength(int availableLength, int otherDimension) {
        return availableLength;
    }

    private static boolean isEven(int value) {
        return value % 2 == 0;
    }

}

package com.aapeli.multiuser;

import com.aapeli.colorgui.ColorButton;
import java.awt.Color;
import java.awt.Graphics;

public class RoundedUpperCornersButton extends ColorButton {

    private boolean roundedUpperCorners = false;

    public RoundedUpperCornersButton(String label) {
        super(label);
    }

    public void clearBackground(Graphics g, int var2, int var3) {
        g.fillRect(1, 1, var2 - 1, var3 - 1);
    }

    public void drawBorder(Graphics g, int width, int height) {
        boolean normalState = this.isNormalState();
        Color[] borderColors = this.getLightAndDarkBorderColors();
        if (!this.roundedUpperCorners) {
            g.setColor(normalState ? borderColors[0] : borderColors[1]);
            g.drawLine(0, 0, width - 2, 0);
            g.drawLine(0, 0, 0, height - 1);
            g.setColor(normalState ? borderColors[1] : borderColors[0]);
            g.drawLine(width - 1, height - 1, 1, height - 1);
            g.drawLine(width - 1, height - 1, width - 1, 0);
        } else {
            g.setColor(normalState ? borderColors[0] : borderColors[1]);
            g.drawLine(1, 0, width - 2, 0);
            g.drawLine(0, 1, 0, height - 1);
            g.setColor(normalState ? borderColors[1] : borderColors[0]);
            g.drawLine(width - 1, height - 1, 1, height - 1);
            g.drawLine(width - 1, height - 1, width - 1, 1);
        }
    }

    public void setRoundedUpperCorners() {
        this.roundedUpperCorners = true;
        this.repaint();
    }
}

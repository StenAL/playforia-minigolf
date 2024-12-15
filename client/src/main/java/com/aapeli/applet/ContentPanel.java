package com.aapeli.applet;

import com.aapeli.client.IPanel;
import java.awt.Dimension;

class ContentPanel extends IPanel {

    protected ContentPanel(AbstractGameFrame gameFrame) {
        this.setBackground(gameFrame.getBackground());
        this.setForeground(gameFrame.getForeground());
        this.setSize(gameFrame.contentWidth, gameFrame.contentHeight);
        this.setPreferredSize(new Dimension(gameFrame.contentWidth, gameFrame.contentHeight));
        this.setLayout(null);
    }

    protected void destroy() {
        this.setVisible(false);
        this.removeAll();
    }

    protected void makeVisible() {
        if (this.getComponentCount() > 0) {
            this.setVisible(true);
        }
    }
}

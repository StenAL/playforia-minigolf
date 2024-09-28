package com.aapeli.applet;

import com.aapeli.client.IPanel;
import java.awt.Dimension;

class ContentPanel extends IPanel {

    protected ContentPanel(AApplet applet) {
        this.setBackground(applet.getBackground());
        this.setForeground(applet.getForeground());
        this.setSize(applet.appletWidth, applet.appletHeight);
        this.setPreferredSize(new Dimension(applet.appletWidth, applet.appletHeight));
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

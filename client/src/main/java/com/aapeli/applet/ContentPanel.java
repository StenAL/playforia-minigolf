package com.aapeli.applet;

import com.aapeli.client.IPanel;


class ContentPanel extends IPanel {

    protected ContentPanel(AApplet applet) {
        this.setBackground(applet.getBackground());
        this.setForeground(applet.getForeground());
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

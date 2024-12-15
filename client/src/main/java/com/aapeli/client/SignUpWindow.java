package com.aapeli.client;

import com.aapeli.applet.AbstractGameFrame;
import java.awt.Component;

class SignUpWindow extends PopupWindow {

    private SignUpHandler signUpHandler;
    private SignUpPanel signUpPanel;

    protected SignUpWindow(AbstractGameFrame var1, SignUpHandler var2, int var3, int var4) {
        super(var1.textManager);
        this.signUpHandler = var2;
        this.signUpPanel = new SignUpPanel(var1, this, var3, var4);
    }

    protected void method239(Component var1) {
        String var2 = this.signUpPanel.method820();
        if (super.aTextManager187.isAvailable("Localized_GameClientName")) {
            var2 = var2 + " - " + super.aTextManager187.getGame("Localized_GameClientName");
        }

        this.method238(var1, var2, this.signUpPanel);
    }

    protected void close() {
        super.close();
        this.signUpHandler.method1603();
        this.signUpPanel = null;
        this.signUpHandler = null;
    }
}

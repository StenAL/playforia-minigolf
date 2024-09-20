package com.aapeli.colorgui;

import com.aapeli.client.IPanel;
import java.util.ArrayList;
import java.util.List;

public final class RadioButtonGroup {

    private List<IPanel> buttons;
    private boolean aBoolean1590;

    public RadioButtonGroup() {
        this(false);
    }

    public RadioButtonGroup(boolean var1) {
        this.buttons = new ArrayList<>();
        this.aBoolean1590 = var1;
    }

    protected void method1756(RadioButton button) {
        synchronized (this.buttons) {
            this.buttons.add(button);
        }
    }

    protected void method1757(RoundRadioButton button) {
        synchronized (this.buttons) {
            this.buttons.add(button);
        }
    }

    protected boolean method1758(boolean var1) {
        if (var1) {
            this.method1759();
            return true;
        } else {
            return this.aBoolean1590;
        }
    }

    private void method1759() {
        for (IPanel var2 : this.buttons) {
            if (var2 instanceof RadioButton) {
                ((RadioButton) var2).realSetState(false);
            } else {
                ((RoundRadioButton) var2).realSetState(false);
            }
        }
    }
}

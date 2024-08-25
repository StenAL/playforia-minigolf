package com.aapeli.colorgui;

import com.aapeli.client.IPanel;

import java.util.Vector;

public final class RadioButtonGroup {

    private Vector<IPanel> aVector1589;
    private boolean aBoolean1590;


    public RadioButtonGroup() {
        this(false);
    }

    public RadioButtonGroup(boolean var1) {
        this.aVector1589 = new Vector<>();
        this.aBoolean1590 = var1;
    }

    protected void method1756(RadioButton var1) {
        this.aVector1589.addElement(var1);
    }

    protected void method1757(RoundRadioButton var1) {
        this.aVector1589.addElement(var1);
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
        for (IPanel var2: this.aVector1589) {
            if (var2 instanceof RadioButton) {
                ((RadioButton) var2).realSetState(false);
            } else {
                ((RoundRadioButton) var2).realSetState(false);
            }
        }
    }
}

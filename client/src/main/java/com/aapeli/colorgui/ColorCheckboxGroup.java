package com.aapeli.colorgui;

import java.util.ArrayList;
import java.util.List;

public final class ColorCheckboxGroup {

    private List<ColorCheckbox> checkboxes = new ArrayList<>();

    protected void addCheckbox(ColorCheckbox checkbox) {
        synchronized(this.checkboxes) {
            this.checkboxes.add(checkbox);
        }
    }

    protected boolean method1748(boolean var1) {
        if (var1) {
            this.method1749();
            return true;
        } else {
            return false;
        }
    }

    private void method1749() {
        for (ColorCheckbox colorCheckbox : checkboxes) {
            colorCheckbox.realSetState(false);
        }
    }
}

package com.aapeli.colorgui;

import java.util.ArrayList;
import java.util.List;

public final class RadioButtonGroup {

    private final List<RadioButton> buttons;
    private final boolean canDeselectAllButtons;

    public RadioButtonGroup() {
        this(false);
    }

    public RadioButtonGroup(boolean canDeselectAllButtons) {
        this.buttons = new ArrayList<>();
        this.canDeselectAllButtons = canDeselectAllButtons;
    }

    protected void addButton(RadioButton button) {
        synchronized (this.buttons) {
            this.buttons.add(button);
        }
    }

    protected boolean trySetState(boolean selected) {
        if (selected) {
            this.deselectAllButtons();
            return true;
        } else {
            return this.canDeselectAllButtons;
        }
    }

    private void deselectAllButtons() {
        for (RadioButton button : this.buttons) {
            button.realSetState(false);
        }
    }
}

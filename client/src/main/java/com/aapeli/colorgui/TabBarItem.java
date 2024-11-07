package com.aapeli.colorgui;

import java.awt.Component;
import java.awt.Image;

public final class TabBarItem {

    private RadioButton button;
    private Component component;
    private int id;
    private boolean componentAutoSize;

    public TabBarItem(TabBar tabBar, String text, Component component) {
        this(tabBar, null, text, component);
    }

    public TabBarItem(TabBar tabBar, Image icon, String text, Component component) {
        this.button = tabBar.registerItem(icon, text);
        this.component = component;
        this.id = 0;
        this.componentAutoSize = false;
    }

    public void setTabId(int id) {
        this.id = id;
    }

    public int getTabId() {
        return this.id;
    }

    public void setComponentAutoSize(boolean autoSize) {
        this.componentAutoSize = autoSize;
    }

    public boolean isComponentAutoSize() {
        return this.componentAutoSize;
    }

    public RadioButton getButton() {
        return this.button;
    }

    public Component getComponent() {
        return this.component;
    }
}

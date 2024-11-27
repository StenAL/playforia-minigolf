package com.aapeli.client;

import java.awt.Component;

class TellFriendWindow extends PopupWindow {

    private TellFriendHandler tellFriendHandler;
    private TellFriendPanel tellFriendPanel;
    private static final String aString2892 = "TellFriend_Title";

    protected TellFriendWindow(TextManager var1, ImageManager var2, TellFriendHandler var3) {
        super(var1);
        this.tellFriendHandler = var3;
        this.tellFriendPanel = new TellFriendPanel(var1, var2, var3, this);
    }

    protected void method241(Component var1) {
        this.method238(var1, super.aTextManager187.getShared("TellFriend_Title"), this.tellFriendPanel);
    }

    protected void close() {
        super.close();
        this.tellFriendHandler.method1703();
        this.tellFriendPanel = null;
        this.tellFriendHandler = null;
    }
}

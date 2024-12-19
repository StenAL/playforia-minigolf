package com.aapeli.client;

import com.aapeli.frame.AbstractGameFrame;

class SignUpHandler {

    private AbstractGameFrame gameFrame;
    private SignUpWindow frame;
    private boolean aBoolean1365;

    protected SignUpHandler(AbstractGameFrame var1) {
        this.gameFrame = var1;
        this.aBoolean1365 = false;
    }

    protected boolean method1599(boolean var1) {
        try {
            return this.aBoolean1365 ? false : (this.gameFrame.param.getSession() != null ? false : false);
        } catch (Exception var3) {
            return false;
        }
    }

    protected boolean method1600() {
        try {
            if (this.aBoolean1365) {
                return false;
            } else if (this.gameFrame.param.getSession() == null) {
                return false;
            } else {
                this.frame = new SignUpWindow(this.gameFrame, this, 1, 1);
                this.frame.method239(this.gameFrame);
                this.aBoolean1365 = true;
                return true;
            }
        } catch (Exception var2) {
            return false;
        }
    }

    protected boolean method1601(int var1, int var2) {
        try {
            if (this.aBoolean1365) {
                return false;
            } else if (this.gameFrame.param.getSession() == null) {
                return false;
            } else if (var1 <= 0 && var2 != 0) {
                this.frame = new SignUpWindow(this.gameFrame, this, 1, 2);
                this.frame.method239(this.gameFrame);
                this.aBoolean1365 = true;
                return true;
            } else {
                return false;
            }
        } catch (Exception var4) {
            return false;
        }
    }

    protected void method1602() {
        try {
            if (this.frame != null) {
                this.frame.close();
            }
        } catch (Exception var2) {
        }
    }

    protected void method1603() {
        this.frame = null;
    }
}

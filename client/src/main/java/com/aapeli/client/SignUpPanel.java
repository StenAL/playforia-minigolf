package com.aapeli.client;

import com.aapeli.colorgui.RoundButton;
import com.aapeli.frame.AbstractGameFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SignUpPanel extends IPanel implements ActionListener {

    private static final Color aColor3205 = new Color(240, 240, 240);
    private static final Color aColor3206 = Color.black;
    private static final Color aColor3207 = new Color(128, 255, 128);
    private static final Color aColor3208 = new Color(240, 240, 96);
    private static final Font aFont3209 = new Font("Dialog", Font.PLAIN, 16);
    private AbstractGameFrame gameFrame;
    private SignUpWindow signUpWindow;
    private int anInt3212;
    private int anInt3213;
    private HtmlText aHtmlText3214;
    private RoundButton aRoundButton3215;
    private RoundButton aRoundButton3216;
    private RoundButton aRoundButton3217;

    protected SignUpPanel(AbstractGameFrame var1, SignUpWindow var2, int var3, int var4) {
        this.gameFrame = var1;
        this.signUpWindow = var2;
        this.anInt3212 = var3;
        this.anInt3213 = var4;
        this.setSize(450, 270);
        this.setBackground(aColor3205);
        this.setForeground(aColor3206);
        this.method821();
        this.setSharedBackground(var1.imageManager, "tf-background.gif", 0, 0);
    }

    public void update(Graphics g) {
        this.drawBackground(g);
        g.setFont(aFont3209);
        if (this.aHtmlText3214 == null) {
            String text = null;
            if (this.anInt3212 == 0) {
                if (this.anInt3213 == 1) {
                    text = "WS_ScoreNotSaved";
                } else if (this.anInt3213 == 2) {
                    text = "WM_StatsNotSaved";
                }
            } else if (this.anInt3212 == 1) {
                if (this.anInt3213 == 1) {
                    text = "RS_PersonalRecord";
                } else if (this.anInt3213 == 2) {
                    text = "RM_FirstRanking";
                }
            }

            text = this.gameFrame.textManager.getText("GameFin_" + text);
            this.aHtmlText3214 = new HtmlText(g, 410, text);
        }

        g.setColor(aColor3206);
        this.aHtmlText3214.print(g, 20, 45);
    }

    public void actionPerformed(ActionEvent var1) {
        this.signUpWindow.close();
        if (var1.getSource() == this.aRoundButton3215) {
            this.gameFrame.setEndState(AbstractGameFrame.END_QUIT_REGISTER);
            this.gameFrame.param.showRegisterPage();
        }
    }

    protected String method820() {
        return this.anInt3212 == 0
                ? this.gameFrame.textManager.getText("GameFin_W_GameOver")
                : (this.anInt3212 == 1 ? this.gameFrame.textManager.getText("GameFin_R_Congratulations") : "-");
    }

    private void method821() {
        this.setLayout(null);
        if (this.anInt3212 == 0) {
            this.aRoundButton3215 = new RoundButton(this.gameFrame.textManager.getText("GameFin_W_CreateAccount"));
            this.aRoundButton3215.setBounds(210, 225, 220, 30);
            this.aRoundButton3215.setBackground(aColor3207);
            this.aRoundButton3215.addActionListener(this);
            this.add(this.aRoundButton3215);
            this.aRoundButton3216 = new RoundButton(this.gameFrame.textManager.getText("GameFin_W_Continue"));
            this.aRoundButton3216.setBounds(20, 228, 160, 27);
            this.aRoundButton3216.setBackground(aColor3208);
            this.aRoundButton3216.addActionListener(this);
            this.add(this.aRoundButton3216);
        } else if (this.anInt3212 == 1) {
            this.aRoundButton3217 = new RoundButton(this.gameFrame.textManager.getText("GameFin_R_OK"));
            this.aRoundButton3217.setBounds(330, 228, 100, 27);
            this.aRoundButton3217.setBackground(aColor3207);
            this.aRoundButton3217.addActionListener(this);
            this.add(this.aRoundButton3217);
        }
    }
}

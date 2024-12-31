package com.aapeli.applet;

import com.aapeli.client.Parameters;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class AdCanvas extends Canvas implements MouseListener {

    private static final String linkTarget = "_blank";
    private AbstractGameFrame gameFrame;
    private LoadingPanel loadingPanel;
    private URL anURL117;
    private List<AdCanvasText> texts;
    private URI uri;
    private String aString120;
    private int anInt121;
    private Image anImage122;
    private boolean aBoolean123;
    private long aLong124;
    private boolean aBoolean125;
    private long aLong126;

    private AdCanvas(AbstractGameFrame var1, URL var2, List<AdCanvasText> var3, URI var4, String var5, int var6) {
        this.gameFrame = var1;
        this.anURL117 = var2;
        this.texts = var3;
        this.uri = var4;
        this.aString120 = var5;
        this.anInt121 = var6;
        this.aLong124 = 0L;
        this.aBoolean125 = false;
        this.anImage122 = null;
        this.aBoolean123 = false;
    }

    public void paint(Graphics var1) {
        this.update(var1);
    }

    public void update(Graphics var1) {
        if (this.aBoolean123) {
            if (!this.aBoolean125) {
                if (this.loadingPanel != null) {
                    Image var2 = this.loadingPanel.getImage();
                    if (var2 != null) {
                        Point var3 = this.getLocation();
                        var1.drawImage(var2, -var3.x, -var3.y, this);
                    }
                }

                var1.drawImage(this.anImage122, 0, 0, null);

                for (AdCanvasText var4 : this.texts) {
                    var4.method1548(var1);
                }
            } else {
                var1.setColor(Color.white);
                var1.fillRect(0, 0, 20, 20);
                var1.setColor(Color.red);
                var1.drawRect(0, 0, 19, 19);
                var1.drawLine(0, 0, 19, 19);
                var1.drawLine(0, 19, 19, 0);
            }
        }
    }

    public void mouseEntered(MouseEvent var1) {}

    public void mouseExited(MouseEvent var1) {}

    public void mousePressed(MouseEvent var1) {
        try {
            Desktop.getDesktop().browse(this.uri);
        } catch (IOException e) {
        }
    }

    public void mouseReleased(MouseEvent var1) {}

    public void mouseClicked(MouseEvent var1) {}

    protected static AdCanvas create(AbstractGameFrame gameFrame, Parameters parameters) {
        try {
            String var2 = parameters.getParameter("ad_image");
            URL var3 = new URL(new URL(parameters.getServerIp()), var2);
            List<AdCanvasText> var4 = new ArrayList<>();

            String var6;
            for (int var5 = 1; (var6 = parameters.getParameter("ad_text-" + var5)) != null; ++var5) {
                AdCanvasText var7 = AdCanvasText.method1547(var6);
                if (var7 != null) {
                    var4.add(var7);
                }
            }

            String var14 = parameters.getParameter("ad_page");
            URI uri = var14 != null ? new URI(var14) : null;
            String var9 = parameters.getParameter("ad_target");
            if (var9 == null) {
                var9 = linkTarget;
            }

            int var10 = 10;
            String var11 = parameters.getParameter("ad_mintime");
            if (var11 != null) {
                var10 = Integer.parseInt(var11);
            }

            return new AdCanvas(gameFrame, var3, var4, uri, var9, var10);
        } catch (Exception var13) {
            return null;
        }
    }

    protected void method212() {
        this.anImage122 = Toolkit.getDefaultToolkit().createImage(anURL117); // this.gameFrame.getImage(this.anURL117);
    }

    protected boolean method213() {
        if (this.aLong124 <= 0L) {
            this.aLong124 = System.currentTimeMillis();
        } else if (System.currentTimeMillis() > this.aLong124 + 15000L) {
            this.aBoolean125 = true;
        }

        if (!this.aBoolean125 && !this.gameFrame.prepareImage(this.anImage122, null)) {
            return false;
        } else {
            if (!this.aBoolean125) {
                int var1 = this.anImage122.getWidth(null);
                int var2 = this.anImage122.getHeight(null);
                this.setSize(var1, var2);
            } else {
                this.setSize(20, 20);
            }

            if (this.uri != null) {
                this.addMouseListener(this);
                this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            this.aBoolean123 = true;
            return true;
        }
    }

    protected void method214(LoadingPanel loadingPanel) {
        this.loadingPanel = loadingPanel;
        this.aLong126 = System.currentTimeMillis();
        this.repaint();
    }

    protected int method215() {
        if (this.aBoolean125) {
            return 0;
        } else {
            int var1 = (int) (System.currentTimeMillis() - this.aLong126);
            int var2 = this.anInt121 * 1000 - var1;
            return Math.max(var2, 0);
        }
    }

    protected boolean method216() {
        if (this.uri == null) {
            return false;
        } else {
            String var1 = this.aString120.toLowerCase();
            return var1.equals("_self") ? false : (var1.equals("_parent") ? false : !var1.equals("_top"));
        }
    }

    protected void method217() {
        this.aBoolean123 = false;
        this.removeMouseListener(this);
        if (this.anImage122 != null) {
            this.anImage122.flush();
            this.anImage122 = null;
        }

        this.anURL117 = null;
        this.uri = null;
        this.aString120 = null;
        this.loadingPanel = null;
        this.gameFrame = null;
    }
}

package com.aapeli.colorgui;

import com.aapeli.client.IPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

public class TabBar extends IPanel implements ComponentListener, ActionListener {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.lightGray;
    private static final Color DEFAULT_BORDER_COLOR = Color.black;
    private static final Color DEFAULT_BUTTON_BACKGROUND_COLOR = Color.blue;
    private static final Color DEFAULT_BUTTON_FOREGROUND_COLOR = Color.white;
    private static final Font DEFAULT_FONT = FontConstants.font;
    private int width;
    private int height;
    private Color backgroundColor;
    private Color buttonBorderColor;
    private Color buttonBackgroundColor;
    private Color buttonForegroundColor;
    private Font buttonFont;
    private Image backgroundImage;
    private int backgroundImageOffsetX;
    private int backgroundImageOffsetY;
    private RadioButtonGroup buttonGroup;
    private List<TabBarItem> items;
    private int itemsCount;
    private int selectedTabIndex;
    private int borderStyle;
    private int preferredHeight;
    private List<TabBarListener> listeners;
    private Object lock = new Object();

    public TabBar(int width, int height) {
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setBorderColor(DEFAULT_BORDER_COLOR);
        this.setButtonFont(DEFAULT_FONT);
        this.setButtonBackground(DEFAULT_BUTTON_BACKGROUND_COLOR);
        this.setButtonForeground(DEFAULT_BUTTON_FOREGROUND_COLOR);
        this.borderStyle = 2;
        this.buttonGroup = new RadioButtonGroup();
        this.items = new ArrayList<>();
        this.itemsCount = 0;
        this.selectedTabIndex = -1;
        this.addComponentListener(this);
        this.listeners = new ArrayList<>();
        this.setLayout(null);
        this.preferredHeight = 0;
    }

    public void addNotify() {
        super.addNotify();
        this.repaint();
    }

    public void paint(Graphics g) {
        this.update(g);
    }

    public void update(Graphics g) {
        if (this.backgroundImage == null) {
            this.drawBackground(g);
        } else if (this.backgroundImageOffsetX == 0 && this.backgroundImageOffsetY == 0) {
            g.drawImage(this.backgroundImage, 0, 0, this);
        } else {
            g.drawImage(
                    this.backgroundImage,
                    0,
                    0,
                    this.width,
                    this.height,
                    this.backgroundImageOffsetX,
                    this.backgroundImageOffsetY,
                    this.backgroundImageOffsetX + this.width,
                    this.backgroundImageOffsetY + this.height,
                    this);
        }

        if (this.borderStyle != 0) {
            g.setColor(this.buttonBorderColor);
            if (this.borderStyle == 2) {
                g.drawRect(0, this.preferredHeight - 2, this.width - 1, this.height - this.preferredHeight + 1);
                g.drawRect(1, this.preferredHeight - 1, this.width - 3, this.height - this.preferredHeight - 1);
            } else {
                g.drawRect(0, this.preferredHeight - 1, this.width - 1, this.height - this.preferredHeight);
            }
        }
    }

    public void componentShown(ComponentEvent e) {}

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
        Dimension size = this.getSize();
        this.width = size.width;
        this.height = size.height;
        this.relayout();
        this.repaint();
    }

    public void actionPerformed(ActionEvent event) {
        synchronized (this.lock) {
            int id = this.getTabIndex(event);
            this.setSelectedIndex(id);
            this.notifySelectedTabChanged(this.selectedTabIndex);
        }
    }

    public void setBorderColor(Color borderColor) {
        synchronized (this.lock) {
            this.buttonBorderColor = borderColor;
            for (int i = 0; i < this.itemsCount; ++i) {
                this.getButtonByIndex(i).setBorderColor(borderColor);
            }
        }

        this.repaint();
    }

    public void setButtonFont(Font font) {
        synchronized (this.lock) {
            this.buttonFont = font;

            for (int i = 0; i < this.itemsCount; ++i) {
                this.getButtonByIndex(i).setFont(font);
            }
        }
    }

    public void setButtonBackground(Color background) {
        synchronized (this.lock) {
            this.buttonBackgroundColor = background;

            for (int i = 0; i < this.itemsCount; ++i) {
                this.getButtonByIndex(i).setBackground(background);
            }
        }
    }

    public void setButtonForeground(Color foreground) {
        synchronized (this.lock) {
            this.buttonForegroundColor = foreground;

            for (int i = 0; i < this.itemsCount; ++i) {
                this.getButtonByIndex(i).setForeground(foreground);
            }
        }
    }

    public void setBackground(Color background) {
        this.backgroundColor = background;
        super.setBackground(this.backgroundColor);
        this.repaint();
    }

    public void setBorder(int border) {
        synchronized (this.lock) {
            this.borderStyle = border;

            for (int i = 0; i < this.itemsCount; ++i) {
                this.getButtonByIndex(i).setBorder(border);
            }
        }
    }

    public void addTab(String text, Component component) {
        synchronized (this.lock) {
            this.addTab(new TabBarItem(this, text, component));
        }
    }

    public void addTab(TabBarItem item) {
        synchronized (this.lock) {
            this.items.add(item);
            ++this.itemsCount;
            this.relayout();
            RadioButton button = item.getButton();
            this.add(button);
            if (this.itemsCount == 1) {
                button.setState(true);
                this.add(item.getComponent());
                this.selectedTabIndex = 0;
            }
        }

        this.repaint();
    }

    public TabBarItem getTabBarItemByIndex(int i) {
        return this.items.get(i);
    }

    public TabBarItem getTabBarItemById(int id) {
        synchronized (this.lock) {
            for (int i = 0; i < this.itemsCount; ++i) {
                TabBarItem item = this.getTabBarItemByIndex(i);
                if (item.getTabId() == id) {
                    return item;
                }
            }

            return null;
        }
    }

    public TabBarItem[] getTabBarItems() {
        synchronized (this.lock) {
            TabBarItem[] items = new TabBarItem[this.itemsCount];
            items = this.items.toArray(items);
            return items;
        }
    }

    public void setTabTitle(int id, String text) {
        this.getButtonByIndex(id).setLabel(text);
        this.relayout();
    }

    public int getSelectedTabIndex() {
        synchronized (this.lock) {
            for (int i = 0; i < this.itemsCount; ++i) {
                if (this.getButtonByIndex(i).getState()) {
                    return i;
                }
            }

            return -1;
        }
    }

    public void setSelectedIndex(int index) {
        synchronized (this.lock) {
            if (index != this.selectedTabIndex) {
                TabBarItem newSelectedTab = this.getTabBarItemByIndex(index);
                TabBarItem previousSelectedTab = this.getTabBarItemByIndex(this.selectedTabIndex);
                newSelectedTab.getButton().setState(true);
                this.remove(previousSelectedTab.getComponent());
                this.add(newSelectedTab.getComponent());
                this.selectedTabIndex = index;
            }
        }
    }

    public void setBackgroundImage(Image image) {
        this.setBackgroundImage(image, 0, 0);
    }

    public void setBackgroundImage(Image image, int backgroundImageOffsetX, int backgroundImageOffsetY) {
        this.backgroundImage = image;
        this.backgroundImageOffsetX = backgroundImageOffsetX;
        this.backgroundImageOffsetY = backgroundImageOffsetY;
        this.repaint();
    }

    public void addTabBarListener(TabBarListener listener) {
        synchronized (this.lock) {
            this.listeners.add(listener);
        }
    }

    public void removeTabBarListener(TabBarListener listener) {
        synchronized (this.lock) {
            this.listeners.remove(listener);
        }
    }

    protected RadioButton registerItem(Image icon, String text) {
        RadioButton button = new RadioButton(text, this.buttonGroup, false);
        button.setIconImage(icon);
        button.setFont(this.buttonFont);
        button.setBackground(this.buttonBackgroundColor);
        button.setForeground(this.buttonForegroundColor);
        button.setBorderColor(this.buttonBorderColor);
        button.setBorder(this.borderStyle);
        button.addActionListener(this);
        return button;
    }

    private void relayout() {
        int preferredWidth = 0;

        for (int i = 0; i < this.itemsCount; ++i) {
            RadioButton button = this.getButtonByIndex(i);
            preferredWidth += 2 + button.getPreferredSize().width + 2;
        }

        double widthToPreferredWidth = (double) this.width / (double) preferredWidth;
        if (widthToPreferredWidth > 1.2D) {
            widthToPreferredWidth = 1.2D;
        }

        int maxHeight = 0;

        int y;
        for (int i = 0; i < this.itemsCount; ++i) {
            y = this.getTabBarItemByIndex(i).getComponent().getSize().height;
            if (y > maxHeight) {
                maxHeight = y;
            }
        }

        this.preferredHeight = this.height - 1 - 1 - maxHeight;
        if (this.preferredHeight < 15) {
            this.preferredHeight = 15;
        } else if (this.preferredHeight > 30) {
            this.preferredHeight = 30;
        }

        int x = 2;

        for (int i = 0; i < this.itemsCount; ++i) {
            RadioButton button = this.getButtonByIndex(i);
            int width = (int) ((double) button.getPreferredSize().width * widthToPreferredWidth + 0.5D);
            button.setBounds(x, 0, width, this.preferredHeight);
            x += width + 2;
        }

        int borderWidth = this.borderStyle == 0 ? 0 : (this.borderStyle == 1 ? 1 : 2);

        for (int i = 0; i < this.itemsCount; ++i) {
            TabBarItem item = this.getTabBarItemByIndex(i);
            Component component = item.getComponent();
            component.setLocation(borderWidth, this.preferredHeight);
            if (item.isComponentAutoSize()) {
                component.setSize(
                        this.width - borderWidth - borderWidth, this.height - this.preferredHeight - borderWidth);
            }
        }
    }

    private int getTabIndex(ActionEvent event) {
        Object source = event.getSource();

        for (int i = 0; i < this.itemsCount; ++i) {
            if (source == this.getButtonByIndex(i)) {
                return i;
            }
        }

        return -1;
    }

    private void notifySelectedTabChanged(int tabIndex) {
        synchronized (this.lock) {
            if (this.listeners.size() != 0) {
                for (TabBarListener tabBarListener : listeners) {
                    tabBarListener.selectedTabChanged(tabIndex);
                }
            }
        }
    }

    private RadioButton getButtonByIndex(int i) {
        return this.getTabBarItemByIndex(i).getButton();
    }
}

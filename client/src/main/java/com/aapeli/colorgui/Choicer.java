package com.aapeli.colorgui;

import com.aapeli.client.IPanel;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class Choicer extends IPanel implements ComponentListener, ItemListener, ItemSelectable {

    private Choice choice = new Choice();
    private ColorSpinner colorSpinner;
    private boolean choiceMode = true;
    private List<ItemListener> listeners;
    private Object lock = new Object();

    public Choicer() {
        this.choice.setBackground(Color.white);
        this.choice.setForeground(Color.black);
        this.choice.addItemListener(this);
        this.listeners = new ArrayList<>();
        this.setLayout(null);
        this.choice.setLocation(0, 0);
        this.add(this.choice);
        this.addComponentListener(this);
    }

    public void componentHidden(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
        synchronized (this.lock) {
            Dimension size = this.getSize();
            if (this.choiceMode) {
                this.choice.setSize(size);
            } else {
                this.colorSpinner.setSize(size);
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        synchronized (this.listeners) {
            if (!this.listeners.isEmpty()) {
                e = new ItemEvent(this, e.getID(), e.getItem(), e.getStateChange());
                for (ItemListener listener : this.listeners) {
                    listener.itemStateChanged(e);
                }
            }
        }
    }

    public Object[] getSelectedObjects() {
        synchronized (this.lock) {
            return this.choiceMode ? this.choice.getSelectedObjects() : this.colorSpinner.getSelectedObjects();
        }
    }

    public void setBackground(Color color) {
        synchronized (this.lock) {
            super.setBackground(color);
            if (this.choiceMode) {
                this.choice.setBackground(color);
            } else {
                this.colorSpinner.setBackground(color);
            }
        }
    }

    public void setForeground(Color color) {
        synchronized (this.lock) {
            super.setForeground(color);
            if (this.choiceMode) {
                this.choice.setForeground(color);
            } else {
                this.colorSpinner.setForeground(color);
            }
        }
    }

    public void addItem(String text) {
        synchronized (this.lock) {
            if (this.choiceMode) {
                this.moveToSpinnerIfNecessary(text);
            }

            if (this.choiceMode) {
                this.choice.addItem(text);
            } else {
                this.colorSpinner.addItem(text);
            }
        }
    }

    public void removeItem(int i) {
        synchronized (this.lock) {
            if (this.choiceMode) {
                this.choice.remove(i);
            } else {
                this.colorSpinner.removeItem(i);
            }
        }
    }

    public int getItemCount() {
        synchronized (this.lock) {
            return this.choiceMode ? this.choice.getItemCount() : this.colorSpinner.getItemCount();
        }
    }

    public int getSelectedIndex() {
        synchronized (this.lock) {
            return this.choiceMode ? this.choice.getSelectedIndex() : this.colorSpinner.getSelectedIndex();
        }
    }

    public void select(int i) {
        this.setSelectedIndex(i);
    }

    public void setSelectedIndex(int i) {
        synchronized (this.lock) {
            if (this.choiceMode) {
                this.choice.select(i);
            } else {
                this.colorSpinner.setSelectedIndex(i);
            }
        }
    }

    public void addItemListener(ItemListener listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
    }

    public void removeItemListener(ItemListener var1) {
        synchronized (this.listeners) {
            this.listeners.remove(var1);
        }
    }

    public void moveToSpinner() {
        synchronized (this.lock) {
            if (this.choiceMode) {
                this.colorSpinner = new ColorSpinner();
                this.colorSpinner.setLocation(0, 0);
                this.colorSpinner.setSize(this.getSize());
                this.colorSpinner.setBackground(this.choice.getBackground());
                this.colorSpinner.setForeground(this.choice.getForeground());
                int items = this.choice.getItemCount();

                for (int i = 0; i < items; ++i) {
                    this.colorSpinner.addItem(this.choice.getItem(i));
                }

                int selectedIndex = this.choice.getSelectedIndex();
                if (selectedIndex >= 0) {
                    this.colorSpinner.setSelectedIndex(selectedIndex);
                }

                this.choice.removeItemListener(this);
                this.remove(this.choice);
                this.add(this.colorSpinner);
                this.colorSpinner.addItemListener(this);
                this.choiceMode = false;
                this.choice = null;
            }
        }
    }

    public ColorSpinner getColorSpinner() {
        return this.colorSpinner;
    }

    public boolean isChoiceMode() {
        return this.choiceMode;
    }

    private void moveToSpinnerIfNecessary(String text) {
        char[] chars = text.toCharArray();

        for (char c : chars) {
            if (c > 255) {
                this.moveToSpinner();
                return;
            }
        }
    }
}

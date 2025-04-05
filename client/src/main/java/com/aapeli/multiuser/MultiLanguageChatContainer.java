package com.aapeli.multiuser;

import com.aapeli.client.IPanel;
import com.aapeli.colorgui.TabBar;
import com.aapeli.colorgui.TabBarItem;
import com.aapeli.colorgui.TabBarListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import org.moparforia.shared.Language;

public final class MultiLanguageChatContainer extends IPanel implements ComponentListener, TabBarListener {

    private ChatBase chatBase;
    private Languages languages;
    private int width;
    private int height;
    private ChatTextArea chatTextArea;
    private Language language;
    private TabBar tabBar;
    private Object lock;

    protected MultiLanguageChatContainer(ChatBase chatBase, ChatTextArea chatTextArea, Language language) {
        this.chatBase = chatBase;
        this.lock = new Object();
        this.languages = new Languages(chatBase.imageManager);
        Dimension size = chatTextArea.getSize();
        this.width = size.width;
        this.height = size.height;
        this.setSize(this.width, this.height);
        this.setLayout(null);
        chatTextArea.setLocation(0, 0);
        this.add(chatTextArea);
        this.chatTextArea = chatTextArea;
        this.language = language;
        this.addComponentListener(this);
    }

    public void componentShown(ComponentEvent e) {}

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
        synchronized (this.lock) {
            Dimension size = this.getSize();
            this.width = size.width;
            this.height = size.height;
            if (this.tabBar == null) {
                this.chatTextArea.setSize(this.width, this.height);
            } else {
                this.tabBar.setSize(this.width, this.height);
            }
        }
    }

    public void selectedTabChanged(int tabIndex) {
        TabBarItem item = this.tabBar.getTabBarItemByIndex(tabIndex);
        ChatTextArea chatArea = (ChatTextArea) item.getComponent();
        this.chatBase.setChatTextArea(chatArea);
    }

    protected Language getLanguage() {
        synchronized (this.lock) {
            if (this.tabBar == null) {
                return this.language;
            } else {
                TabBarItem tabBarItem = this.tabBar.getTabBarItemByIndex(this.tabBar.getSelectedTabIndex());
                return Language.fromId(tabBarItem.getTabId());
            }
        }
    }

    protected void createLanguageChatArea(Language language) {
        this.getOrCreateChatTextArea(language);
    }

    protected void addMessage(Language language, String user, String message) {
        ChatTextArea chatTextArea = this.getOrCreateChatTextArea(language);
        chatTextArea.addSay(user, message);
    }

    protected void addMessage(Language language, String text) {
        ChatTextArea chatTextArea = this.getOrCreateChatTextArea(language);
        chatTextArea.addMessage(text);
    }

    protected void addSheriffSay(String text) {
        ChatTextArea[] chats = this.getAllChats();

        for (ChatTextArea chatTextArea : chats) {
            chatTextArea.addSheriffSay(text);
        }
    }

    protected void addServerSay(String text) {
        ChatTextArea[] chats = this.getAllChats();

        for (ChatTextArea chatTextArea : chats) {
            chatTextArea.addServerSay(text);
        }
    }

    protected void addLocalizedServerSay(String text) {
        ChatTextArea[] chats = this.getAllChats();

        for (ChatTextArea chatTextArea : chats) {
            chatTextArea.addLocalizedServerSay(text);
        }
    }

    protected void addBroadcastMessage(String text) {
        ChatTextArea[] chats = this.getAllChats();

        for (ChatTextArea chatTextArea : chats) {
            chatTextArea.addBroadcastMessage(text);
        }
    }

    public void clear() {
        ChatTextArea[] chats = this.getAllChats();

        for (ChatTextArea chatTextArea : chats) {
            chatTextArea.clear();
        }
    }

    private ChatTextArea getOrCreateChatTextArea(Language language) {
        synchronized (this.lock) {
            if (this.tabBar == null) {
                if (language == this.language) {
                    return this.chatTextArea;
                }

                this.removeAll();
                this.tabBar = new TabBar(this.width, this.height);
                this.tabBar.setLocation(0, 0);
                this.tabBar.setBackground(super.getBackground());
                this.tabBar.setButtonBackground(Color.lightGray);
                this.tabBar.setButtonForeground(Color.black);
                this.tabBar.setBorder(1);
                this.add(this.tabBar);
                this.tabBar.addTabBarListener(this);
                this.addTabBarLanguageItem(this.language, this.chatTextArea);
                this.chatTextArea = null;
            }

            TabBarItem tabBarItem = this.tabBar.getTabBarItemById(language.getId());
            if (tabBarItem != null) {
                return (ChatTextArea) tabBarItem.getComponent();
            } else {
                ChatTextArea chatTextArea =
                        (ChatTextArea) this.tabBar.getTabBarItemByIndex(0).getComponent();
                chatTextArea = new ChatTextArea(
                        chatTextArea.getTextManager(),
                        chatTextArea.getBadWordFilter(),
                        this.width,
                        this.height - 15,
                        chatTextArea.getFont());
                chatTextArea.setLocation(0, 15);
                this.addTabBarLanguageItem(language, chatTextArea);
                return chatTextArea;
            }
        }
    }

    private void addTabBarLanguageItem(Language language, ChatTextArea chatTextArea) {
        chatTextArea.setBorderStyle(0);
        TabBarItem item = new TabBarItem(
                this.tabBar,
                this.languages.getFlag(language),
                chatTextArea.getTextManager().getText("Language_" + language),
                chatTextArea);
        item.setTabId(language.getId());
        item.setComponentAutoSize(true);
        this.tabBar.addTab(item);
    }

    private ChatTextArea[] getAllChats() {
        synchronized (this.lock) {
            if (this.tabBar == null) {
                return new ChatTextArea[] {this.chatTextArea};
            } else {
                TabBarItem[] items = this.tabBar.getTabBarItems();
                int length = items.length;
                ChatTextArea[] chatTextAreas = new ChatTextArea[length];

                for (int i = 0; i < length; ++i) {
                    chatTextAreas[i] = (ChatTextArea) items[i].getComponent();
                }

                return chatTextAreas;
            }
        }
    }
}

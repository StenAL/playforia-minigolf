package com.aapeli.multiuser;

import com.aapeli.client.BadWordFilter;
import com.aapeli.client.IPanel;
import com.aapeli.client.ImageManager;
import com.aapeli.client.InputTextField;
import com.aapeli.client.InputTextFieldListener;
import com.aapeli.client.Parameters;
import com.aapeli.client.TextManager;
import com.aapeli.client.UrlLabel;
import com.aapeli.colorgui.ColorButton;
import com.aapeli.colorgui.RoundButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;

public abstract class ChatBase extends IPanel implements ComponentListener, UserListHandler, ActionListener, InputTextFieldListener {

    public static final int CIDR_NONE = 0;
    public static final int CIDR_UNREG = 1;
    public static final int CIDR_UNCONF = 2;
    private static final Color sayButtonColor = new Color(144, 144, 224);
    private static boolean shouldDisplayChatInputHelp = true;
    public Parameters param;
    public TextManager textManager;
    public ImageManager imageManager;
    private BadWordFilter badWordFilter;
    private FloodProtection floodProtection;
    public int width;
    public int height;
    private Image image;
    private int anInt2351;
    private int anInt2352;
    private int anInt2353;
    private int anInt2354;
    private String aString2355;
    private int chatDisabledStatus;
    public UserList userList;
    public ChatTextArea chatTextArea;
    public GlobalTextArea gui_globaloutput;
    public InputTextField inputTextField;
    public Component sayButton;
    public UrlLabel signupMessage;
    private String aString2357;
    private Vector<ChatListener> chatListeners;
    private Object synchronizedObject;

    public ChatBase(Parameters parameters, TextManager textManager, ImageManager imageManager, BadWordFilter badWordFilter, boolean useSmallFont, boolean var6, int width, int height) {
        this(parameters, textManager, imageManager, badWordFilter, true, true, useSmallFont, var6, false, width, height);
    }

    public ChatBase(Parameters parameters, TextManager textManager, ImageManager imageManager, BadWordFilter badWordFilter, boolean var5, boolean var6, boolean useSmallFont, boolean var8, int width, int height) {
        this(parameters, textManager, imageManager, badWordFilter, var5, var6, useSmallFont, var8, false, width, height);
    }

    public ChatBase(Parameters params, TextManager textManager, ImageManager imageManager, BadWordFilter badWordFilter, boolean var5, boolean var6, boolean useSmallFont, boolean var8, boolean shouldNotWriteWelcomeMessage, int width, int height) {
        this.param = params;
        this.textManager = textManager;
        this.imageManager = imageManager;
        this.badWordFilter = badWordFilter;
        this.synchronizedObject = new Object();
        this.floodProtection = new FloodProtection();
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.aString2355 = null;
        this.chatDisabledStatus = 0;
        this.init(var5, var6, useSmallFont, var8, shouldNotWriteWelcomeMessage);
        this.addComponentListener(this);
        this.chatListeners = new Vector<>();
    }

    public void update(Graphics g) {
        if (this.image != null) {
            g.drawImage(this.image, 0, 0, this.width, this.height, this.anInt2351, this.anInt2352, this.anInt2351 + this.width, this.anInt2352 + this.height, this);
        } else {
            this.drawBackground(g);
        }

    }

    public void componentShown(ComponentEvent var1) {
    }

    public void componentHidden(ComponentEvent var1) {
    }

    public void componentMoved(ComponentEvent var1) {
        if (this.image != null) {
            Point var2 = this.getLocation();
            int var3 = var2.x - this.anInt2353;
            int var4 = var2.y - this.anInt2354;
            this.setBackgroundImage(this.image, this.anInt2351 + var3, this.anInt2352 + var4);
        }

    }

    public void componentResized(ComponentEvent event) {
        Dimension size = this.getSize();
        this.width = size.width;
        this.height = size.height;
        this.resizeLayout();
    }

    public void openPlayerCard(String var1) {
        this.param.showPlayerCard(var1);
    }

    public void adminCommand(String var1, String var2) {
        ChatListener[] var3 = this.getChatListenersCopy();

        for (ChatListener chatListener : var3) {
            chatListener.localUserAdminCommand(var1, var2);
        }

    }

    public void adminCommand(String var1, String var2, String var3) {
        ChatListener[] var4 = this.getChatListenersCopy();

        for (ChatListener chatListener : var4) {
            chatListener.localUserAdminCommand(var1, var2, var3);
        }

    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.sayButton) {
            this.sendMessage();
        }

    }

    public void startedTyping() {
        this.aString2357 = this.getMessageRecipient();
    }

    public void clearedField() {
        this.aString2357 = null;
    }

    public void enterPressed() {
        this.sendMessage();
    }

    public static UrlLabel setInputByCIDR(int var0, Container var1, InputTextField var2, Component var3, UrlLabel var4, TextManager var5, Parameters var6) {
        return getSignupMessage(var0, var1, var2, var3, var4, var5.getShared("Chat_NoGuestChatAndRegNote"), var5.getShared("Chat_NoUnconfirmedChatNote"), var6);
    }

    public void setBackground(Color var1) {
        if (this.userList != null) {
            this.userList.setBackground(var1);
        }

        if (this.signupMessage != null) {
            this.signupMessage.setBackground(var1);
        }

        this.repaint();
    }

    public void setForeground(Color var1) {
        if (this.userList != null) {
            this.userList.setForeground(var1);
        }

        if (this.signupMessage != null) {
            this.signupMessage.setForeground(var1);
        }

    }

    public void setBackgroundImage(Image var1, int var2, int var3) {
        this.image = var1;
        this.anInt2351 = var2;
        this.anInt2352 = var3;
        Point var4 = this.getLocation();
        this.anInt2353 = var4.x;
        this.anInt2354 = var4.y;
        Point var5 = this.userList.getLocation();
        this.userList.setBackgroundImage(var1, var2 + var5.x, var3 + var5.y);
        this.repaint();
    }

    public void addChatListener(ChatListener var1) {
        this.chatListeners.addElement(var1);
    }

    public void removeChatListener(ChatListener var1) {
        this.chatListeners.removeElement(var1);
    }

    public void setMessageMaximumLength(int var1) {
        this.inputTextField.setTextMaximumLength(var1);
    }

    public void clearOutput() {
        Object var1 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.gui_globaloutput == null) {
                this.chatTextArea.clear();
            } else {
                this.gui_globaloutput.clear();
            }

        }
    }

    public void enablePopUp(boolean isModerator, boolean isAdmin) {
        this.userList.enablePopUp(isModerator, isAdmin);
    }

    public void addPlainMessage(String var1) {
        this.chatTextArea.addPlainMessage(var1);
    }

    public void addMessage(String var1) {
        this.chatTextArea.addMessage(var1);
    }

    public void addHighlightMessage(String var1) {
        this.chatTextArea.addHighlightMessage(var1);
    }

    public void addErrorMessage(String var1) {
        this.chatTextArea.addErrorMessage(var1);
    }

    public void addLine() {
        this.chatTextArea.addLine();
    }

    public int setFullUserList(String[] list) {
        return this.setFullUserList(list, -1);
    }

    public int setFullUserList(String[] list, int var2) {
        this.userList.removeAllUsers();
        int l = list.length;

        for (int i = 0; i < l; ++i) {
            this.addToUserList(list[i], var2 == i);
        }

        return l;
    }

    public String localUserJoin(String var1) {
        this.addToUserList(var1, true);
        return this.aString2355;
    }

    public void userSay(String var1, String var2) {
        if (!this.isUserIgnored(var1)) {
            this.chatTextArea.addSay(var1, var2);
        }

    }

    public void userSay(int var1, String var2, String var3) {
        if (!this.isUserIgnored(var2)) {
            this.gui_globaloutput.method916(var1, var2, var3);
        }

    }

    public void userSayPrivately(String var1, String var2) {
        if (!this.isUserIgnored(var1)) {
            this.chatTextArea.addSayPrivately(var1, this.aString2355, var2);
        }

    }

    public void sheriffSay(String var1) {
        Object var2 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.gui_globaloutput == null) {
                this.chatTextArea.addSheriffSay(var1);
            } else {
                this.gui_globaloutput.method918(var1);
            }

        }
    }

    public void serverSay(String var1) {
        if (var1.startsWith("L10N:")) {
            String var3 = null;
            int var5 = var1.indexOf(59);
            String var4;
            String var12;
            if (var5 == -1) {
                var12 = var1.substring(5);
                var4 = this.textManager.getShared(var12);
            } else {
                var12 = var1.substring(5, var5);
                int var6 = var1.indexOf(59, var5 + 1);
                if (var6 == -1) {
                    var3 = var1.substring(var5 + 1);
                    var4 = this.textManager.getShared(var12, var3);
                } else {
                    var3 = var1.substring(var5 + 1, var6);
                    String var7 = var1.substring(var6 + 1);
                    var4 = this.textManager.getShared(var12, var3, var7);
                }
            }

            if (var4.length() > 1) {
                if ((var12.equals("ServerSay_SheriffGaveWarning") || var12.equals("ServerSay_SheriffMutedUser") || var12.equals("ServerSay_SheriffUnMutedUser")) && var3 != null && !this.userList.isUser(var3)) {
                    return;
                }

                Object var13 = this.synchronizedObject;
                synchronized (this.synchronizedObject) {
                    if (this.gui_globaloutput == null) {
                        this.chatTextArea.addLocalizedServerSay(var4);
                    } else {
                        this.gui_globaloutput.method920(var4);
                    }
                }
            }

        } else {
            Object var2 = this.synchronizedObject;
            synchronized (this.synchronizedObject) {
                if (this.gui_globaloutput == null) {
                    this.chatTextArea.addServerSay(var1);
                } else {
                    this.gui_globaloutput.method919(var1);
                }

            }
        }
    }

    public void broadcastMessage(String message) {
        Object var2 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.gui_globaloutput == null) {
                this.chatTextArea.addBroadcastMessage(message);
            } else {
                this.gui_globaloutput.method921(message);
            }

        }
    }

    public boolean isUserInLobby(String var1) {
        return this.isUserInChat(var1);
    }

    public boolean isUserInChat(String var1) {
        return this.userList.getUser(var1) != null;
    }

    public boolean isUserIgnored(String user) {
        UserListItem userItem = this.userList.getUser(user);
        return userItem == null ? true : userItem.isIgnore();
    }

    public UserList getUserList() {
        return this.userList;
    }

    public boolean useRoundButtons() {
        Object var1 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.sayButton instanceof RoundButton) {
                return false;
            } else {
                RoundButton var2 = this.copyColorButtonToRoundButton(this.sayButton);
                var2.setVisible(this.sayButton.isVisible());
                this.sayButton = var2;
                this.userList.usePixelRoundedButtonsAndCheckBoxes();
                return true;
            }
        }
    }

    public void setOutputToGlobal(int var1) {
        Object var2 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.gui_globaloutput == null) {
                Point var3 = this.chatTextArea.getLocation();
                this.remove(this.chatTextArea);
                this.gui_globaloutput = new GlobalTextArea(this, this.chatTextArea, var1);
                this.gui_globaloutput.setLocation(var3.x, var3.y);
                this.add(this.gui_globaloutput);
            }
        }
    }

    public void disableChatInput(int status) {
        this.chatDisabledStatus = status;
        this.paintSignupMessage();
    }

    public boolean isTyping() {
        return this.inputTextField.isTyping();
    }

    public abstract void resizeLayout();

    public String addToUserList(String var1, boolean var2) {
        return this.addToUserListNew(var1, var2).getNick();
    }

    public UserListItem addToUserListNew(String var1, boolean var2) {
        UserListItem var3 = this.userList.addUser(var1, var2);
        if (var2) {
            this.aString2355 = var3.getNick();
        }

        Object var4 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.gui_globaloutput != null) {
                this.gui_globaloutput.method915(var3.getLanguage());
            }

            return var3;
        }
    }

    public RoundButton copyColorButtonToRoundButton(Component var1) {
        ColorButton var2 = (ColorButton) var1;
        var2.removeActionListener(this);
        this.remove(var2);
        RoundButton var3 = new RoundButton(var2.getLabel());
        var3.setBounds(var2.getBounds());
        var3.setBackground(var2.getBackground());
        var3.addActionListener(this);
        this.add(var3);
        return var3;
    }

    public String getRegisterationNeededText() {
        return this.textManager.getShared("Chat_NoGuestChatAndRegNote");
    }

    public String getConfirmationNeededText() {
        return this.textManager.getShared("Chat_NoUnconfirmedChatNote");
    }

    public void setCurrentOutput(ChatTextArea var1) {
        this.chatTextArea = var1;
    }

    protected void method889(UserListItem var1, String var2) {
        Object var3 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.gui_globaloutput == null) {
                this.chatTextArea.addMessage(var2);
            } else {
                this.gui_globaloutput.method917(var1.getLanguage(), var2);
            }

        }
    }

    protected void method890(UserListItem var1, UserListItem var2, String var3) {
        Object var4 = this.synchronizedObject;
        synchronized (this.synchronizedObject) {
            if (this.gui_globaloutput == null) {
                this.chatTextArea.addMessage(var3);
            } else {
                int var5 = var1.getLanguage();
                int var6 = var2.getLanguage();
                this.gui_globaloutput.method917(var5, var3);
                if (var6 != var5) {
                    this.gui_globaloutput.method917(var6, var3);
                }
            }

        }
    }

    private static UrlLabel getSignupMessage(int chatDisabledStatus, Container container, InputTextField inputTextField, Component sayButton, UrlLabel signupMessage, String registrationNeededText, String confirmationNeededText, Parameters parameters) {
        if (chatDisabledStatus == 0) {
            if (signupMessage != null) {
                signupMessage.setVisible(false);
            }

            inputTextField.setVisible(true);
            sayButton.setVisible(true);
            return signupMessage;
        } else {
            if (signupMessage == null) {
                signupMessage = new UrlLabel(parameters.getApplet());
                Point inputFieldLocation = inputTextField.getLocation();
                Point sayButtonLocation = sayButton.getLocation();
                Dimension sayButtonSize = sayButton.getSize();
                signupMessage.setBounds(inputFieldLocation.x, inputFieldLocation.y, sayButtonLocation.x + sayButtonSize.width - inputFieldLocation.x, sayButtonLocation.y + sayButtonSize.height - inputFieldLocation.y);
                signupMessage.setBackground(container.getBackground());
                signupMessage.setForeground(container.getForeground());
                container.add(signupMessage);
            }

            inputTextField.setVisible(false);
            sayButton.setVisible(false);
            if (chatDisabledStatus == 1) {
                signupMessage.setText(registrationNeededText, parameters.getRegisterPage());
                signupMessage.setTarget(0);
            } else if (chatDisabledStatus == 2) {
                signupMessage.setText(confirmationNeededText, null);
            } else {
                signupMessage.setText(null, null);
            }

            signupMessage.setVisible(true);
            return signupMessage;
        }
    }

    private void init(boolean var1, boolean var2, boolean useSmallFont, boolean var4, boolean shouldNotWriteWelcomeMessage) {
        this.setLayout(null);
        this.chatTextArea = new ChatTextArea(this.textManager, this.badWordFilter, 200, 100, useSmallFont ? ChatTextArea.SMALL_FONT : ChatTextArea.DEFAULT_FONT);
        if (shouldDisplayChatInputHelp && !shouldNotWriteWelcomeMessage) {
            this.chatTextArea.addWelcomeMessage(this.textManager.getShared("Chat_Welcome"));
        }

        this.add(this.chatTextArea);
        if (shouldDisplayChatInputHelp) {
            this.inputTextField = new InputTextField(this.textManager.getShared("Chat_InputHelp"), 200, true);
            shouldDisplayChatInputHelp = false;
        } else {
            this.inputTextField = new InputTextField(200, true);
        }

        this.inputTextField.addInputTextFieldListener(this);
        this.add(this.inputTextField);
        ColorButton sayButton = new ColorButton(this.textManager.getShared("Chat_SayButton"));
        sayButton.setBackground(sayButtonColor);
        sayButton.addActionListener(this);
        this.add(sayButton);
        this.sayButton = sayButton;
        this.userList = new UserList(this, this.textManager, this.imageManager, !var4, var1, var2);
        this.userList.setChatReference(this);
        this.add(this.userList);
        this.signupMessage = new UrlLabel(this.param.getApplet());
        this.add(this.signupMessage);
        this.paintSignupMessage();
    }

    private void sendMessage() {
        if (this.aString2355 != null) {
            String message = this.inputTextField.getText().trim();
            if (message.length() != 0) {
                if (!this.floodProtection.isOkToSay(message)) {
                    this.chatTextArea.addFloodMessage();
                } else {
                    String var2 = this.getMessageRecipient();
                    String var3 = this.aString2357;
                    this.aString2357 = null;
                    if (var2 == null && var3 != null && !this.isUserInChat(var3)) {
                        this.chatTextArea.addPrivateMessageUserLeftMessage(var3);
                    } else {
                        message = this.inputTextField.getInputText();
                        if (message.length() > 0) {
                            ChatListener[] var4 = this.getChatListenersCopy();
                            if (var2 != null) {
                                for (ChatListener chatListener : var4) {
                                    chatListener.localUserSayPrivately(var2, message);
                                }

                                this.chatTextArea.addOwnSayPrivately(this.aString2355, var2, message);
                                return;
                            }

                            Object var5 = this.synchronizedObject;
                            synchronized (this.synchronizedObject) {
                                int var6;
                                if (this.gui_globaloutput == null) {
                                    for (var6 = 0; var6 < var4.length; ++var6) {
                                        var4[var6].localUserSay(message);
                                    }
                                } else {
                                    var6 = this.gui_globaloutput.method914();

                                    for (ChatListener chatListener : var4) {
                                        ((GlobalChatListener) chatListener).localUserSay(var6, message);
                                    }
                                }
                            }

                            this.chatTextArea.addOwnSay(this.aString2355, message);
                        }

                    }
                }
            }
        }
    }

    private String getMessageRecipient() {
        UserListItem user = this.userList.getSelectedUser();
        return user != null && user.isPrivately() ? user.getNick() : null;
    }

    private ChatListener[] getChatListenersCopy() {
        int chatListenersCount = this.chatListeners.size();
        ChatListener[] chatListeners = new ChatListener[chatListenersCount];

        for (int i = 0; i < chatListenersCount; ++i) {
            chatListeners[i] = this.chatListeners.elementAt(i);
        }

        return chatListeners;
    }

    private void paintSignupMessage() {
        this.signupMessage = getSignupMessage(this.chatDisabledStatus, this, this.inputTextField, this.sayButton, this.signupMessage, this.getRegisterationNeededText(), this.getConfirmationNeededText(), this.param);
    }
}

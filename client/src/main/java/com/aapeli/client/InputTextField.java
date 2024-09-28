package com.aapeli.client;

import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputTextField extends TextField implements FocusListener, KeyListener, ActionListener {

    private static final Color colourBackground = Color.white;
    private static final Color colourText = Color.black;
    private static final Color colourDefaultText = new Color(160, 160, 160);
    private int maxLength;
    private String inputText;
    private boolean showDefaultText;
    private boolean hasFocus;
    private int replacedCharacterAtEnd;
    private boolean shouldReplaceNextChar;
    private boolean enabled;
    private List<String> userInput;
    private int userInputCount;
    private int selectedPastInputIndex;
    private String finalInput;
    private int inputTextLength;
    private List<InputTextFieldListener> listeners;

    public InputTextField(int maxLength) {
        this("", maxLength, false);
    }

    public InputTextField(String defaultText, int maxLength) {
        this(defaultText, maxLength, false);
    }

    public InputTextField(int maxLength, boolean enabled) {
        this("", maxLength, enabled);
    }

    public InputTextField(String defaultText, int maxLength, boolean enabled) {
        this.maxLength = maxLength;
        this.setText(defaultText);
        this.inputText = "";
        this.showDefaultText = defaultText.length() > 0;
        this.hasFocus = false;
        this.replacedCharacterAtEnd = 0;
        this.shouldReplaceNextChar = false;
        this.enabled = enabled;
        if (enabled) {
            this.userInput = new ArrayList<>();
            this.userInputCount = 0;
            this.selectedPastInputIndex = 0;
        }

        this.setBackground(colourBackground);
        this.setForeground(this.showDefaultText ? colourDefaultText : colourText);
        this.setEditable(true);
        this.addFocusListener(this);
        this.addKeyListener(this);
        this.addActionListener(this);
        this.inputTextLength = 0;
        this.listeners = new ArrayList<>();
    }

    public void focusGained(FocusEvent e) {
        if (this.showDefaultText) {
            this.clearDefaultText();
        }

        this.hasFocus = true;
    }

    public void focusLost(FocusEvent e) {
        this.hasFocus = false;
    }

    public void keyPressed(KeyEvent e) {
        this.keyInput();
    }

    public void keyReleased(KeyEvent evt) {
        this.keyInput();
        if (this.enabled) {
            int keyCode = evt.getKeyCode();
            if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                synchronized (this.userInput) {
                    if (this.userInputCount == 0) {
                        return;
                    }

                    if (keyCode == KeyEvent.VK_UP) {
                        if (this.selectedPastInputIndex == 0) {
                            return;
                        }

                        if (this.selectedPastInputIndex == this.userInputCount) {
                            this.finalInput = this.getText();
                        }

                        --this.selectedPastInputIndex;
                    } else {
                        if (this.selectedPastInputIndex == this.userInputCount) {
                            return;
                        }

                        ++this.selectedPastInputIndex;
                    }

                    String text;
                    if (this.selectedPastInputIndex < this.userInputCount) {
                        text = this.userInput.get(this.selectedPastInputIndex);
                    } else {
                        text = this.finalInput;
                    }

                    this.setText(text);
                    this.setCaretPosition(text.length());
                    return;
                }
            }
        }

        this.method965(evt);
    }

    public void keyTyped(KeyEvent evt) {
        this.keyInput();
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == this) {
            synchronized (this) {
                for (InputTextFieldListener listener : this.listeners) {
                    listener.enterPressed();
                }
            }
        }
    }

    public String getInputText() {
        return this.getInputText(true);
    }

    public String getInputText(boolean clear) {
        String userInput;
        synchronized (this) {
            if (this.showDefaultText) {
                this.clearDefaultText();
                return "";
            }

            if (this.shouldReplaceNextChar) {
                if (this.replacedCharacterAtEnd == -1) {
                    this.replaceInputText(this.getText(), this.getCaretPosition(), 0, "~");
                }

                this.shouldReplaceNextChar = false;
            }

            userInput = this.getText().trim();
            if (userInput.length() > this.maxLength) {
                userInput = this.inputText;
            }

            if (clear) {
                this.clear();
            }
        }

        if (this.enabled) {
            synchronized (this.userInput) {
                if (this.userInputCount >= 50) {
                    this.userInput.removeFirst();
                    --this.userInputCount;
                }

                this.userInput.add(userInput);
                ++this.userInputCount;
                this.selectedPastInputIndex = this.userInputCount;
            }
        }

        return userInput;
    }

    public synchronized void clear() {
        this.setText("");
        this.inputText = "";
        this.inputTextLength = 0;
    }

    public void noClearOnFirstFocus() {
        this.showDefaultText = false;
        this.setForeground(colourText);
    }

    public boolean haveFocus() {
        return this.hasFocus;
    }

    public void setTextMaximumLength(int limit) {
        this.maxLength = limit;
    }

    public boolean isTyping() {
        return !this.showDefaultText && this.getText().length() > 0;
    }

    public void addInputTextFieldListener(InputTextFieldListener listener) {
        synchronized (this) {
            this.listeners.add(listener);
        }
    }

    private void clearDefaultText() {
        this.showDefaultText = false;
        this.setForeground(colourText);
        this.clear();
    }

    private synchronized void keyInput() {
        String text = this.getText();
        int textLen = text.length();
        if (textLen <= this.maxLength) {
            this.inputText = text;
        } else {
            int lastCharIndex = this.getCaretPosition() - 1;
            int inputTextLen = this.inputText.length();
            if (lastCharIndex < 0) {
                lastCharIndex = 0;
            } else if (lastCharIndex > inputTextLen) {
                lastCharIndex = inputTextLen;
            }

            this.setText(this.inputText);
            this.setCaretPosition(lastCharIndex);
        }

        if (this.inputTextLength == 0 && textLen > 0) {
            for (InputTextFieldListener listener : this.listeners) {
                listener.startedTyping();
            }
        }

        if (this.inputTextLength > 0 && textLen == 0) {
            for (InputTextFieldListener listener : this.listeners) {
                listener.clearedField();
            }
        }

        this.inputTextLength = textLen;
    }

    private synchronized void method965(KeyEvent evt) {
        if (this.replacedCharacterAtEnd != 1) {
            int keyCode = evt.getKeyCode();
            if (keyCode < 16 || keyCode > 18) {
                char chr = evt.getKeyChar();
                if (chr >= ' ' && chr <= 255) {
                    if (this.shouldReplaceNextChar) {
                        this.replaceChar(chr);
                        this.shouldReplaceNextChar = false;
                    } else {
                        this.shouldReplaceNextChar = chr == '~';
                    }

                } else {
                    this.shouldReplaceNextChar = false;
                }
            }
        }
    }

    private void replaceChar(char chr) {
        String replacement;
        if (chr == ' ') {
            replacement = "~";
        } else if (chr == 'N') {
            replacement = "Ñ";
        } else if (chr == 'A') {
            replacement = "Ã";
        } else if (chr == 'O') {
            replacement = "Õ";
        } else if (chr == 'n') {
            replacement = "ñ";
        } else if (chr == 'a') {
            replacement = "ã";
        } else if (chr == 'o') {
            replacement = "õ";
        } else if (chr == '~') {
            replacement = "~~";
        } else {
            replacement = "~" + chr;
        }

        String text = this.getText();
        int caretPosition = this.getCaretPosition();
        if (this.replacedCharacterAtEnd == 0) {
            if (text.substring(0, caretPosition).endsWith(replacement)) {
                this.replacedCharacterAtEnd = 1;
                return;
            }

            this.replacedCharacterAtEnd = -1;
        }

        if (chr == '~') {
            this.replaceInputText(text, caretPosition, 0, replacement);
        } else if (caretPosition > 0) {
            if (caretPosition == 0) {
                return;
            }

            if (chr != text.charAt(caretPosition - 1)) {
                return;
            }

            this.replaceInputText(text, caretPosition, 1, replacement);
        }
    }

    private void replaceInputText(String text, int currentPosition, int var3, String replacement) {
        int replacementLength = replacement.length();
        if (text.length() - var3 + replacementLength <= this.maxLength) {
            this.setText(text.substring(0, currentPosition - var3) + replacement + text.substring(currentPosition));
            this.setCaretPosition(currentPosition - var3 + replacementLength);
        }
    }
}

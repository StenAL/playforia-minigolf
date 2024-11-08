package agolf;

import com.aapeli.colorgui.Choicer;
import com.aapeli.multiuser.UsernameValidator;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.moparforia.shared.Locale;

class TrackTestLoginPanel extends Panel implements ActionListener, KeyListener, ItemListener {

    private GameApplet gameApplet;
    private int width;
    private int height;
    private Locale locale;
    private TextField textFieldName;
    private TextField textFieldPassword;
    private Button buttonOk;
    private Label labelError;
    private Label labelName;
    private Choicer languageChoicer;

    protected TrackTestLoginPanel(GameApplet gameApplet, int width, int height) {
        this.gameApplet = gameApplet;
        this.width = width;
        this.height = height;
        this.locale = gameApplet.param.getLocale();
        this.setSize(width, height);
        this.create();
    }

    public void addNotify() {
        super.addNotify();
        this.repaint();
    }

    public void paint(Graphics g) {
        this.update(g);
    }

    public void update(Graphics g) {
        g.setColor(GameApplet.colourGameBackground);
        g.fillRect(0, 0, this.width, this.height);
    }

    public void actionPerformed(ActionEvent evt) {
        String username = this.textFieldName.getText().trim();
        String password = this.textFieldPassword.getText().trim();
        // String password = '';
        this.gameApplet.trackTestLogin(username, password, locale);
    }

    public void keyPressed(KeyEvent evt) {}

    private void create() {
        this.setLayout(null);
        this.textFieldName = new TextField(""); // ("(name)");
        this.textFieldName.setBounds(this.width / 2 - 75, this.height / 2 - 60, 150, 25);
        this.textFieldName.setBackground(Color.white);
        this.textFieldName.setForeground(Color.black);
        textFieldName.addKeyListener(this);
        this.add(this.textFieldName);
        textFieldName.requestFocus();

        this.textFieldPassword = new TextField(""); // ("(password)");
        this.textFieldPassword.setBounds(this.width / 2 - 75, this.height / 2 - 10, 150, 25);
        this.textFieldPassword.setBackground(Color.white);
        this.textFieldPassword.setForeground(Color.black);
        textFieldPassword.setEchoChar('*');

        this.languageChoicer = new Choicer();
        this.languageChoicer.setBounds(this.width / 2 - 75, this.height / 2 - 10, 150, 25);
        this.languageChoicer.addItem("English");
        this.languageChoicer.addItem("Finnish");
        this.languageChoicer.addItem("Swedish");
        this.languageChoicer.setBackground(Color.white);
        this.languageChoicer.setForeground(Color.black);
        this.languageChoicer.addItemListener(this);
        int selectedLanguageIndex =
                switch (locale) {
                    case EN_US -> 0;
                    case FI_FI -> 1;
                    case SV_SE -> 2;
                };
        this.languageChoicer.setSelectedIndex(selectedLanguageIndex);
        this.add(this.languageChoicer);

        this.buttonOk = new Button("OK");
        this.buttonOk.setBounds(this.width / 2 - 75, this.height / 2 + 50, 75, 25);
        this.buttonOk.addActionListener(this);
        this.add(this.buttonOk);

        labelError = new Label("Only spaces, alphabetical and numerical characters are allowed");
        labelError.setBounds(width / 2 - 75, height / 2 - 35, 400, 25);
        labelError.setForeground(Color.red);
        labelError.setVisible(false);
        add(labelError);
        labelName = new Label("Nickname:");
        labelName.setBounds(width / 2 - 200, height / 2 - 60, 75, 25);
        add(labelName);
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && buttonOk.isEnabled()) {
            actionPerformed(null);
            return;
        }
        boolean validUsername = UsernameValidator.isValidUsername(textFieldName.getText());
        if (validUsername) {
            labelError.setVisible(false);
            buttonOk.setEnabled(true);
        } else {
            labelError.setVisible(true);
            buttonOk.setEnabled(false);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == this.languageChoicer) {
            switch (this.languageChoicer.getSelectedIndex()) {
                case 0:
                    this.locale = Locale.EN_US;
                    break;
                case 1:
                    this.locale = Locale.FI_FI;
                    break;
                case 2:
                    this.locale = Locale.SV_SE;
                    break;
            }
        }
    }
}

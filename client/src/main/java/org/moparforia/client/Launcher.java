package org.moparforia.client;

import agolf.GameApplet;
import com.aapeli.multiuser.UsernameValidator;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.moparforia.shared.Locale;
import org.moparforia.shared.ManifestVersionProvider;
import picocli.CommandLine;

@CommandLine.Command(
        description = "Starts Minigolf Client",
        name = "client",
        mixinStandardHelpOptions = true,
        versionProvider = ManifestVersionProvider.class)
public class Launcher implements Callable<Integer> {

    public static final String DEFAULT_SERVER = "127.0.0.1";
    public static final int DEFAULT_PORT = 4242;

    // CLI options
    @CommandLine.Option(
            names = {"--hostname", "-ip"},
            description = "Sets server hostname",
            defaultValue = "")
    private String hostname;

    @CommandLine.Option(
            names = {"--port", "-p"},
            description = "Sets server port",
            defaultValue = "0")
    private int port;

    @CommandLine.Option(
            names = {"--lang", "-l"},
            description = "Sets language of the game, available values:\n ${COMPLETION-CANDIDATES}",
            defaultValue = "en_us")
    private Locale locale;

    @CommandLine.Option(
            names = {"--username", "-u"},
            description = "Sets the username to use when connecting to the server")
    private String username;

    @CommandLine.Option(
            names = {"--verbose", "-v"},
            description = "Set if you want verbose information")
    private static boolean verbose = false;

    @CommandLine.Option(
            names = {"--norandom", "-n"},
            description = "Set if you want to disable randomization for shots")
    private static boolean norandom = false;

    public static boolean debug() {
        return verbose;
    }

    public static boolean isUsingCustomServer() {
        return true; // instance.serverBox.isSelected();
    }

    public static void main(String... args) {
        Launcher launcher = new Launcher();
        new CommandLine(launcher).setCaseInsensitiveEnumValuesAllowed(true).execute(args);
    }

    public boolean showSettingDialog(JFrame frame, String server, int port) throws ParseException {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(4, 1));

        final JTextField serverField = new JTextField();
        JLabel serverLabel = new JLabel("Hostname:");
        serverField.setText(server);
        pane.add(serverLabel);
        pane.add(serverField);

        JSpinner portSpinner = new JSpinner();
        portSpinner.setModel(new SpinnerNumberModel(port, 1, Integer.MAX_VALUE, 1));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(portSpinner, "#");
        // Align spinner values to the left
        editor.getTextField().setHorizontalAlignment(JTextField.LEFT);
        portSpinner.setEditor(editor);
        JLabel portLabel = new JLabel("Port:");
        pane.add(portLabel);
        pane.add(portSpinner);

        int result = JOptionPane.showConfirmDialog(frame, pane, "Choose a server", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            this.hostname = serverField.getText();
            portSpinner.commitEdit();
            this.port = (Integer) portSpinner.getValue();
            return true;
        } else {
            return false;
        }
    }

    private String[] login(JFrame frame) {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(2, 2));
        JLabel userLabel = new JLabel("user");
        JLabel passLabel = new JLabel("pass");
        final JTextField userField = new JTextField();
        final JPasswordField passField = new JPasswordField();

        pane.add(userLabel);
        pane.add(userField);
        pane.add(passLabel);
        pane.add(passField);

        int option = JOptionPane.showConfirmDialog(frame, pane, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            return new String[] {user, pass};
        } else {
            return new String[] {null, null};
        }
    }

    @Override
    public Integer call() throws Exception {
        JFrame frame = createFrame();
        if (hostname.isEmpty() || port == 0) {
            // Determine which of these was actually false
            String temp_hostname = hostname.isEmpty() ? DEFAULT_SERVER : hostname;
            int temp_port = port == 0 ? DEFAULT_PORT : port;
            if (!showSettingDialog(frame, temp_hostname, temp_port)) {
                System.err.println("Server needs to be specified for minigolf to run");
                return 1;
            }
        }

        if (username != null) {
            if (!UsernameValidator.isValidUsername(username)) {
                System.err.println("Invalid username: Only spaces, alphabetical and numerical characters are allowed");
                return 2;
            }
        }

        launchGame(frame, hostname, port, locale, username, verbose, norandom);
        return null;
    }

    public JFrame createFrame() throws IOException {
        JFrame frame = new JFrame();
        frame.setTitle("Minigolf");
        Image img = loadIcon();
        frame.setIconImage(img);
        return frame;
    }

    public GameApplet launchGame(
            JFrame frame,
            String hostname,
            int port,
            Locale locale,
            String username,
            boolean verbose,
            boolean norandom) {
        return new GameApplet(frame, hostname, port, locale, username, verbose, norandom);
    }

    public Image loadIcon() throws IOException {
        return ImageIO.read(getClass().getResource("/icons/playforia.png"));
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

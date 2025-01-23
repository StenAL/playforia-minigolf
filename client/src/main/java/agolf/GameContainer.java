package agolf;

import agolf.game.GamePanel;
import agolf.lobby.LobbyPanel;
import com.aapeli.client.BadWordFilter;
import com.aapeli.client.ImageManager;
import com.aapeli.client.Parameters;
import com.aapeli.client.SoundManager;
import com.aapeli.client.TextManager;
import com.aapeli.tools.Tools;

public class GameContainer { // some kind of a container for everything

    public GolfGameFrame golfGameFrame;
    public Parameters params;
    public TextManager textManager;
    public SoundManager soundManager;
    public ImageManager imageManager;
    public SpriteManager spriteManager;
    public BadWordFilter badWordFilter;
    public TrackCollection trackCollection;
    public GolfConnection connection;
    public LobbySelectPanel lobbySelectionPanel;
    public LobbyPanel lobbyPanel;
    public String defaultLobby;
    public boolean disableSinglePlayer;
    public SynchronizedBool synchronizedTrackTestMode;
    public boolean safeMode;
    public GamePanel gamePanel;
    public int graphicsQualityIndex;

    public GameContainer(GolfGameFrame golfGameFrame, Parameters params) {
        this.golfGameFrame = golfGameFrame;
        this.params = params;
        this.graphicsQualityIndex = 3;
        this.synchronizedTrackTestMode = new SynchronizedBool();
        this.init();
    }

    protected void destroy() {
        if (this.connection != null) {
            this.connection.disconnect();
        }
    }

    private void init() {
        this.disableSinglePlayer = this.params.getParameter("disablespgames") != null;
        if (!this.disableSinglePlayer) {
            this.disableSinglePlayer = this.params.getParameter("disablespgame") != null;
        }

        this.defaultLobby = this.params.getParameter("lobby");
        this.synchronizedTrackTestMode.set(Tools.getBoolean(this.params.getParameter("tracktestmode")));
    }
}

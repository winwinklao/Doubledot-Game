import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.net.URL;
import javax.sound.sampled.*;


public class Board extends JPanel implements ActionListener {

    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    private final Font bigFont = new Font("Helvetica", Font.BOLD, 28);
    private Color mazeColor;

    private boolean inGame = false;
    private boolean dying = false;
    private boolean howto_play = false;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 20;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;

    private final int INTRO_ANIM_DELAY = 8;
    private final int INTRO_ANIM_COUNT = 8;
    private int introAnimCount = INTRO_ANIM_DELAY;
    private int introAnimDir = 1;
    private int introAnimPos = 0;

    private final int HTP_ANIM_DELAY = 10;
    private final int HTP_ANIM_COUNT = 10;
    private int htpAnimCount = HTP_ANIM_DELAY;
    private int htpAnimDir = 1;
    private int htpAnimPos = 0;

    private final int MAN_ANIM_DELAY = 2;
    private final int MAN_ANIM_COUNT = 4;
    private int manAnimCount = MAN_ANIM_DELAY;
    private int manAnimDir = 1;
    private int manAnimPos = 0;
    private final int MAN_SPEED = 6;

    private final int WIN_ANIM_DELAY = 8;
    private final int WIN_ANIM_COUNT = 3;
    private int winAnimCount = WIN_ANIM_DELAY;
    private int winAnimDir = 1;
    private int winAnimPos = 0;

    private final int MAX_GHOSTS = 10;
    private int ghostAnimPos = 0;
    private int N_GHOSTS = 6;

    private int Life;
    private int score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

    private Image oghost1, oghost2, oghost3;
    private Image tghost1, tghost2, tghost3;
    private Image trghost1, trghost2, trghost3;
    private Image fghost1, fghost2, fghost3;
    private Image man, man1, man1up, man1left, man1right, man1down;
    private Image man2up, man2down, man2left, man2right;
    private Image man3up, man3down, man3left, man3right;
    private Image man1stand, man2stand;
    private Image ji, gold, intro1, intro2, intro3, intro4, intro5, intro6, intro7, intro8;
    private Image over1, over2, over3, over4, over5, over6, over7, over8;
    private Image win1, win2, win3;
    private Image htp1, htp2, htp3, htp4, htp5, htp6, htp7, htp8, htp9, htp10;

    private int man_x, man_y, mand_x, mand_y;
    private int req_dx, req_dy, view_dx, view_dy;

    private AudioPlayer bgM , bgA;

    private final short levelData1[] = {
        19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        21, 0 , 0 , 0 , 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21, 0 , 0 , 0 , 17, 16, 16, 16, 16, 16, 16, 16, 24, 24, 24, 24, 16, 16, 16, 20,
        21, 0 , 0 , 0 , 17, 16, 16, 24, 16, 16, 16, 20, 0 , 0 , 0 , 0 , 17, 16, 16, 20,
        17, 18, 18, 18, 16, 16, 20, 0 , 17, 16, 16, 16, 18, 18, 18, 18, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20, 0 , 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 24, 20,
        25, 16, 16, 16, 24, 24, 28, 0 , 25, 24, 24, 16, 16, 16, 16, 16, 16, 20, 0 , 21,
        1 , 17, 16, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 16, 16, 16, 16, 16, 20, 0 , 21,
        1 , 17, 16, 16, 18, 18, 22, 0 , 19, 18, 18, 16, 16, 16, 16, 16, 16, 20, 0 , 21,
        1 , 17, 16, 16, 16, 16, 20, 0 , 17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0 , 21,
        1 , 17, 16, 16, 16, 16, 20, 0 , 17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0 , 21,
        1 , 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 24, 24, 24, 24, 16, 20, 0 , 21,
        1 , 17, 16, 16, 16, 16, 16, 16, 16, 24, 16, 20, 0 , 0 , 0 , 0 , 17, 20, 0 , 21,
        1 , 17, 16, 16, 16, 16, 16, 16, 20, 0 , 25, 28, 0 , 0 , 0 , 0 , 17, 20, 0 , 21,
        1 , 17, 16, 24, 24, 16, 16, 16, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 20, 0 , 21,
        1 , 17, 20, 0 , 0 , 17, 16, 16, 16, 18, 18, 22, 0 , 0 , 0 , 0 , 17, 20, 0 , 21,
        1 , 17, 20, 0 , 0 , 17, 16, 16, 16, 16, 16, 20, 0 , 0 , 0 , 0 , 17, 20, 0 , 21,
        1 , 17, 16, 18, 18, 16, 16, 16, 16, 16, 16, 16, 18, 18, 18, 18, 16, 20, 0 , 21,
        1 , 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 18, 20,
        9 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 25, 24, 24, 24, 24, 24, 24, 24, 24, 28,
    };
    private final short levelData2[] = {
        19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 16, 16, 16, 24, 16, 16, 20,
        17, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 16, 16, 16, 16, 28, 0 , 25, 16, 20,
        17, 20, 0 , 19, 18, 18, 18, 18, 18, 18, 16, 16, 16, 16, 20, 0 , 0 , 0 , 17, 20,
        17, 20, 0 , 17, 16, 16, 24, 24, 24, 24, 24, 24, 24, 16, 16, 22, 0 , 19, 16, 20,
        17, 20, 0 , 17, 16, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 16, 16, 26, 16, 16, 20,
        17, 20, 0 , 17, 16, 20, 0 , 19, 18, 18, 18, 22, 0 , 17, 16, 28, 0 , 25, 16, 20,
        17, 20, 0 , 25, 16, 20, 0 , 17, 16, 16, 16, 20, 0 , 17, 20, 0 , 0 , 0 , 17, 20,
        17, 20, 0 , 0 , 17, 20, 0 , 17, 16, 16, 16, 20, 0 , 17, 16, 22, 0 , 19, 16, 20,
        17, 16, 30, 0 , 17, 20, 0 , 17, 16, 16, 16, 16, 26, 16, 16, 16, 18, 16, 16, 20,
        17, 20, 0 , 0 , 17, 20, 0 , 17, 16, 16, 16, 20, 0 , 17, 16, 24, 24, 24, 16, 20,
        17, 16, 18, 18, 16, 20, 0 , 25, 24, 24, 24, 28, 0 , 17, 20, 0 , 0 , 0 , 17, 20,
        17, 16, 24, 16, 16, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 20, 0 , 0 , 0 , 17, 20,
        17, 20, 0 , 17, 16, 16, 18, 18, 18, 18, 18, 18, 18, 16, 16, 22, 0 , 19, 16, 20,
        17, 20, 0 , 17, 16, 16, 16, 16, 24, 24, 24, 24, 16, 16, 16, 20, 0 , 17 ,16, 20,
        17, 20, 0 , 17, 16, 16, 16, 20, 0 , 0 , 0 , 0 , 17, 16, 24, 16, 18, 16, 16, 20,
        17, 20, 0 , 25, 24, 24, 24, 16, 18, 18, 22, 0 , 25, 28, 0 , 17, 16, 16, 16, 20,
        17, 20, 0 , 0 , 0 , 0 , 0 , 17, 16, 16, 20, 0 , 0 , 0 , 0 , 17, 16, 16, 16, 20,
        17, 16, 18, 18, 18, 18, 18, 16, 16, 16, 16, 18, 18, 18, 18, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 24, 24, 24, 24, 24 ,24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    private final short levelData3[] = {
        19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 26, 18, 26, 22,
        25, 16, 16, 16, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 20, 0 , 21, 0 , 21,
        1 , 17, 16, 16, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21, 0 , 21, 0 , 21,
        1 , 17, 16, 16, 16, 18, 18, 18, 22, 0 , 0 , 19, 18, 18, 18, 20, 0 , 21, 0 , 21,
        1 , 17, 16, 16, 16, 16, 16, 16, 20, 0 , 0 , 17, 16, 16, 16, 20, 0 , 21, 0 , 21,
        1 , 17, 24, 24, 24, 16, 16, 16, 16, 18, 18, 16, 16, 16, 24, 20, 0 , 25, 26, 20,
        1 , 21, 0 , 0 , 0 , 17, 16, 16, 24, 24, 24, 16, 16, 20, 0 , 21, 0 , 0 , 0 , 21,
        1 , 21, 0 , 0 , 0 , 17, 16, 20, 0 , 0 , 0 , 17, 16, 20, 0 , 17, 18, 18, 18, 20,
        1 , 21, 0 , 0 , 0 , 17, 16, 20, 0 , 23, 0 , 17, 16, 20, 0 , 17, 16, 16, 16, 20,
        1 , 17, 18, 18, 18, 16, 16, 20, 0 , 21, 0 , 17, 16, 20, 0 , 17, 16, 24, 16, 20,
        1 , 17, 16, 16, 16, 16, 16, 20, 0 , 21, 0 , 17, 16, 20, 0 , 25, 28, 0 , 17, 20,
        1 , 17, 16, 16, 16, 16, 16, 20, 0 , 21, 0 , 17, 16, 20, 0 , 0 , 0 , 0 , 17, 20,
        1 , 17, 16, 16, 16, 16, 16, 20, 0 , 21, 0 , 17, 16, 16, 18, 18, 18, 18, 16, 20,
        1 , 17, 24, 24, 24, 16, 16, 20, 0 , 21, 0 , 17, 16, 16, 16, 16, 16, 16, 16, 20,
        19, 20, 0 , 0 , 0 , 17, 16, 16, 18, 16, 18, 16, 16, 16, 24, 24, 24, 16, 16, 20,
        17, 20, 0 , 0 , 0 , 17, 16, 16, 16, 16, 16, 16, 16, 20, 0 , 0 , 0 , 17, 16, 20,
        17, 20, 0 , 0 , 0 , 25, 24, 24, 24, 24, 24, 16, 16, 20, 0 , 0 , 0 , 17, 16, 20,
        17, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 16, 20, 0 , 0 , 0 , 17, 16, 20,
        25, 24, 26, 26, 26, 26, 26, 26, 26, 26, 26, 24, 24, 24, 26, 26, 26, 16, 16, 20,
        9 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 25, 24, 28
    };
    private final short levelData4[] = {
        19, 26, 26, 26, 26, 26, 26, 26, 26, 18, 18, 26, 26, 26, 26, 26, 26, 26, 26, 22,
        21, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21,
        21, 0 , 19, 26, 26, 18, 18, 26, 26, 24, 24, 26, 26, 26, 26, 26, 26, 26, 26, 20,
        21, 0 , 21, 0 , 0 , 17, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21,
        21, 0 , 17, 18, 18, 16, 16, 26, 18, 18, 18, 18, 26, 18, 26, 26, 18, 22, 0 , 21,
        21, 0 , 17, 16, 16, 16, 20, 0 , 17, 16, 16, 20, 0 , 21, 0 , 0 , 17, 20, 0 , 21,
        21, 0 , 17, 16, 24, 24, 28, 0 , 25, 24, 24, 20, 0 , 21, 0 , 0 , 17, 16, 18, 20,
        21, 0 , 17, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21, 0 , 21, 0 , 0 , 17, 16, 24, 20,
        21, 0 , 17, 16, 18, 18, 22, 0 , 19, 18, 18, 20, 0 , 21, 0 , 0 , 17, 20, 0 , 21,
        17, 18, 16, 24, 24, 24, 24, 26, 24, 24, 24, 24, 26, 24, 26, 26, 24, 28, 0 , 21,
        17, 24, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21,
        21, 0 , 21, 0 , 19, 26, 18, 18, 18, 26, 18, 18, 18, 26, 18, 18, 18, 26, 26, 20,
        21, 0 , 21, 0 , 21, 0 , 25, 24, 28, 0 , 25, 24, 28, 0 , 25, 24, 28, 0 , 0 , 21,
        21, 0 , 21, 0 , 21, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21,
        21, 0 , 21, 0 , 17, 18, 22, 0 , 19, 18, 22, 0 , 19, 18, 22, 0 , 19, 18, 18, 20,
        21, 0 , 21, 0 , 25, 24, 24, 26, 24, 24, 24, 26, 24, 24, 24, 26, 24, 24, 24, 20,
        21, 0 , 21, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21,
        21, 0 , 25, 26, 26, 26, 26, 26, 26, 18, 18, 26, 26, 26, 26, 26, 26, 26, 26, 20,
        21, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 21,
        25, 26, 26, 26, 26, 26, 26, 26, 26, 24, 24, 26, 26, 26, 26, 26, 26, 26, 26, 28
    };

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 5;

    private int currentSpeed = 2;
    private short[] screenData;
    private Timer timer;

    public Board() {

        loadImages();
        initVariables();
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.black);
    }

    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        mazeColor = new Color(5, 100, 5);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        bgA = new AudioPlayer("loss.wav");
        bgA.play();
        timer = new Timer(40, this);
        timer.start();
    }

    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void doAnim() {

        manAnimCount--;
        introAnimCount--;
        winAnimCount--;
        htpAnimCount--;

        if (manAnimCount <= 0) {
            manAnimCount = MAN_ANIM_DELAY;
            manAnimPos = manAnimPos + manAnimDir;

            if (manAnimPos == (MAN_ANIM_COUNT - 1) || manAnimPos == 0) {
                manAnimDir = -manAnimDir;
            }
        }
        if (introAnimCount <= 0) {
            introAnimCount = INTRO_ANIM_DELAY;
            introAnimPos = introAnimPos + introAnimDir;

            if (introAnimPos == (INTRO_ANIM_COUNT - 1) || introAnimPos == 0) {
                introAnimPos = 0;

                if (introAnimPos == 0 || introAnimPos == 0) {
                  introAnimCount = INTRO_ANIM_DELAY;
                  introAnimPos = introAnimPos + introAnimDir;
                }
            }
        }
      if (winAnimCount <= 0) {
          winAnimCount = WIN_ANIM_DELAY;
          winAnimPos = winAnimPos + winAnimDir;

          if (winAnimPos == (WIN_ANIM_COUNT - 1) || winAnimPos == 0) {
              winAnimDir = -winAnimDir;
          }
      }

        if (htpAnimCount <= 0) {
            htpAnimCount = HTP_ANIM_DELAY;
            htpAnimPos = htpAnimPos + htpAnimDir;

            if (htpAnimPos == (HTP_ANIM_COUNT - 1) || htpAnimPos == 0) {
                htpAnimPos = 0;

                if (htpAnimPos == 0 || htpAnimPos == 0) {
                  htpAnimCount = HTP_ANIM_DELAY;
                  htpAnimPos = htpAnimPos + htpAnimDir;
        }
      }
    }
  }

    private void playGame(Graphics2D g2d) {

        if (dying == true) {

            death();


        } else {

            moveMan();
            drawMan(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g2d) {
        if(Life > 0 && N_GHOSTS < 10){
            switch (introAnimPos) {
                case 1:
                    g2d.drawImage(intro2, 0, 0, this);
                    break;
                case 2:
                    g2d.drawImage(intro3, 0, 0, this);
                    break;
                case 3:
                    g2d.drawImage(intro4, 0, 0, this);
                    break;
                case 4:
                    g2d.drawImage(intro5, 0, 0, this);
                    break;
                case 5:
                    g2d.drawImage(intro6, 0, 0, this);
                    break;
                case 6:
                    g2d.drawImage(intro7, 0, 0, this);
                    break;
                case 7:
                    g2d.drawImage(intro8, 0, 0, this);
                    break;
                default:
                    g2d.drawImage(intro1, 0, 0, this);
                    break;
            }
          }
          if(Life > 0 && N_GHOSTS >= 10){
            switch (winAnimPos) {
                case 1:
                    g2d.drawImage(win2, 0, 0, this);
                    break;
                case 2:
                    g2d.drawImage(win3, 0, 0, this);
                    break;
                default:
                    g2d.drawImage(win1, 0, 0, this);
                    break;
            }
            drawScore(g2d);
          }
        if (Life == 0) {
          switch (introAnimPos) {
              case 1:
                  g2d.drawImage(over2, 0, 0, this);
                  break;
              case 2:
                  g2d.drawImage(over3, 0, 0, this);
                  break;
              case 3:
                  g2d.drawImage(over4, 0, 0, this);
                  break;
              case 4:
                  g2d.drawImage(over5, 0, 0, this);
                  break;
              case 5:
                  g2d.drawImage(over6, 0, 0, this);
                  break;
              case 6:
                  g2d.drawImage(over7, 0, 0, this);
                  break;
              case 7:
                  g2d.drawImage(over8, 0, 0, this);
                  break;
              default:
                  g2d.drawImage(over1, 0, 0, this);
                  break;
        }
        N_GHOSTS = 10;
        drawScore(g2d);
      }
}

    private void howtoplay(Graphics2D g2d){
          switch (htpAnimPos) {
              case 1:
                  g2d.drawImage(htp2, 0, 0, this);
                  break;
              case 2:
                  g2d.drawImage(htp3, 0, 0, this);
                  break;
              case 3:
                  g2d.drawImage(htp4, 0, 0, this);
                  break;
              case 4:
                  g2d.drawImage(htp5, 0, 0, this);
                  break;
              case 5:
                  g2d.drawImage(htp6, 0, 0, this);
                  break;
              case 6:
                  g2d.drawImage(htp7, 0, 0, this);
                  break;
              case 7:
                  g2d.drawImage(htp8, 0, 0, this);
                  break;
              case 8:
                  g2d.drawImage(htp9, 0, 0, this);
                  break;
              case 9:
                  g2d.drawImage(htp10, 0, 0, this);
                  break;
              default:
                  g2d.drawImage(htp1, 0, 0, this);
                  break;
        }
    }

    private void drawScore(Graphics2D g) {

        int i;
        String s;
      if(inGame){
        g.setFont(smallFont);
        g.setColor(new Color(110, 128, 255));
        s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 120, SCREEN_SIZE + 16);

        for (i = 0; i < Life; i++) {
            g.drawImage(ji, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
      }
      if (Life == 0) {
        g.setFont(bigFont);
        g.setColor(new Color(255, 255, 255));
        s = "Score: " + score;
        g.drawString(s, 170, 320);
      }
      if (Life > 0 && N_GHOSTS >= 10) {
        g.setFont(bigFont);
        g.setColor(new Color(255, 255, 255));
        s = "Score: " + score;
        g.drawString(s, 165, 330);
      }
    }

    private void checkMaze() {
        short i = 0;
        boolean finished = true;

        while ((i < N_BLOCKS * N_BLOCKS) && finished) {
            if ((screenData[i] & 48) != 0) {
                finished = false;
            }
            if(Life != 0){
              i++;
            }
        }

        if (finished == true) {
          bgM = new AudioPlayer("complete.wav");
          bgM.play();
            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initLevel();
        }
    }

    private void death() {

        Life--;
        bgM = new AudioPlayer("bowserfalls.wav");
        bgM.play();
        if (Life == 0) {
            bgM = new AudioPlayer("loss.wav");
            bgM.play();
            inGame = false;
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {

        short i;
        int pos;
        int count;

        for (i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }
                }
                else {
                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }

            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            if (man_x > (ghost_x[i] - 12) && man_x < (ghost_x[i] + 12)
                    && man_y > (ghost_y[i] - 12) && man_y < (ghost_y[i] + 12)
                    && inGame) {

                dying = true; 
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {
          ghostAnimPos = manAnimPos;
          if(N_GHOSTS == 6){
            switch (ghostAnimPos) {
                case 1:
                    g2d.drawImage(oghost1, x, y, this);
                    break;
                case 2:
                    g2d.drawImage(oghost2, x, y, this);
                    break;
                case 3:
                    g2d.drawImage(oghost3, x, y, this);
                    break;
                default:
                    g2d.drawImage(oghost1, x, y, this);
                    break;
            }
          }
          if(N_GHOSTS == 7){
            switch (ghostAnimPos) {
                case 1:
                    g2d.drawImage(tghost1, x, y, this);
                    break;
                case 2:
                    g2d.drawImage(tghost2, x, y, this);
                    break;
                case 3:
                    g2d.drawImage(tghost3, x, y, this);
                    break;
                default:
                    g2d.drawImage(tghost1, x, y, this);
                    break;
            }
          }
          if(N_GHOSTS == 8){
            switch (ghostAnimPos) {
                case 1:
                    g2d.drawImage(trghost1, x, y, this);
                    break;
                case 2:
                    g2d.drawImage(trghost2, x, y, this);
                    break;
                case 3:
                    g2d.drawImage(trghost3, x, y, this);
                    break;
                default:
                    g2d.drawImage(trghost1, x, y, this);
                    break;
            }
          }
          if(N_GHOSTS == 9){
            switch (ghostAnimPos) {
                case 1:
                    g2d.drawImage(fghost1, x, y, this);
                    break;
                case 2:
                    g2d.drawImage(fghost2, x, y, this);
                    break;
                case 3:
                    g2d.drawImage(fghost3, x, y, this);
                    break;
                default:
                    g2d.drawImage(fghost1, x, y, this);
                    break;
            }
          }

    }

    private void moveMan() {

        int pos;
        short ch;

        if (req_dx == -mand_x && req_dy == -mand_y) {
            mand_x = req_dx;
            mand_y = req_dy;
            view_dx = mand_x;
            view_dy = mand_y;
        }

        if (man_x % BLOCK_SIZE == 0 && man_y % BLOCK_SIZE == 0) {
            pos = man_x / BLOCK_SIZE + N_BLOCKS * (int) (man_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                bgM = new AudioPlayer("BEEP.wav");
                bgM.play();
                if(N_GHOSTS == 6){
                  score = score + 1;
                }
                if(N_GHOSTS == 7){
                  score = score + 2;
                }
                if(N_GHOSTS == 8){
                  score = score + 3;
                }
                if(N_GHOSTS == 9){
                  score = score + 4;
                }
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    mand_x = req_dx;
                    mand_y = req_dy;
                    view_dx = mand_x;
                    view_dy = mand_y;
                }
            }

            if ((mand_x == -1 && mand_y == 0 && (ch & 1) != 0)
                    || (mand_x == 1 && mand_y == 0 && (ch & 4) != 0)
                    || (mand_x == 0 && mand_y == -1 && (ch & 2) != 0)
                    || (mand_x == 0 && mand_y == 1 && (ch & 8) != 0)) {
                mand_x = 0;
                mand_y = 0;
            }
        }
        man_x = man_x + MAN_SPEED * mand_x;
        man_y = man_y + MAN_SPEED * mand_y;
    }

    private void drawMan(Graphics2D g2d) {

        if (view_dx == -1) {
            drawManLeft(g2d);
        }
        else if (view_dx == 1) {
            drawManRight(g2d);
        }
        else if (view_dy == -1) {
            drawManUp(g2d);
        }
        else if (view_dy == 1){
            drawManDown(g2d);
        }
        else{
            drawManStand(g2d);
        }
    }

    private void drawManStand(Graphics2D g2d) {

        switch (manAnimPos) {
            case 1:
                g2d.drawImage(man1stand, man_x, man_y - 22, this);
                break;
            case 2:
                g2d.drawImage(man1stand, man_x, man_y - 22, this);
                break;
            case 3:
                g2d.drawImage(man1stand, man_x, man_y - 22, this);
                break;
            default:
                g2d.drawImage(man2stand, man_x, man_y - 22, this);
                break;
        }
    }

    private void drawManUp(Graphics2D g2d) {

        switch (manAnimPos) {
            case 1:
                g2d.drawImage(man1up, man_x, man_y - 22, this);
                break;
            case 2:
                g2d.drawImage(man2up, man_x, man_y - 22, this);
                break;
            case 3:
                g2d.drawImage(man3up, man_x, man_y - 22, this);
                break;
            default:
                g2d.drawImage(man1up, man_x, man_y - 22, this);
                break;
        }
    }

    private void drawManDown(Graphics2D g2d) {

        switch (manAnimPos) {
            case 1:
                g2d.drawImage(man1down, man_x, man_y - 22, this);
                break;
            case 2:
                g2d.drawImage(man2down, man_x, man_y - 22, this);
                break;
            case 3:
                g2d.drawImage(man3down, man_x, man_y - 22, this);
                break;
            default:
                g2d.drawImage(man1down, man_x, man_y - 22, this);
                break;
        }
    }

    private void drawManLeft(Graphics2D g2d) {

        switch (manAnimPos) {
            case 1:
                g2d.drawImage(man1left, man_x, man_y - 22, this);
                break;
            case 2:
                g2d.drawImage(man2left, man_x, man_y - 22, this);
                break;
            case 3:
                g2d.drawImage(man3left, man_x, man_y - 22, this);
                break;
            default:
                g2d.drawImage(man1, man_x, man_y - 22, this);
                break;
        }
    }

    private void drawManRight(Graphics2D g2d) {

        switch (manAnimPos) {
            case 1:
                g2d.drawImage(man1right, man_x, man_y - 22, this);
                break;
            case 2:
                g2d.drawImage(man2right, man_x, man_y - 22, this);
                break;
            case 3:
                g2d.drawImage(man3right, man_x, man_y - 22, this);
                break;
            default:
                g2d.drawImage(man, man_x, man_y - 22, this);
                break;
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2));

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE -1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.drawImage(gold, x + 11, y + 11, this);
                }

                i++;
            }
        }
    }

    private void initGame() {

        Life = 6;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 2;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {

          if (N_GHOSTS == 6) {
            screenData[i] = levelData1[i];
            if(Life == 0){
              inGame = false;
            }
            continueLevel();
        }
          if (N_GHOSTS == 7) {
            screenData[i] = levelData2[i];
            if(Life == 0){
              inGame = false;
            }
            continueLevel();
        }
          if (N_GHOSTS == 8) {
            screenData[i] = levelData3[i];
            if(Life == 0){
              inGame = false;
            }
            continueLevel();
        }
          if (N_GHOSTS == 9) {
            screenData[i] = levelData4[i];
            if(Life == 0){
              inGame = false;
            }
            continueLevel();
        }
          if (N_GHOSTS >= 10) {
            inGame = false;
      }
    }
  }

    private void continueLevel() {

        short i;
        int dx = 1;
        int random;
        for (i = 0; i < N_GHOSTS; i++) {
            ghost_y[i] = 0;
            ghost_x[i] = 0;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }
        man_x = 19 * BLOCK_SIZE;
        man_y = 19 * BLOCK_SIZE;
        mand_x = 0;
        mand_y = 0;
        req_dx = 0;
        req_dy = 0;
        view_dx = -1;
        view_dy = 0;
        dying = false;
    }

    private void loadImages() {

        intro1 = new ImageIcon("intro1.png").getImage();
        intro2 = new ImageIcon("intro2.png").getImage();
        intro3 = new ImageIcon("intro3.png").getImage();
        intro4 = new ImageIcon("intro4.png").getImage();
        intro5 = new ImageIcon("intro5.png").getImage();
        intro6 = new ImageIcon("intro6.png").getImage();
        intro7 = new ImageIcon("intro7.png").getImage();
        intro8 = new ImageIcon("intro8.png").getImage();
        htp1 = new ImageIcon("howtoplay1.png").getImage();
        htp2 = new ImageIcon("howtoplay2.png").getImage();
        htp3 = new ImageIcon("howtoplay3.png").getImage();
        htp4 = new ImageIcon("howtoplay4.png").getImage();
        htp5 = new ImageIcon("howtoplay5.png").getImage();
        htp6 = new ImageIcon("howtoplay6.png").getImage();
        htp7 = new ImageIcon("howtoplay7.png").getImage();
        htp8 = new ImageIcon("howtoplay8.png").getImage();
        htp9 = new ImageIcon("howtoplay9.png").getImage();
        htp10 = new ImageIcon("howtoplay10.png").getImage();
        gold = new ImageIcon("gold.png").getImage();
        ji = new ImageIcon("ji.png").getImage();
        oghost1 = new ImageIcon("one1.png").getImage();
        oghost2 = new ImageIcon("one2.png").getImage();
        oghost3 = new ImageIcon("one3.png").getImage();
        tghost1 = new ImageIcon("two1.png").getImage();
        tghost2 = new ImageIcon("two2.png").getImage();
        tghost3 = new ImageIcon("two3.png").getImage();
        trghost1 = new ImageIcon("three1.png").getImage();
        trghost2 = new ImageIcon("three2.png").getImage();
        trghost3 = new ImageIcon("three3.png").getImage();
        fghost1 = new ImageIcon("four1.png").getImage();
        fghost2 = new ImageIcon("four2.png").getImage();
        fghost3 = new ImageIcon("four3.png").getImage();
        man = new ImageIcon("pacmanR.png").getImage();
        man1 = new ImageIcon("pacman1L.png").getImage();
        man1up = new ImageIcon("up1.png").getImage();
        man2up = new ImageIcon("up2.png").getImage();
        man3up = new ImageIcon("up3.png").getImage();
        man1down = new ImageIcon("down1.png").getImage();
        man2down = new ImageIcon("down2.png").getImage();
        man3down = new ImageIcon("down3.png").getImage();
        man1left = new ImageIcon("left1.png").getImage();
        man2left = new ImageIcon("left2.png").getImage();
        man3left = new ImageIcon("left3.png").getImage();
        man1right = new ImageIcon("right1.png").getImage();
        man2right = new ImageIcon("right2.png").getImage();
        man3right = new ImageIcon("right3.png").getImage();
        man1stand = new ImageIcon("stand1.png").getImage();
        man2stand = new ImageIcon("stand2.png").getImage();
        win1 = new ImageIcon("win1.png").getImage();
        win2 = new ImageIcon("win2.png").getImage();
        win3 = new ImageIcon("win3.png").getImage();
        over1 = new ImageIcon("gameover1.png").getImage();
        over2 = new ImageIcon("gameover2.png").getImage();
        over3 = new ImageIcon("gameover3.png").getImage();
        over4 = new ImageIcon("gameover4.png").getImage();
        over5 = new ImageIcon("gameover5.png").getImage();
        over6 = new ImageIcon("gameover6.png").getImage();
        over7 = new ImageIcon("gameover7.png").getImage();
        over8 = new ImageIcon("gameover8.png").getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        drawMaze(g2d);
        drawScore(g2d);
        doAnim();

        if (inGame == true) {
            playGame(g2d);
        }
        else {
          if(howto_play == false){
            showIntroScreen(g2d);
          }
          if(howto_play == true){
            howtoplay(g2d);
          }

        }
    }

    class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if(inGame == true){
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                }
                else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                }
                else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                }
                else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                }
                else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
                else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    }
                    else {
                        timer.start();
                    }
                }
              }
              else{
                if(Life > 0 && N_GHOSTS < 10){
                  if (key == KeyEvent.VK_SPACE) {
                      bgM = new AudioPlayer("click.wav");
                      bgM.play();
                      howto_play = true;

                  }
              }
                if (key == 's' || key == 'S') {
                    bgM = new AudioPlayer("click.wav");
                    bgM.play();
                    inGame = true;
                    howto_play = false;
                    initGame();
                }
              }

        }
    }

    public void actionPerformed(ActionEvent e) {

        repaint();
    }
}


class AudioPlayer {
	private Clip clip;
	public AudioPlayer(String s) {
		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(s));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void play() {
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}

	public void stop() {
		if(clip.isRunning()) clip.stop();
	}

}

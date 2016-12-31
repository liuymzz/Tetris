package ui;

import entities.*;
import enums.GameState;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainUI extends JFrame implements Runnable {
    private int[][] map;                            //格子背景
    private Block block;                            //活动方块
    private Block targetBlock;                      //提示方块
    private HB hb;                                  //画板
    private int dropInterval = 0;                   //方块下落间隔
    private Block nextBlock;                        //下一个方块
    private GameState state = GameState.WELCOME;    //游戏状态
    private GameFont welcomeFont =
            new GameFont("俄罗斯方块", Constants.WINDOW_WIDTH / 3, -Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);     //欢迎界面游戏标题
    private GameImage startImage =
            new GameImage("btn_play.png", (Constants.WINDOW_WIDTH - Medias.getImage("btn_play.png").getWidth()) / 2, welcomeFont.getY() + Constants.BLOCK_SIZE);     //欢迎界面图片按钮
    private GameImage exitImage =
            new GameImage("exit.png", Constants.WINDOW_WIDTH - 100, Constants.WINDOW_HEIGHT - 100);
    private GameFont countDown = new GameFont("3", Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, Constants.BLOCK_SIZE * 3);
    private GameImage defeate = new GameImage("defeat1.png", 100, -Medias.getImage("defeat.png").getHeight());
    private GameImage restartImage = new GameImage("restart.png", 100, Constants.WINDOW_HEIGHT - 100);

    public MainUI() {
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        map = new int[Constants.ROW][Constants.COLUMN];
        block = Fac.getBlock();
        refreshTargetBlock();
        nextBlock = Fac.getBlock();

        Container c = getContentPane();
        c.setFocusable(true);
        hb = new HB();
        c.add(hb);
        c.addKeyListener(new KeyAd());
        c.addMouseListener(new MouseAd());
        c.addMouseMotionListener(new MouseAd());

        setVisible(true);
    }

    private void moveTargetBlock() {
        while (targetBlock.moveDown(map)) {

        }
    }


    class KeyAd extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (state == GameState.GAMING) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        block.rotate();
                        block.isRotateSuccess(map);
                        refreshTargetBlock();
                        break;
                    case KeyEvent.VK_DOWN:
                        block.moveDown(map);
                        break;
                    case KeyEvent.VK_LEFT:
                        block.moveLeft(map);
                        refreshTargetBlock();
                        break;
                    case KeyEvent.VK_RIGHT:
                        block.moveRight(map);
                        refreshTargetBlock();
                        break;
                    case KeyEvent.VK_SPACE:
                        SoundUtils.Play(Medias.getAudio("sfx_harddrop.wav"),false);
                        while (block.moveDown(map)) {

                        }
                        frozen();
                        break;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_1) {
                if (state == state.GAMING) {
                    state = GameState.PAUSE;
                } else if (state == GameState.PAUSE) {
                    state = GameState.GAMING;
                }
            }

        }
    }

    class MouseAd extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (state == GameState.GAME_SELECT) {
                if (startImage.getRectangle().contains(e.getX(), e.getY())) {
                    state = GameState.COUNTDOWN;
                    SoundUtils.Play(Medias.getAudio("countdown.wav"),false);
                } else if (exitImage.getRectangle().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }
            } else if (state == GameState.OVER) {
                if (restartImage.getRectangle().contains(e.getX(), e.getY())) {
                    for (int i = 0; i < map.length; i++) {
                        for (int j = 0; j < map[i].length; j++) {
                            map[i][j] = 0;
                        }
                    }
                    block = Fac.getBlock();
                    nextBlock = Fac.getBlock();
                    refreshTargetBlock();
                    state = GameState.GAMING;
                } else if (exitImage.getRectangle().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (state == GameState.GAME_SELECT) {
                if (startImage.getRectangle().contains(e.getX(), e.getY())) {
                    startImage.setImage(Medias.getImage("btn_play_click.png"));
                } else if (exitImage.getRectangle().contains(e.getX(), e.getY())) {
                    exitImage.setImage(Medias.getImage("exit_click.png"));
                } else {
                    startImage.setImage(Medias.getImage("btn_play.png"));
                    exitImage.setImage(Medias.getImage("exit.png"));
                }
            } else if(state == GameState.OVER){
                if(exitImage.getRectangle().contains(e.getX(),e.getY())){
                    exitImage.setImage(Medias.getImage("exit_click.png"));
                }else {
                    exitImage.setImage(Medias.getImage("exit.png"));
                }
            }

            hb.repaint();
        }

    }

    class HB extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.clearRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
            if (state == GameState.GAMING) {
                drawMap(g);
                drawBlock(g);
                drawTargetBlock(g);
                drawNextBlock(g);
            } else if (state == GameState.GAME_SELECT) {
                drawWelcome(g);
            } else if (state == GameState.COUNTDOWN) {
                drawCountDown(g);
            } else if (state == GameState.WELCOME) {
                drawWelcome(g);
            } else if (state == GameState.PAUSE) {
                drawMap(g);
                drawBlock(g);
                drawTargetBlock(g);
                drawNextBlock(g);
            } else if (state == GameState.OVER) {
                g.drawImage(Medias.getImage("bg.jpg"), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
                drawGameImage(g, defeate);
                drawGameImage(g, restartImage);
                drawGameImage(g, exitImage);
            }

        }

        private void drawGameImage(Graphics g, GameImage gameImage) {
            g.drawImage(gameImage.getImage(), gameImage.getX(), gameImage.getY(), this);
        }

        private void drawCountDown(Graphics g) {
            g.setColor(Color.black);
            g.setFont(new Font("微软雅黑", Font.ITALIC, countDown.getSize()));
            g.drawImage(Medias.getImage("bg.jpg"), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
            g.drawString(countDown.getText(), countDown.getX(), countDown.getY());

        }

        private void drawWelcome(Graphics g) {
            g.drawImage(Medias.getImage("bg.jpg"), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
            g.setFont(new Font("微软雅黑", Font.BOLD, welcomeFont.getSize()));
            g.drawString(welcomeFont.getText(), welcomeFont.getX(), welcomeFont.getY());
            g.drawImage(startImage.getImage(), startImage.getX(), startImage.getY(), this);
            g.drawImage(exitImage.getImage(), exitImage.getX(), exitImage.getY(), this);
        }

        private void drawNextBlock(Graphics g) {
            for (int i = 0; i < nextBlock.getBlock().length; i++) {
                for (int j = 0; j < nextBlock.getBlock()[i].length; j++) {
                    int index = nextBlock.getBlock()[i][j];
                    if (index != 0) {
                        g.drawImage(Medias.getImage(index + ".png"),
                                nextBlock.getX() * Constants.BLOCK_SIZE + (7 + j) * Constants.BLOCK_SIZE,
                                nextBlock.getY() * Constants.BLOCK_SIZE + (i + 1) * Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                this);
                    }
                }
            }
        }

        private void drawTargetBlock(Graphics g) {
            g.setColor(Color.WHITE);
            for (int i = 0; i < targetBlock.getBlock().length; i++) {
                for (int j = 0; j < targetBlock.getBlock()[i].length; j++) {
                    if (targetBlock.getBlock()[i][j] != 0) {
                        g.draw3DRect(targetBlock.getX() * Constants.BLOCK_SIZE + j * Constants.BLOCK_SIZE,
                                targetBlock.getY() * Constants.BLOCK_SIZE + i * Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                true);
                    }
                }
            }
        }

        private void drawMap(Graphics g) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] != 0) {
                        g.drawImage(Medias.getImage(map[i][j] + ".png"),
                                j * Constants.BLOCK_SIZE,
                                i * Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                this
                        );
                    } else {
                        g.setColor(Color.darkGray);
                        g.fill3DRect(j * Constants.BLOCK_SIZE,
                                i * Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                true);
                    }
                }
            }

            g.fill3DRect((Constants.COLUMN) * Constants.BLOCK_SIZE, 0, (Constants.WINDOW_WIDTH - Constants.COLUMN) * Constants.BLOCK_SIZE, Constants.WINDOW_HEIGHT, true);
        }

        private void drawBlock(Graphics g) {
            for (int i = 0; i < block.getBlock().length; i++) {
                for (int j = 0; j < block.getBlock()[i].length; j++) {
                    int index = block.getBlock()[i][j];
                    if (index != 0) {
                        g.drawImage(Medias.getImage(index + ".png"),
                                block.getX() * Constants.BLOCK_SIZE + j * Constants.BLOCK_SIZE,
                                block.getY() * Constants.BLOCK_SIZE + i * Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
                                this);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        MainUI ui = new MainUI();
        new Thread(ui).start();
    }

    @Override
    public void run() {
        while (true) {
            if (state == GameState.WELCOME) {
                welcomeFont.moveDown();
                startImage.moveDown();
                if (welcomeFont.getY() > 5 * Constants.BLOCK_SIZE) {
                    state = GameState.GAME_SELECT;
                }
            } else if (state == GameState.GAME_SELECT) {

            } else if (state == GameState.COUNTDOWN) {
                countDown.setSize(countDown.getSize() - 1);
                if (countDown.getSize() <= 0) {
                    countDown.setText(Integer.parseInt(countDown.getText()) - 1 + "");
                    countDown.setSize(Constants.BLOCK_SIZE * 3);
                }
                if ("0".equals(countDown.getText())) {
                    state = GameState.GAMING;
                }
            } else if (state == GameState.GAMING) {
                blockDrop();
            } else if (state == GameState.OVER) {
                if (defeate.getY() <= 100) {
                    defeate.moveDown();
                }
            }

            hb.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void blockDrop() {
        dropInterval++;
        if (dropInterval >= 40) {
            dropInterval = 0;
            if (block.moveDown(map) == false) {
                frozen();
            }
        }
    }

    private void frozen() {
        SoundUtils.Play(Medias.getAudio("sfx_lockdown.wav"),false);
        for (int i = 0; i < block.getBlock().length; i++) {
            for (int j = 0; j < block.getBlock()[i].length; j++) {
                if (block.getBlock()[i][j] != 0) {
                    map[block.getY() + i][block.getX() + j] = block.getBlock()[i][j];
                }
            }
        }
        block = nextBlock;
        refreshTargetBlock();
        nextBlock = Fac.getBlock();
        int index;
        while ((index = isFullRow()) != -1) {
            fuckRow(index);
        }
        refreshTargetBlock();
        for (int i = 0; i < Constants.COLUMN; i++) {
            if (map[0][i] != 0) {
                state = GameState.OVER;
                SoundUtils.Play(Medias.getAudio("sfx_gameover.wav"),false);
            }
        }
    }

    //当前方块的状态改变后需要刷新targetBlock
    private void refreshTargetBlock() {
        targetBlock = CloneUtils.clone(block);
        moveTargetBlock();
    }

    private void fuckRow(int index) {
        for (int i = index; i > 0; i--) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = map[i - 1][j];
            }
            SoundUtils.Play(Medias.getAudio("sfx_single.wav"),false);
        }
    }

    private int isFullRow() {
        int index = -1;
        for (int i = 0; i < map.length; i++) {
            int count = 0;
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0) {
                    count++;
                }
            }
            if (count == Constants.COLUMN) {
                return i;
            }
        }

        return index;
    }
}

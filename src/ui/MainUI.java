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
import java.io.*;

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
    private boolean isCountDowning = false;         //是否处于倒计时状态，播放倒计时声音会用到
    private GameImage defeate = new GameImage("defeat1.png", 100, -Medias.getImage("defeat.png").getHeight());
    private GameImage restartImage = new GameImage("restart.png", 100, Constants.WINDOW_HEIGHT - 100);
    private int score = 0;
    private int eliminateLine = 0;      //用来记录消除的行数，提供给道具一使用
    private int prop_1_Times = 1;       //道具一的可用次数

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
                        return;
                    case KeyEvent.VK_DOWN:
                        block.moveDown(map);
                        return;
                    case KeyEvent.VK_LEFT:
                        block.moveLeft(map);
                        refreshTargetBlock();
                        return;
                    case KeyEvent.VK_RIGHT:
                        block.moveRight(map);
                        refreshTargetBlock();
                        return;
                    case KeyEvent.VK_SPACE:
                        SoundUtils.Play(Medias.getAudio("sfx_harddrop.wav"), false);
                        while (block.moveDown(map)) {

                        }
                        frozen();
                        return;
                    case KeyEvent.VK_S:
                        save();
                        return;
                    case KeyEvent.VK_L:
                        load();
                        return;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_P) {
                if (state == state.GAMING) {
                    state = GameState.PAUSE;
                } else if (state == GameState.PAUSE) {
                    state = GameState.GAMING;
                }
                return;
            }

            if(e.getKeyCode() == KeyEvent.VK_1){
                if(state == GameState.GAMING){
                    state = GameState.PROP_addBlock;
                }else if(state == GameState.PROP_addBlock){
                    eliminateRow();
                    state = GameState.GAMING;
                }
                return;
            }

        }

        private void load() {
            File file = new File("conf/record");
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(file));
                Object object = objectInputStream.readObject();
                if(object instanceof Save){
                    Save save = (Save)object;
                    map = save.getMap();
                    block = save.getBlock();
                    targetBlock = save.getTargetBlock();
                    nextBlock = save.getNextBlock();
                    score = save.getScore();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void save() {
            File file = new File("conf/record");
            ObjectOutputStream objectOutputStream = null;
            Save save = new Save(map, block, targetBlock, nextBlock, score);
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(save);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                } else if (exitImage.getRectangle().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }

                return;
            }

            if (state == GameState.OVER) {
                if (restartImage.getRectangle().contains(e.getX(), e.getY())) {
                    for (int i = 0; i < map.length; i++) {
                        for (int j = 0; j < map[i].length; j++) {
                            map[i][j] = 0;
                        }
                    }
                    block = Fac.getBlock();
                    nextBlock = Fac.getBlock();
                    refreshTargetBlock();
                    state = GameState.COUNTDOWN;
                    score = 0;
                } else if (exitImage.getRectangle().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }

                return;
            }

            if(state == GameState.PROP_addBlock){
                if(prop_1_Times > 0){
                    int xx = e.getX() / Constants.BLOCK_SIZE;
                    int yy = e.getY() / Constants.BLOCK_SIZE;
                    if(xx < Constants.COLUMN && yy < Constants.ROW){
                        if(map[yy][xx] == 0){
                            map[yy][xx] = 1;
                        }else{
                            map[yy][xx] = 0;
                        }
                        prop_1_Times --;
                    }else{
                        SoundUtils.Play(Medias.getAudio("sfx_harddrop.wav"),false);
                    }


                }else{
                    SoundUtils.Play(Medias.getAudio("sfx_harddrop.wav"),false);
                }
            }

            return;
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
            } else if (state == GameState.OVER) {
                if (exitImage.getRectangle().contains(e.getX(), e.getY())) {
                    exitImage.setImage(Medias.getImage("exit_click.png"));
                } else {
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
                drawGaming(g);
                g.setColor(Color.white);
                g.setFont(new Font("微软雅黑", Font.BOLD, Constants.BLOCK_SIZE / 2));
                g.drawString("您消除了" + score + "行", 11 * Constants.BLOCK_SIZE, 7 * Constants.BLOCK_SIZE);
            } else if (state == GameState.GAME_SELECT) {
                drawWelcome(g);
            } else if (state == GameState.COUNTDOWN) {
                drawCountDown(g);
            } else if (state == GameState.WELCOME) {
                drawWelcome(g);
            } else if (state == GameState.PAUSE) {
                drawGaming(g);
            } else if (state == GameState.OVER) {
                drawOver(g);
            }else if(state == GameState.PROP_addBlock){
                drawGaming(g);
            }

        }

        //画结束界面
        private void drawOver(Graphics g){
            g.drawImage(Medias.getImage("bg.jpg"), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
            drawGameImage(g, defeate);
            drawGameImage(g, restartImage);
            drawGameImage(g, exitImage);
            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.BOLD, Constants.BLOCK_SIZE / 2));
            g.drawString("您消除了" + score + "行", 7 * Constants.BLOCK_SIZE, 12 * Constants.BLOCK_SIZE);
            g.drawString("历史最佳：" + Fac.getMaxScore(), 7 * Constants.BLOCK_SIZE, 14 * Constants.BLOCK_SIZE);
        }

        //画游戏图片素材对象
        private void drawGameImage(Graphics g, GameImage gameImage) {
            g.drawImage(gameImage.getImage(), gameImage.getX(), gameImage.getY(), this);
        }

        //画游戏中的界面
        private void drawGaming(Graphics g){
            drawMap(g);
            drawBlock(g);
            drawTargetBlock(g);
            drawNextBlock(g);
        }

        //画倒计时
        private void drawCountDown(Graphics g) {
            g.setColor(Color.black);
            g.setFont(new Font("微软雅黑", Font.ITALIC, countDown.getSize()));
            g.drawImage(Medias.getImage("bg.jpg"), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
            g.drawString(countDown.getText(), countDown.getX(), countDown.getY());

        }

        //画欢迎界面
        private void drawWelcome(Graphics g) {
            g.drawImage(Medias.getImage("bg.jpg"), 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
            g.setFont(new Font("微软雅黑", Font.BOLD, welcomeFont.getSize()));
            g.drawString(welcomeFont.getText(), welcomeFont.getX(), welcomeFont.getY());
            g.drawImage(startImage.getImage(), startImage.getX(), startImage.getY(), this);
            g.drawImage(exitImage.getImage(), exitImage.getX(), exitImage.getY(), this);
        }

        //画下一个方块
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

        //画提示方块
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

        //画背景
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

        //画当前活动方块
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
                if(isCountDowning == false){
                    SoundUtils.Play(Medias.getAudio("countdown.wav"),false);
                    isCountDowning = true;
                }
                countDown.setSize(countDown.getSize() - 1);
                if (countDown.getSize() <= 0) {
                    countDown.setText(Integer.parseInt(countDown.getText()) - 1 + "");
                    countDown.setSize(Constants.BLOCK_SIZE * 3);
                }
                if ("0".equals(countDown.getText())) {
                    countDown.setText("3");
                    countDown.setSize(Constants.BLOCK_SIZE * 3);
                    isCountDowning = false;
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
                Thread.sleep(9);
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
        SoundUtils.Play(Medias.getAudio("sfx_lockdown.wav"), false);
        for (int i = 0; i < block.getBlock().length; i++) {
            for (int j = 0; j < block.getBlock()[i].length; j++) {
                if (block.getBlock()[i][j] != 0) {
                    map[block.getY() + i][block.getX() + j] = block.getBlock()[i][j];
                }
            }
        }

        for (int i = 0; i < Constants.COLUMN; i++) {
            if (map[0][i] != 0) {
                int record = Fac.getMaxScore();
                state = GameState.OVER;
                SoundUtils.Play(Medias.getAudio("sfx_gameover.wav"), false);
                if (score > record) {
                    Fac.setMaxScore(score);
                }
                break;
            }
        }

        block = nextBlock;
        refreshTargetBlock();
        nextBlock = Fac.getBlock();

        eliminateRow();

    }

    //消除掉所有可以消除的行
    private void eliminateRow(){
        int index;
        while ((index = isFullRow()) != -1) {
            fuckRow(index);
            eliminateLine ++;
            if(eliminateLine == 5){
                prop_1_Times ++;
                eliminateLine = 0;
            }
        }
        refreshTargetBlock();
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
            SoundUtils.Play(Medias.getAudio("sfx_single.wav"), false);
        }
        score++;
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

package ui;

import entities.*;
import utils.CloneUtils;
import utils.Constants;
import utils.Fac;
import utils.Medias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainUI extends JFrame implements Runnable {
    private int[][] map;
    private Block block;
    private Block targetBlock;
    private HB hb;
    private int dropInterval = 0;
    private Block nextBlock;

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

        setVisible(true);
    }

    private void moveTargetBlock() {
        while (targetBlock.moveDown(map)) {

        }
    }


    class KeyAd extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
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
                    while (block.moveDown(map)) {

                    }
                    frozen();
            }
        }
    }

    class HB extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.clearRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
            drawMap(g);
            drawBlock(g);
            drawTargetBlock(g);
            drawNextBlock(g);
        }

        private void drawNextBlock(Graphics g) {
            for (int i = 0; i < nextBlock.getBlock().length; i++) {
                for (int j = 0; j < nextBlock.getBlock()[i].length; j++) {
                    int index = nextBlock.getBlock()[i][j];
                    if (index != 0) {
                        g.drawImage(Medias.getImage(index + ".png"),
                                nextBlock.getX() * Constants.BLOCK_SIZE + j * Constants.BLOCK_SIZE,
                                Constants.BLOCK_SIZE,
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

            g.fill3DRect((Constants.COLUMN) * Constants.BLOCK_SIZE,0,(Constants.WINDOW_WIDTH - Constants.COLUMN) * Constants.BLOCK_SIZE,Constants.WINDOW_HEIGHT,true);
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
            blockDrop();
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

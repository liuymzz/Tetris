package entities;


import utils.Constants;
import utils.Medias;
import utils.SoundUtils;

import java.io.Serializable;

public class Block implements Serializable{
    private int x;
    private int y;
    private int[][] block;

    public Block() {
        x = Constants.COLUMN  / 2 - 1;
        y = 0;
    }

    /**
     * 移动方块并检测是否移动成功
     *
     * @param map 需要传入map数组用来检测移动是否合法，如果不合法则不返回
     * @return 合法返回true并移动方块，否则返回false并不移动
     */
    public boolean moveDown(int[][] map) {
        y++;
        if (check(map) == false) {

                y--;
                return false;

        }
        return true;
    }

    /**
     * 移动方块并检测是否移动成功
     *
     * @param map 需要传入map数组用来检测移动是否合法，如果不合法则不返回
     * @return 合法返回true并移动方块，否则返回false并不移动
     */
    public boolean moveUp(int[][] map) {
        y--;
        if (check(map) == false) {
            y++;
            return false;
        }
        return true;
    }

    /**
     * 移动方块并检测是否移动成功
     *
     * @param map 需要传入map数组用来检测移动是否合法，如果不合法则不返回
     * @return 合法返回true并移动方块，否则返回false并不移动
     */
    public boolean moveLeft(int[][] map) {
        x--;
        if (check(map) == false) {
            SoundUtils.Play(Medias.getAudio("sfx_movefail.wav"),false);
            x++;
            return false;
        }

        SoundUtils.Play(Medias.getAudio("sfx_move.wav"),false);
        return true;
    }

    /**
     * 移动方块并检测是否移动成功
     *
     * @param map 需要传入map数组用来检测移动是否合法，如果不合法则不返回
     * @return 合法返回true并移动方块，否则返回false并不移动
     */
    public boolean moveRight(int[][] map) {
        x++;
        if (check(map) == false) {
            SoundUtils.Play(Medias.getAudio("sfx_movefail.wav"),false);
            x--;
            return false;
        }
        SoundUtils.Play(Medias.getAudio("sfx_move.wav"),false);
        return true;
    }

    /**
     * 旋转方块
     */
    public void rotate() {
        int n = block.length;
        int[][] m = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                m[row][col] = block[n - 1 - col][row];
            }
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                block[row][col] = m[row][col];
            }
        }

    }

    /**
     * 判断旋转是否合法，不合法返回到未旋转之前的状态
     *
     * @param map 地图数组
     * @return 合法返回true，否则返回false
     */
    public boolean isRotateSuccess(int[][] map) {
        if (check(map) == false) {
            SoundUtils.Play(Medias.getAudio("sfx_rotatefail.wav"),false);
            rotate();
            rotate();
            rotate();
            //System.out.println("返回未旋转状态");
        }else{
            SoundUtils.Play(Medias.getAudio("sfx_rotate.wav"),false);
        }
        return true;
    }


    public boolean check(int[][] map) throws ArrayIndexOutOfBoundsException {

        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != 0) {
                    try {
                        if (map[y + i][x + j] != 0) {
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][] getBlock() {
        return block;
    }

    public void setBlock(int[][] block) {
        this.block = block;
    }
}

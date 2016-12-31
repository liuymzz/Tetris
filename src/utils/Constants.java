package utils;


public class Constants {
    public static final int BLOCK_SIZE = 30;                                    //每个格子的像素大小
    public static final int COLUMN = 10;                                        //有多少列
    public static final int ROW = 20;                                           //有多少行
    public static final int GAME_WIDTH = BLOCK_SIZE * COLUMN;                   //游戏区域的宽度
    public static final int WINDOW_WIDTH = GAME_WIDTH + BLOCK_SIZE * 6;         //游戏窗口的宽度
    public static final int WINDOW_HEIGHT = BLOCK_SIZE * ROW;                   //游戏窗口的高度

    //方块内部的值，代表方块的颜色
    public static final int BC_BLUE = 1;
    public static final int BC_PURPLE = 2;
    public static final int BC_GREEN = 3;
    public static final int BC_ORANGE = 4;
    public static final int BC_CYAN = 5;
    public static final int BC_YELLOW = 6;
    public static final int BC_RED = 7;


}

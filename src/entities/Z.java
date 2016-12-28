package entities;

import utils.Constants;

public class Z extends Block {
    public Z(){
        int[][] block = new int[3][3];
        block[0][1] = block[1][0] = block[1][1] = block[2][0] = Constants.BC_RED;
        setBlock(block);
    }
}

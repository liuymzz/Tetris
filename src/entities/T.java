package entities;

import utils.Constants;

public class T extends Block {
    public T(){
        int[][] block = new int[3][3];
        block[0][0] = block[0][1] = block[0][2] = block[1][1] = Constants.BC_YELLOW;
        setBlock(block);
    }
}

package entities;

import utils.Constants;

public class L extends Block {

    public L(){
        int[][] block = new int[3][3];
        block[0][0] = block[1][0] = block[2][0] = block[2][1] = Constants.BC_GREEN;
        setBlock(block);
    }
}

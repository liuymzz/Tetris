package entities;

import utils.Constants;

public class I extends Block {

    public I(){
        int[][] block = new int[4][4];
        block[0][0] = block[1][0] = block[2][0] = block[3][0] = Constants.BC_BLUE;
        setBlock(block);
    }
}

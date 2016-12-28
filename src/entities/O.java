package entities;

import utils.Constants;

public class O extends Block {

    public O(){
        int[][] block = new int[2][2];
        block[0][0] = block[0][1] = block[1][0] = block[1][1] = Constants.BC_ORANGE;
        setBlock(block);
    }
}

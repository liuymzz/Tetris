package entities;

import utils.Constants;

public class J extends Block {

    public J(){
        int[][] block = new int[3][3];
        block[0][1] = block[1][1] = block[2][0] = block[2][1] = Constants.BC_PURPLE;
        setBlock(block);
    }
}

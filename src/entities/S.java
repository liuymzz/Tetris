package entities;

import utils.Constants;

public class S extends Block {

    public S(){
        int[][] block = new int[3][3];
        block[0][0] = block[1][0] = block[1][1] = block[2][1] = Constants.BC_CYAN;
        setBlock(block);
    }
}

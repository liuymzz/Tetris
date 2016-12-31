package entities;

import java.io.Serializable;

public class Save implements Serializable {
    int[][] map;
    private Block block;                            //活动方块
    private Block targetBlock;
    private Block nextBlock;
    private int score;

    public Save(int[][] map,Block block,Block targetBlock,Block nextBlock,int score){
        this.block = block;
        this.map = map;
        this.score = score;
        this.nextBlock = nextBlock;
        this.targetBlock = targetBlock;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Block getTargetBlock() {
        return targetBlock;
    }

    public void setTargetBlock(Block targetBlock) {
        this.targetBlock = targetBlock;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

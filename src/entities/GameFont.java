package entities;

import utils.Constants;

public class GameFont {
    private String text;
    private int x;
    private int y;
    private int size;

    public GameFont(String text,int x,int y,int size){
        this.size = size;
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public void moveDown(){
        y += 2;
    }

    public void moveUp(){
        y --;
    }

    public void moveLeft(){
        x --;
    }

    public void moveRight(){
        x ++;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}

package entities;

import utils.Medias;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameImage {
    private BufferedImage image;
    private int x;
    private int y;
    private Rectangle rectangle = new Rectangle();

    public GameImage(String imageName,int x,int y) {
        image = Medias.getImage(imageName);
        this.x = x;
        this.y = y;

    }

    public void moveDown(){
        y ++;
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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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

    public Rectangle getRectangle() {
        rectangle.setBounds(getX(), getY(), image.getWidth(), image.getHeight());
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}

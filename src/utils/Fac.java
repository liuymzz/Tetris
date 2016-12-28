package utils;

import entities.*;

import java.util.Random;

public class Fac {
    private static Random r = new Random();


    /**
     * 随机获取一个方块
     * @return
     */
    public static Block getBlock(){
        int index = r.nextInt(7)+1;
        switch (index){
            case 1 : return new I();
            case 2 : return new J();
            case 3 : return new L();
            case 4 : return new O();
            case 5 : return new S();
            case 6 : return new T();
            default : return new Z();
        }
    }
}

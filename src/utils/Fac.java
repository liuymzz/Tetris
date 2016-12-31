package utils;

import entities.*;

import java.io.*;
import java.util.Properties;
import java.util.Random;

public class Fac {
    private static Random r = new Random();


    /**
     * 随机获取一个方块
     *
     * @return
     */
    public static Block getBlock() {
        int index = r.nextInt(7) + 1;
        switch (index) {
            case 1:
                return new Z();
            case 2:
                return new J();
            case 3:
                return new L();
            case 4:
                return new O();
            case 5:
                return new S();
            case 6:
                return new T();
            default:
                return new I();
        }
    }

    public static int getMaxScore() {
        int max = 0;
        File file = null;
        FileReader fileReader = null;
        try {
            file = new File("conf/recored.properties");
            fileReader = new FileReader(file);
            Properties properties = new Properties();
            properties.load(fileReader);
            max = Integer.parseInt(properties.getProperty("max_score"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return max;
    }

    public static void setMaxScore(int score){
        File file = new File("conf/recored.properties");
        FileReader fileReader = null;
        try {
            System.out.println("setMaxScore");
            System.out.println(score);
            fileReader = new FileReader(file);
            Properties properties = new Properties();
            properties.load(fileReader);
            properties.setProperty("max_score",score + "");
            properties.store(new FileWriter(file),"");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

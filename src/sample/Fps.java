package sample;


import java.util.Random;

/** klasa Fps, ktora odpowiada za FPS.
 */
public class Fps extends Thread{
    public int fps1;

    public int setFps(){
        Random random = new Random();
        fps1=random.nextInt((3))+58;
        return fps1;
    }
}
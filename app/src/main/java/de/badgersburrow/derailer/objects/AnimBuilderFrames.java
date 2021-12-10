package de.badgersburrow.derailer.objects;

import java.util.ArrayList;

public class AnimBuilderFrames {

    private int drawableIdle; // image used in the animation, can be drawable set for a frame by frame animation
    private int moveSteps; // frames to complete a move from the start to the end of a tile
    private int numAnims; // number of animations started per complete move
    private float posX; // relative to the start of the cart in percent from 0 to 1, 0 -> front
    private float posY; // relative to the width of the cart in percent from -0.5 to 0.5, 0 -> mid
    //private float scaleFactor; // overall scaling of the image due to the size of the playing field
    private ArrayList<Integer> frames;

    public AnimBuilderFrames(int drawableIdle, int numAnims, float posX, float posY){
        this.drawableIdle = drawableIdle;
        this.numAnims = numAnims;
        this.posX = posX;
        this.posY = posY;
        this.frames = new ArrayList<>();
    }

    public AnimBuilderFrames addFrame(int frameId){
        this.frames.add(frameId);
        return this;
    }


    public MoveAnimSecondary build(){
        return new MoveAnimSecondary(drawableIdle, numAnims, posX, posY, frames);
    }
}

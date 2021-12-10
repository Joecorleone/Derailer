package de.badgersburrow.derailer.objects;

public class AnimBuilderSingle {

    private int drawableId; // image used in the animation, can be drawable set for a frame by frame animation
    private int moveSteps; // frames to complete a move from the start to the end of a tile
    private int numAnims; // number of animations started per complete move
    private int duration; // number of frames for a animation
    private float posStartX; // relative to the start of the cart in percent from 0 to 1, 0 -> front
    private float posStartY; // relative to the width of the cart in percent from -0.5 to 0.5, 0 -> mid
    //private float scaleFactor; // overall scaling of the image due to the size of the playing field


    private boolean posFixed = false; // whether the animation is fixed to playing filed -> does not move when cart moves
    private boolean randRot = false; // randomize the rotation of the drawable
    private boolean randInterval = false;

    private float scaleStart = 1; // scaling of the image at animation start, e.g. 0.1
    private float scaleEnd = 1; // scaling of the image at animation end, e.g. 1

    private int alphaStart = 255;
    private int alphaEnd = 255;

    public AnimBuilderSingle(int drawableId, int numAnims, int duration, float posStartX, float posStartY){
        this.drawableId = drawableId;
        this.numAnims = numAnims;
        this.duration = duration;
        this.posStartX = posStartX;
        this.posStartY = posStartY;
    }

    public AnimBuilderSingle setPosFixed(boolean posFixed){
        this.posFixed = posFixed;
        return this;
    }

    public AnimBuilderSingle setRandRot(boolean randRot){
        this.randRot = randRot;
        return this;
    }

    public AnimBuilderSingle setRandInterval(boolean randInterval){
        this.randInterval = randInterval;
        return this;
    }

    public AnimBuilderSingle setScale(float scaleStart, float scaleEnd){
        this.scaleStart = scaleStart;
        this.scaleEnd = scaleEnd;
        return this;
    }

    public AnimBuilderSingle setAlpha(int alphaStart, int alphaEnd){
        this.alphaStart = alphaStart;
        this.alphaEnd = alphaEnd;
        return this;
    }


    public MoveAnimSecondary build(){
        return new MoveAnimSecondary(drawableId, numAnims, duration,
        posStartX, posStartY, scaleStart, scaleEnd,
        posFixed, randRot, randInterval,
        alphaStart, alphaEnd);
    }
}

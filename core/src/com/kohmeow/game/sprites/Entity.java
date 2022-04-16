package com.kohmeow.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;




public class Entity extends Sprite {
    private Vector2 velocity;
    private Direction currentDirection;
    private Direction previousDirection;

    /*
    Walking animation loop

    */
    private Animation<TextureRegion> walk;


    private Array<TextureRegion> walkFrames;


    private Vector2 nextPosition;
    private Vector2 currentPosition;
    private State state = State.IDLE;
    private float frameTime;
    private Sprite frameSprite;
    private TextureRegion currentFrame;

    private Texture texture;

    public static Rectangle boundingBox;



    public enum State {
        IDLE, WALKING
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    public Entity(){
        this.nextPosition = new Vector2();
        this.currentPosition = new Vector2();
        boundingBox = new Rectangle(); // Define box allow player that can plant or watering
        this.velocity = new Vector2(2.5f,2.5f); // (2.5,2.5) cord/s
        frameTime = 0f;
        currentDirection = Direction.DOWN;
        texture = new Texture("slaim.png");
        loadSprite();
        loadAnimations();

    }
    public void startingPosition(float x, float y){
        this.currentPosition.set(x,y);
        this.nextPosition.set(x,y);
    }
    public void update(float delta) {
        if(state == State.WALKING)
            frameTime = (frameTime + delta)%5;
        else
            frameTime = 0;
        boundingBox.set(nextPosition.x + 20, nextPosition.y, 24, 12);
    }

    private void loadSprite() { // Load standing sprite
        TextureRegion[][] textureFrames = TextureRegion.split(texture, 64, 64);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, 64, 64);
        currentFrame = textureFrames[0][0];
    }

    private void loadAnimations() {

        TextureRegion[][] textureFrames = TextureRegion.split(texture, 64, 64);
        walkFrames = new Array<TextureRegion>(9);

        /// Array of texture
        for(int i = 0; i < 9; i++) {
            walkFrames.insert(i, textureFrames[0][i + 1]);

        }

        walk = new Animation<TextureRegion>(.1f, walkFrames, Animation.PlayMode.LOOP);

    }

    // setDirection of spite
    public void setDirection(Direction direction, float delta){
        this.previousDirection = this.currentDirection;
        this.currentDirection = direction;
        switch (currentDirection){
            case DOWN:
                currentFrame = walk.getKeyFrame(frameTime);
                break;
            case UP:
                currentFrame = walk.getKeyFrame(frameTime);
                break;
            case LEFT:
                currentFrame = walk.getKeyFrame(frameTime);
                break;
            case RIGHT:
                currentFrame = walk.getKeyFrame(frameTime);
                break;
            default:
                break;
        }
    }


    /// Move
    public void move(Direction direction, float delta) {
        float x = currentPosition.x;
        float y = currentPosition.y;

        switch (direction){
            case DOWN:
                y -= velocity.y;
                break;
            case UP:
                y += velocity.y;
                break;
            case LEFT:
                x -= velocity.x;
                break;
            case RIGHT:
                x += velocity.x;
                break;
            default:
                break;
        }
        nextPosition.x = x;
        nextPosition.y = y;


    }

    public void setState(State state) {
        this.state = state;
    }

    public Sprite getFrameSprite() {
        return frameSprite;
    } // getFrameSprite

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void setCurrentPosition(float x, float y){
        frameSprite.setX(x);
        frameSprite.setY(y);
        this.currentPosition.x = x;
        this.currentPosition.y = y;
        this.nextPosition.x = x;
        this.nextPosition.y = y;
    }
    public void setCurrentToNext(){
        setCurrentPosition(nextPosition.x, nextPosition.y);
    }//  set current position to next pos

    public static Rectangle getBoundingBox() {
        return boundingBox;
    }

    public static float getPlayerCenterX() {
        return boundingBox.x + boundingBox.width/2;
    }
    public static float getPlayerCenterY() {
        return boundingBox.y + boundingBox.height/2;
    }


}
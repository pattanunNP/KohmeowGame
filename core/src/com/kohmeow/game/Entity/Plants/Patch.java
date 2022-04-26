package com.kohmeow.game.Entity.Plants;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kohmeow.game.resource.ResourceMannager;

public class Patch extends Sprite {

    private Vector2 position;
    private boolean isWatered;
    private float centerX, centerY;
    private Sprite framSprite;
    private TextureRegion currentFrame;

    private Crop crop;
    private String type;

    private ResourceMannager rm;

    private Sprite frameSprite;
    private TextureRegion[][] textureFrames;

    private Rectangle boundingBox;

    public Patch(float x, float y) {

        this.rm = new ResourceMannager();

        this.position = new Vector2(x, y);
        this.isWatered = false;
        centerX = x - 16;
        centerY = y - 16;

        textureFrames = rm.getTextureRegion("Entity/DirtPatch/DirtPatch.png", 32, 32);

        frameSprite = new Sprite(textureFrames[0][0], 0, 0, 32, 32);
        frameSprite.setX(Math.round(centerX / 32) * 32);
        frameSprite.setY(Math.round(centerY / 32) * 32);

        System.out.println("Patch Created");

        if (type.equals("dirt")) {

            currentFrame = textureFrames[1][0];

        } else if (type.equals("grass")) {

            currentFrame = textureFrames[0][0];

        }

        this.boundingBox = new Rectangle(position.x, position.y, 32, 32);

        this.type = "dirt";

    }

    public Patch(float x, float y, String type) {

        this.rm = new ResourceMannager();

        this.position = new Vector2(x, y);
        this.isWatered = false;
        centerX = x - 16;
        centerY = y - 16;

        textureFrames = rm.getTextureRegion("Entity/DirtPatch/DirtPatch.png", 32, 32);

        frameSprite = new Sprite(textureFrames[1][0], 0, 0, 32, 32);
        frameSprite.setX(Math.round(centerX / 32) * 32);
        frameSprite.setY(Math.round(centerY / 32) * 32);

        System.out.println("Patch Created");
        if (type.equals("dirt")) {

            currentFrame = textureFrames[1][0];

        } else if (type.equals("grass")) {

            currentFrame = textureFrames[0][0];

        }

        this.boundingBox = new Rectangle(position.x, position.y, 32, 32);

        this.type = type;

    }

    public void addDay() {

        this.isWatered = false;

    }

    public boolean IsEmpty() {
        if (this.crop != null) {
            return false;
        } else {
            return true;
        }
    }

    public void setWatered() {
        isWatered = true;
        if (this.crop != null) {
            crop.setWatered(true);
        }

    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Sprite getFrameSprite() {
        return this.frameSprite;
    }

    public boolean isWatered() {

        if (type.equals("dirt")) {
            if (isWatered) {
                currentFrame = textureFrames[1][1];
            } else {
                currentFrame = textureFrames[1][0];
            }

        }
        if (type.equals("grass")) {
            if (isWatered) {
                currentFrame = textureFrames[0][1];
            } else {
                currentFrame = textureFrames[0][0];
            }

        }
        return isWatered;
    }

    public void Plant(Crop crop) {
        this.crop = crop;

    }

    public void unPlant() {

        this.crop = null;
    }

    public Rectangle getBoundingBox() {
        return this.boundingBox;
    }

    public Patch contains(float x, float y) {

        System.out.println(String.format("That X: %f This X: %f", position.x, x));

        System.out.println(String.format("That Y: %f This Y: %f", position.y, y));

        if (position.x == x && position.y == y) {
            return this;
        }
        return null;

    }

    public Patch getPatch() {
        return this;
    }
}

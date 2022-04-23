package com.kohmeow.game.Controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;
import com.kohmeow.game.KohMeowGame;
import com.kohmeow.game.Entity.Plants.Crop;
import com.kohmeow.game.Entity.Player.Player;
import com.kohmeow.game.screen.GameScreen;

public class PlayerController implements InputProcessor {

    private Player player;
    private Crop crop;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean jump;
    private boolean toggleInventory;
    private GameScreen screen;
    Vector3 tp;

    public PlayerController(GameScreen screen, Player player) {
        this.player = player;
        this.screen = screen;
        tp = new Vector3();
        left = false;
        right = false;
        up = false;
        down = false;
        jump = false;
        toggleInventory = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
            this.left = true;
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
            this.up = true;
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
            this.down = true;
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
            this.right = true;

        // if (keycode == Input.Keys.NUM_1) {
        // System.out.println("Select" + screen.getItems().get(0).getName());
        // screen.setSelectedItem(screen.getItems().get(0));
        // }

        // if (keycode == Input.Keys.NUM_2) {
        // System.out.println("Select" + screen.getItems().get(1).getName());
        // screen.setSelectedItem(screen.getItems().get(1));
        // }

        // if (keycode == Input.Keys.NUM_3) {
        // System.out.println("Select" + screen.getItems().get(2).getName());
        // screen.setSelectedItem(screen.getItems().get(2));
        // }

        // if (keycode == Input.Keys.NUM_4) {
        // System.out.println("Select" + screen.getItems().get(3).getName());
        // screen.setSelectedItem(screen.getItems().get(3));
        // }

        // if (keycode == Input.Keys.NUM_5) {
        // System.out.println("Select" + screen.getItems().get(4).getName());
        // screen.setSelectedItem(screen.getItems().get(4));
        // }

        // if (keycode == Input.Keys.NUM_6) {
        // System.out.println("Select" + screen.getItems().get(5).getName());
        // screen.setSelectedItem(screen.getItems().get(5));
        // }

        // if (keycode == Input.Keys.NUM_7) {
        // System.out.println("Select" + screen.getItems().get(6).getName());
        // screen.setSelectedItem(screen.getItems().get(6));
        // }

        // if (keycode == Input.Keys.NUM_8) {
        // System.out.println("Select" + screen.getItems().get(7).getName());
        // screen.setSelectedItem(screen.getItems().get(7));
        // }
        if (keycode == Input.Keys.E) {
            System.out.println("Toggle Inventory");
            this.toggleInventory = true;

        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
            this.left = false;
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W)
            this.up = false;
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
            this.down = false;
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
            this.right = false;
        if (keycode == Input.Keys.E) {
            System.out.println("Toggle Inventory");
            this.toggleInventory = false;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 coords = screen.getCam().unproject(tp.set(screenX, screenY, 0));

        if (Math.abs(player.getPlayerCenterX() - coords.x) < 50
                && Math.abs(player.getPlayerCenterY() - coords.y) < 50) {

            TiledMapTileLayer ground = (TiledMapTileLayer) screen.getMap().getLayers().get("Ground");

            TiledMapTileLayer.Cell cell = ground.getCell(Math.round(coords.x * KohMeowGame.UNIT_SCALE),
                    Math.round(coords.y * KohMeowGame.UNIT_SCALE));

            System.out.println(cell != null);
            if (cell != null) {
                System.out.println("Cell: " + cell.getTile().getId());
                if (cell.getTile().getId() == 99) {
                    crop = new Crop("Carrot", coords.x, coords.y);
                    screen.addCrop(crop);

                    screen.numCrops++;
                }
            }
            return false;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // System.out.println(String.format("Mouse: Pos (%d,%d)", screenX, screenY));

        return false;
    }

    @Override
    public boolean scrolled(float amount, float amount2) {
        // System.out.println(String.format("Scroll: (%f,%f)", amount, amount2));

        return true;
    }

    public void update(float delta) {
        processInput(delta);
    }

    private void processInput(float delta) {
        // System.out.println(" Left: " + left + " Right: " + right + " Up: " + up + "
        // Down: " + down);
        // System.out.println("State: " + player.getState());
        // System.out.println("Direction : " + player.getDirection());
        // System.out.println(String.format("Player: Pos
        // (%d,%d)",player.getX(),player.getY()));
        if (up) {
            player.move(Player.Direction.WALKING_UP, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.WALKING_UP, delta);
        } else if (down) {
            player.move(Player.Direction.WALKING_DOWN, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.WALKING_DOWN, delta);
        } else if (right) {
            player.move(Player.Direction.WALKING_RIGHT, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.WALKING_RIGHT, delta);
        } else if (left) {
            player.move(Player.Direction.WALKING_LEFT, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.WALKING_LEFT, delta);
        } else if (jump) {
            player.move(Player.Direction.JUMP, delta);
            player.setState(Player.State.IDLE);

        } else if (!up && !down && !left && !right) {
            player.setState(Player.State.IDLE);

            if (player.getDirection() == Player.Direction.WALKING_UP && player.getState() == Player.State.IDLE) {
                // System.out.println("IDLE UP");
                player.setDirection(Player.Direction.UP, delta);

            } else if (player.getDirection() == Player.Direction.WALKING_DOWN
                    && player.getState() == Player.State.IDLE) {
                // System.out.println("IDLE DOWN");
                player.setDirection(Player.Direction.DOWN, delta);

            } else if (player.getDirection() == Player.Direction.WALKING_RIGHT
                    && player.getState() == Player.State.IDLE) {
                // System.out.println("IDLE RIGHT");
                player.setDirection(Player.Direction.RIGHT, delta);

            } else if (player.getDirection() == Player.Direction.WALKING_LEFT
                    && player.getState() == Player.State.IDLE) {
                // System.out.println("IDLE LEFT");
                player.setDirection(Player.Direction.LEFT, delta);
            }
        }

    }
}
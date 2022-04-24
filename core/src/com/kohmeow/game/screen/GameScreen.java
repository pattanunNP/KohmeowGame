package com.kohmeow.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kohmeow.game.KohMeowGame;
import com.kohmeow.game.Controller.PlayerController;
import com.kohmeow.game.Entity.Plants.Crop;
import com.kohmeow.game.Entity.Player.Player;
import com.kohmeow.game.Items.Item;
import com.kohmeow.game.resource.ResourceMannager;
import com.kohmeow.game.utils.Crosshair;
import com.kohmeow.game.utils.GameTimeClock;
import com.kohmeow.game.utils.MapLoader;

import com.kohmeow.game.utils.Timer;

public class GameScreen extends ScreenAdapter {
    // Instance Var
    private KohMeowGame game;

    private OrthographicCamera cam; // Camera
    private Viewport gameView;
    private Vector3 tp;

    // Tilemap
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer; // Tiledmap Renderer
    // Private MapLoader loader;
    private int mapWidth;
    private int mapHeight;
    private TextureRegion[][] textureFrames;
    private TextureRegion[][] textureFrames2;
    private MapObject object;

    private Viewport viewport;

    private Viewport infoport;

    // Player
    private Player player;
    private Sprite currentPlayerSprite;
    private TextureRegion currentPlayerFrame;

    private PlayerController controller;

    private Stage stage;

    private MapLoader mapLoader;

    private Texture box;
    private Texture border;
    private Texture info;

    public int numCrops;
    public int intType;

    private Music music;
    public Array<Crop> crops;

    private Array<Item> items;
    private Texture mouseCrop;
    private BitmapFont font;

    private ResourceMannager rm;

    public Item currentItem;
    public String currentType;
    public TextureRegion itemFrame;

    private int currentDays;
    private int totalDays;
    private int daysLeft;

    private Timer timer;
    private GameTimeClock clock;
    private int money;

    public int numCrosshair;
    private Array<Crosshair> Crosshairs;

    private Item waterPot;
    private Item shovel;
    private Item carrotSeed;
    private Item cornSeed;
    private Item wheatSeed;
    private Item potatoSeed;
    private Item corn;
    private Item carrot;
    private Item potato;
    private int currentIndex;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;
    private FreeTypeFontParameter parameter2;

    private BitmapFont font_info;
    private String time;
    private ShapeRenderer shapeRenderer;

    private Item wheat;

    public GameScreen(KohMeowGame game) {

        this.game = game;
        this.create();
        intType = 0;

        totalDays = 30;
        currentDays = 0;
        daysLeft = 30;
        money = 1000;

        cam = new OrthographicCamera();
        gameView = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        cam.setToOrtho(false, gameView.getWorldWidth(), gameView.getWorldHeight());

        map = new TmxMapLoader().load("Map/Base.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(cam);

        mapLoader = new MapLoader(this);

        cam.zoom = .5f;

        timer = new Timer();
        timer.StartNew(60, true, true);
        timer.setStartTime(0, 12, 0, 0);
        clock = new GameTimeClock(timer);

        mapWidth = map.getProperties().get("width", Integer.class) * 32;
        mapHeight = map.getProperties().get("height", Integer.class) * 32;

        player = new Player();
        controller = new PlayerController(this, player);
        player.startingPosition(mapLoader.getPlayerSpawnPoint().x, mapLoader.getPlayerSpawnPoint().y);
        currentPlayerSprite = player.getFrameSprite();
        shapeRenderer = new ShapeRenderer();

        mouseCrop = new Texture("Items/Plants.png");

        textureFrames = TextureRegion.split(mouseCrop, 32, 32);
        textureFrames2 = TextureRegion.split(new Texture("UI/Crosshair2.png"), 32, 32);

        crops = new Array<Crop>();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());

        stage = new Stage(viewport, game.batch);

        rm = new ResourceMannager();
        box = rm.getTexture("UI/Box.png");
        border = rm.getTexture("UI/Crosshair.gif");
        info = rm.getTexture("UI/info.png");

        waterPot = new Item("WaterPot", "tools");
        shovel = new Item("Shovel", "tools");

        carrotSeed = new Item("CarrotSeed", "plants_seed", 20);
        cornSeed = new Item("CornSeed", "plants_seed", 10);
        wheatSeed = new Item("WheatSeed", "plants_seed", 30);
        potatoSeed = new Item("PotatoSeed", "plants_seed", 10);

        carrot = new Item("Carrot", "plants_product", 0);
        corn = new Item("Corn", "plants_product", 0);
        potato = new Item("Potato", "plants_product", 0);
        wheat = new Item("Wheat","plants_product", 0);

        items = new Array<Item>(10);
        items.insert(0, waterPot);
        items.insert(1, shovel);
        items.insert(2, carrotSeed);
        items.insert(3, cornSeed);
        items.insert(4, wheatSeed);
        items.insert(5, potatoSeed);
        items.insert(6, carrot);
        items.insert(7, corn);
        items.insert(8, potato);
        items.insert(9, wheat);

        setSelectedItem(waterPot);
        setCurrentIndex(0);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();

        inputMultiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(inputMultiplexer);

        music = rm.musicTheme;
        music.setLooping(true);
        music.setVolume(.5f);
        music.play();

    }

    public void create() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/PixelFJVerdana12pt.ttf"));

        parameter = new FreeTypeFontParameter();
        parameter.size = 5;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.WHITE;

        parameter2 = new FreeTypeFontParameter();
        parameter2.size = 6;
        parameter2.color = Color.BROWN;
        parameter2.borderWidth = 1;
        parameter2.borderStraight = false;
        parameter2.borderColor = Color.BLACK;

        font = generator.generateFont(parameter);
        font_info = generator.generateFont(parameter2);
    }

    @Override
    public void show() {

    }

    public boolean checkCollision(Rectangle boundingBox, MapLayer objectLayer) {
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rectangle)) {
                    System.out.println("Collision");
                    System.out.println(boundingBox.getX() + "," + boundingBox.getY());
                    System.out.println(rectangle.getX() + "," + rectangle.getY());
                    System.out.println("Check is player over stack: " + (boundingBox.getY() > rectangle.getY()));

                    return true;
                }
            }

        }
        return false;
    }

    public boolean isCollision(Rectangle boundingBox) {
        MapLayer objectLayer = map.getLayers().get("Collision");

        return checkCollision(boundingBox, objectLayer);
    }

    @Override
    public void render(float delta) {

        // System.out.println("Player X: " + player.getX() + " Y: " + player.getY());
        // System.out.println("Num Crops: " + numCrops);
        // System.out.println("Num Crosshair: " + numCrosshair);

        Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        cam.update();
        player.update(delta);

        if (currentPlayerSprite.getX() + (cam.viewportWidth / 2 * cam.zoom) < mapWidth
                && currentPlayerSprite.getX() - (cam.viewportWidth / 2 * cam.zoom) > 0) {
            cam.position.x = Player.getPlayerCenterX();
        }

        if (currentPlayerSprite.getY() + (cam.viewportHeight / 2 * cam.zoom) < mapHeight
                && currentPlayerSprite.getY() - cam.viewportHeight / 2 * cam.zoom > 0) {
            cam.position.y = Player.getPlayerCenterY();
        }
        if (!isCollision(Player.getBoundingBox()))
            player.setCurrentToNext();

        controller.update(delta);

        game.batch.setProjectionMatrix(cam.combined);

        currentPlayerFrame = player.getCurrentFrame();

        renderer.setView(cam);

        clock.act(delta);

        if (timer.getDaysPast() != currentDays) {
            for (Crop crops : crops)
                crops.addDay();
            currentDays = timer.getDaysPast();
            daysLeft--;
            System.out.println(String.format("Day left %d ", daysLeft));

        }
        time = timer.getFormattedTimeofDay();

        game.batch.begin();

        for (int i = 0; i < numCrops; i++) {

            if (crops.get(i).isWatered())
                game.batch.setColor(Color.BROWN);

            game.batch.draw(textureFrames[0][1], crops.get(i).getFrameSprite().getX(),
                    crops.get(i).getFrameSprite().getY() - 6);
            game.batch.setColor(Color.WHITE);

            
          
            game.batch.draw(crops.get(i).getCurrentFrame(), crops.get(i).getFrameSprite().getX(),
                    crops.get(i).getFrameSprite().getY());

        }
        game.batch.draw(currentItem.getTextureRegion(), currentPlayerSprite.getX() + 16,
                currentPlayerSprite.getY() + 64, 24, 24);

        // System.out.println("Select Item: " + currentItem.getName());
        for (int i = 0; i < 10; i++) {

            game.batch.draw(box, (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)),
                    cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
            if (i < items.size) {
                // System.out.println(items.get(i).getName() + " " + items.get(i).getType() + "
                // " + items.get(i).getNum());
                game.batch.draw(items.get(i).getTextureRegion(), (cam.position.x + 32 * i) -
                        (cam.viewportWidth / 2 * (cam.zoom / 2)),
                        cam.position.y - (cam.viewportHeight / 2 * cam.zoom));

                if (items.get(i).getType() == "plants_product" || items.get(i).getType() == "plants_seed")
                    font.draw(game.batch, String.format("x%d", items.get(i).getNum()),
                            (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2) - 6) + 5,
                            cam.position.y - (cam.viewportHeight / 2 * cam.zoom) + 12);
                if (items.get(i).getItem() == currentItem.getItem()) {
                    game.batch.draw(border, (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)),
                            cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
                }
            }
        }
        game.batch.draw(info, (cam.position.x) - (cam.viewportWidth / 4),
                (cam.position.y) + (cam.viewportHeight / 3 * (cam.zoom / 2) + 20), 230, 70);

        font_info.draw(game.batch, String.format(" Days: %d/%d", currentDays, totalDays),
                (cam.position.x) - (cam.viewportWidth / 4) + 30,
                (cam.position.y) + 135);

        font_info.draw(game.batch, String.format("Money: %d $", money),
                (cam.position.x) - (cam.viewportWidth / 4) + 30,
                (cam.position.y) + 110);

        font_info.draw(game.batch, String.format("Time: %s", time),
                (cam.position.x) - (cam.viewportWidth / 4) + 115,
                (cam.position.y) + 135);
        game.batch.draw(currentPlayerFrame, currentPlayerSprite.getX(), currentPlayerSprite.getY());

        game.batch.end();
        shapeRenderer.setProjectionMatrix(cam.combined);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(clock.getAmbientLighting());
        Matrix4 mat = cam.combined.cpy();
        shapeRenderer.setProjectionMatrix(mat);
        mat.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        shapeRenderer.rect(cam.position.x - gameView.getWorldWidth() / 2,
                cam.position.y - gameView.getWorldHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
        game.batch.setProjectionMatrix(mat);
        shapeRenderer.setColor(Color.WHITE);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.act(delta);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public Array<Item> getItems() {
        return items;
    }

    public void setSelectedItem(Item item) {
        itemFrame = item.getTextureRegion();
        currentItem = item;
        currentType = item.getType();

    }

    public void setCurrentIndex(int index) {
        currentIndex = index;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public TiledMap getMap() {
        return map;
    }

    public void addCrop(com.kohmeow.game.Entity.Plants.Crop crop) {
        crops.add(crop);
    }

    public void addCrosshiar(com.kohmeow.game.utils.Crosshair crosshair) {
        Crosshairs.add(crosshair);
    }

    public Array<Crop> getCrops() {
        return crops;
    }

    public int getCurrentDays() {
        return currentDays;
    }

    public void addMoney(int price) {
        money += price;

    }

    public void addProduct(Item item, int ,amount){
        item.addAmount();
    }
    public void removeSeed(Item item) {
        item.removeAmount(1);
    }

    @Override
    public void dispose() {
        stage.dispose();
        map.dispose();
        box.dispose();
        music.dispose();
        generator.dispose();
    }
}

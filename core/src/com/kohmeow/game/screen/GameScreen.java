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
import com.badlogic.gdx.math.MathUtils;
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
import com.kohmeow.game.Entity.Plants.Patch;
import com.kohmeow.game.Entity.Player.Player;
import com.kohmeow.game.Items.Item;
import com.kohmeow.game.UI.HudInventory;
import com.kohmeow.game.UI.HudItem;
import com.kohmeow.game.UI.Info;
import com.kohmeow.game.resource.ResourceMannager;
import com.kohmeow.game.utils.GameTimeClock;
import com.kohmeow.game.utils.MapLoader;
import com.kohmeow.game.utils.SaveController;
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

    private Viewport viewport;

    // Player
    private Player player;
    private Sprite currentPlayerSprite;
    private TextureRegion currentPlayerFrame;

    private PlayerController controller;

    private Stage stage;

    private MapLoader mapLoader;

    private Texture box;
    private Texture border;

    public int numCrops;
    public int numPatch;



    private Music music;
    public Array<Crop> crops;
    public Array<Patch> patchs;

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



    private Info info;
    private HudInventory hudInventory;
    private HudItem hudItem;

    private Item waterPot;
    private Item shovel;
    private Item carrotSeed;
    private Item cornSeed;
    private Item wheatSeed;
    private Item potatoSeed;
    private Item corn;
    private Item carrot;
    private Item potato;
    private Item wheat;
    private int currentIndex;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;
    private FreeTypeFontParameter parameter2;
    private FreeTypeFontParameter parameter3;

    private BitmapFont font_info;
    private String time;
    private ShapeRenderer shapeRenderer;
    private Vector3 mousePos;

    private BitmapFont font_name;

    private SaveController SaveController;

    private Item sickle;

    public GameScreen(KohMeowGame game) {

        this.game = game;
        this.create();

   
        numPatch = 0;
        numCrops = 0;
        totalDays = 30;
        currentDays = 1;
        daysLeft = 30;
        money = 1000;

        SaveController = new SaveController();

        cam = new OrthographicCamera();
        gameView = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        cam.setToOrtho(false, gameView.getWorldWidth(), gameView.getWorldHeight());

        map = new TmxMapLoader().load("Map/Base.tmx");
        mapWidth = map.getProperties().get("width", Integer.class) * 32;
        mapHeight = map.getProperties().get("height", Integer.class) * 32;
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(cam);

        mapLoader = new MapLoader(this);

        cam.zoom = .5f;

        timer = new Timer();
        timer.StartNew(60, true, true);
        timer.setStartTime(0, 12, 0, 0);
        clock = new GameTimeClock(timer);

        mousePos = new Vector3();

        player = new Player();
        controller = new PlayerController(this, player);
        player.startingPosition(mapLoader.getPlayerSpawnPoint().x, mapLoader.getPlayerSpawnPoint().y);
        currentPlayerSprite = player.getFrameSprite();
        shapeRenderer = new ShapeRenderer();

        crops = new Array<Crop>();
        patchs = new Array<Patch>();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());

        stage = new Stage(viewport, game.batch);

        mouseCrop = new Texture("Items/Plants.png");

        textureFrames = TextureRegion.split(mouseCrop, 32, 32);
        textureFrames2 = TextureRegion.split(new Texture("UI/Crosshair2.png"), 32, 32);

        rm = new ResourceMannager();
        box = rm.getTexture("UI/Box.png");
        border = rm.getTexture("UI/Crosshair.gif");

        waterPot = new Item("WaterPot", "tools");
        shovel = new Item("Shovel", "tools");
        sickle = new Item("Sickle", "tools");

        carrotSeed = new Item("CarrotSeed", "plants_seed", 20);
        cornSeed = new Item("CornSeed", "plants_seed", 10);
        wheatSeed = new Item("WheatSeed", "plants_seed", 10);
        potatoSeed = new Item("PotatoSeed", "plants_seed", 10);

        carrot = new Item("Carrot", "plants_product", 0);
        corn = new Item("Corn", "plants_product", 0);
        potato = new Item("Potato", "plants_product", 0);
        wheat = new Item("Wheat", "plants_product", 0);

        items = new Array<Item>(11);
        items.insert(0, waterPot);
        items.insert(1, shovel);
        items.insert(2, sickle);
        items.insert(3, carrotSeed);
        items.insert(4, cornSeed);
        items.insert(5, wheatSeed);
        items.insert(6, potatoSeed);
        items.insert(7, carrot);
        items.insert(8, corn);
        items.insert(9, potato);
        items.insert(10, wheat);

        setSelectedItem(waterPot);
        setCurrentIndex(0);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(inputMultiplexer);

        info = new Info(game.batch, cam, font_info, rm);
        hudInventory = new HudInventory(game.batch, cam, font_name, rm);
        hudItem = new HudItem(game.batch, cam, font_name, rm);

        music = rm.musicTheme;
        music.setLooping(true);
        music.setVolume(.2f);
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

        parameter3 = new FreeTypeFontParameter();
        parameter3.size = 5;
        parameter3.color = Color.WHITE;
        parameter3.borderWidth = 1;
        parameter3.borderColor = Color.BLACK;

        font = generator.generateFont(parameter);
        font_info = generator.generateFont(parameter2);
        font_name = generator.generateFont(parameter3);
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
        // System.out.println("Num Patchs: " + numPatch);
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

        if(isShop(player.getBoundingRectangle()))
            System.out.println("Shop");

        controller.update(delta);

        game.batch.setProjectionMatrix(cam.combined);

        currentPlayerFrame = player.getCurrentFrame();

        renderer.setView(cam);

        clock.act(delta);

        if (timer.getDaysPast() != currentDays) {
            for (Patch patchs : patchs)
                patchs.addDay();
            currentDays = timer.getDaysPast();
            daysLeft--;
            System.out.println(String.format("Day left %d ", daysLeft));

        }
        time = timer.getFormattedTimeofDay();

        game.batch.begin();

        for (int j = 0; j < numPatch; j++) {

            game.batch.draw(patchs.get(j).getCurrentFrame(), patchs.get(j).getFrameSprite().getX(),
                    patchs.get(j).getFrameSprite().getY());

        }

        for (int i = 0; i < numCrops; i++) {

            game.batch.draw(crops.get(i).getCurrentFrame(), crops.get(i).getFrameSprite().getX(),
                    crops.get(i).getFrameSprite().getY());

        }

        float x = (mousePos.x - 16);
        float y = (mousePos.y - 16);

        game.batch.setColor(255, 255, 255, 0.5f);
        game.batch.draw(border, x, y);
        game.batch.setColor(Color.WHITE);

        // System.out.println("Select Item: " + currentItem.getName());

        info.draw(currentDays, totalDays, money, time); // Draw info
        hudInventory.draw(items, currentItem, font); // Draw inventory
        hudItem.draw(currentPlayerSprite, currentItem, font); // Draw item

        // Draw Player

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

    public void addCrop(Crop crop) {
        crops.add(crop);
        numCrops++;
    }

    public void removeCrop(int ID, String return_product, int return_amount) {
        int IndexToRemove = 0;
        if (numCrops > 0) {
            for (int i = 0; i < numCrops; i++) {
                if (crops.get(i).getID() == ID) {
                    IndexToRemove = i;
                }
            }
            crops.removeIndex(IndexToRemove);
            numCrops--;
        }

    }

    public void addPatch(Patch patch) {
        patchs.add(patch);
        numPatch++;
    }

    public void removePatch(int index) {
        patchs.removeIndex(index);
        numPatch--;
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
    public boolean isShop(Rectangle boundingBox) {
        Rectangle rectangle = new Rectangle();
        for (MapObject shop: map.getLayers().get("Shop").getObjects()) {
            rectangle = ((RectangleMapObject) shop).getRectangle();
            
        }
        return rectangle.overlaps(boundingBox);
    }

    public void addProduct(String name, int amount, String seedName) {
        System.out.println(name + ":" + amount);
        for (int i = 0; i < items.size; i++) {
            System.out.println(
                    String.format("%s %s => %b", items.get(i).getName(), name, items.get(i).getName().equals(name)));
            if (items.get(i).getName().equals(name)) {
                items.get(i).addAmount(amount);
                System.out.println("Added");
                int random = MathUtils.random.nextInt(3);
                addSeed(seedName, random);
            }

        }
    }

    public void addSeed(String name, int amount) {
        for (int i = 0; i < items.size; i++) {
            if (items.get(i).getName() == name) {
                items.get(i).addAmount(amount);

            }
        }
    }

    public void removeSeed(String name) {

        for (int i = 0; i < items.size; i++) {
            if (items.get(i).getName() == name) {
                items.get(i).removeAmount(1);
            }

        }
    }

    public int getNumSeed(String name) {
        int num = 0;
        for (int i = 0; i < items.size; i++) {
            if (items.get(i).getName() == name) {
                num = items.get(i).getNum();

            }

        }
        return num;
    }

    public void GameSave() {
        SaveController.saveGame(money, crops);

    }

    public void buySeed(String name) {
        int price = 0;
        for (int i = 0; i < items.size; i++) {
            if (items.get(i).getName() == name) {
                price = items.get(i).getPrice();

            }

        }

        if (price <= money) {
            money -= price;
            addSeed(name, 2);
        }

    }

    public void SetmousePos(Vector3 coords) {
        mousePos = coords;
    }

    @Override
    public void dispose() {

        stage.dispose();
        map.dispose();
        box.dispose();
        music.dispose();
        generator.dispose();
        font_name.dispose();
        font.dispose();
        font_info.dispose();

    }
}

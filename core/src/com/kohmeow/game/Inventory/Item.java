package com.kohmeow.game.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kohmeow.game.resource.ResourceMannager;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Item {
    private TextureRegion textureRegion;

    private String name;
    private String description;
    private Texture texture;
    private int price;
    private String type;

    private int nums;

    private JsonValue itemInfo;
    private JsonReader jsonReader;

    private JsonValue info;

    private ResourceMannager rm;

    public Item(String name, String type) {
        this.rm = new ResourceMannager();

        this.jsonReader = new JsonReader();

        this.type = type;

        this.nums = 1;

        loadinfo(name, type);
    }

    public Item(String name, String type, int init_num) {
        this.rm = new ResourceMannager();

        this.jsonReader = new JsonReader();

        this.type = type;

        loadinfo(name, type);

        this.nums = init_num;
    }

    private void loadinfo(String name, String type) {
        JsonValue itemInfo = jsonReader.parse(Gdx.files.internal("Items/Items.json"));
        JsonValue info = itemInfo.get(type);
        this.name = name;
        TextureRegion[][] texture = rm.getTextureRegion("Items/Items.png");
        // System.out.println(info.get(name).getString("file_path"));

        if (type == "tools") {
            int index_x = info.get(name).getInt("x");
            int index_y = info.get(name).getInt("y");
            this.description = info.get(name).getString("desc");
            this.textureRegion = texture[index_y][index_x];

        } else if (type == "plants_product") {
            int index_x = info.get(name).getInt("x");
            int index_y = info.get(name).getInt("y");
            this.description = info.get(name).getString("desc");
            this.price = info.get(name).getInt("price");
            this.textureRegion = texture[index_y][index_x];

        } else if (type == "plants_seed") {
            int index_x = info.get(name).getInt("x");
            int index_y = info.get(name).getInt("y");
            this.description = info.get(name).getString("desc");
            this.price = info.get(name).getInt("price");
            this.textureRegion = texture[index_y][index_x];

        }

        // this.description = info.getString("description");

    }

    public Texture getTexture() {
        return texture;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public String getType() {
        return type;
    }

    public Item getItem() {
        return this;
    }

    public int getNum() {
        return this.nums;

    }

    public String getName() {
        return name;
    }

    public String getFullDesc() {
        String ret = "";

        ret += description;

        return ret;
    }

}

package de.marcus.javagame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Item extends Entity{
    public static final long MAX_ITEM_LIFETIME = 600000;

    public long despawnTimer;

    public Item(Vector2 position, Texture texture) {
        super(position, texture);
        this.despawnTimer = MAX_ITEM_LIFETIME;
    }

    public void update() {
        despawnTimer =- 1;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(getTexture(),getPosition().x,getPosition().y);
    }
}

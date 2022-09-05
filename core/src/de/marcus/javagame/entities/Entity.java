package de.marcus.javagame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Marcus
 * <p>
 * Entity System: Entity
 * <p>
 * Intended for non-living entities (items).
 * Base class for Creature
 * @see Creature
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    private Vector2 position;
    private Texture texture;

    public Entity(float posX, float posY, Texture texture) {
        position = new Vector2(posX, posY);
        this.texture = texture;
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void render(SpriteBatch batch, float height, float width) {
        batch.draw(texture, position.x, position.y,width,height);
    }

    public void update() {

    }
    /**
     *
     * @param x Negative x = left
     * @param y Negative y = down
     */
    public void move(float x, float y) {
        position.set(position.x + x,position.y + y);
    }
}

package de.marcus.javagame.entities.agressive.sandbiom;

import com.badlogic.gdx.graphics.Texture;
import de.marcus.javagame.entities.agressive.Enemy;

public class Hyena extends Enemy {
    public Hyena(float posX, float posY, Texture texture, int maxHealth, int maxHunger, int maxArmor, int maxThirst, float movementSpeed, int damage, double attackSpeed, float range) {
        super(posX, posY, texture, maxHealth, maxHunger, maxArmor, maxThirst, movementSpeed, damage, attackSpeed, range);
    }
}
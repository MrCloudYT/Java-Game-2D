package de.marcus.javagame.entities.agressive.notbiomspecific;

import com.badlogic.gdx.graphics.Texture;
import de.marcus.javagame.entities.agressive.Enemy;

/**
 * @author Marcus
 * <p>
 * Entity System: Mage
 * <p>
 * Enemy Mage
 */
public class Mage extends Enemy {
    private DamageType damageType;

    public Mage(float posX, float posY, Texture texture, int maxHealth, int maxHunger, int maxArmor, int maxThirst, float movementSpeed, int damage, double attackSpeed, float range, DamageType damageType) {
        super(posX, posY, texture, maxHealth, maxHunger, maxArmor, maxThirst, movementSpeed, damage, attackSpeed, range);
        this.damageType = damageType;
    }

    public enum DamageType {
        FIRE, WATER, EARTH, WIND
    }
}
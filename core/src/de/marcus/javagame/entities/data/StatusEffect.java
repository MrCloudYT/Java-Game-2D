package de.marcus.javagame.entities.data;

import de.marcus.javagame.EffectType;
import de.marcus.javagame.entities.base.Creature;
import lombok.Getter;

@Getter
public class StatusEffect implements Cloneable {
    private EffectType effectType;
    //ms
    private long duration;
    private long nextCallTime;


    public StatusEffect(EffectType effectType, long duration) {
        this.effectType = effectType;
        this.duration = duration;
        nextCallTime = (long) (duration - (effectType.getApplyTime() * 1000));
    }

    public void update(Creature creature, long deltaTime) {
        decrementTimer(deltaTime, creature);
    }

    public boolean decrementTimer(long decrement, Creature creature) {
        duration -= decrement;
        //The next call time is the duration divided into segments by ApplyTime of the given effect
        //TODO: Maybe don't keep duration > 0 but idk yet
        if (duration <= nextCallTime && duration > 0) {

            creature.setHealth(Math.min(creature.getHealth() + (int) effectType.getDamage(), creature.getMaxHealth()));


            // apply time is in seconds, nextCallTime in ms
            nextCallTime -= effectType.getApplyTime() * 1000;
        }

        return duration < 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

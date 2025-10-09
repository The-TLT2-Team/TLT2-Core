package com.fg.tltmod.util.mixin;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;

public interface IManaBurstMixin{
    IntOpenHashSet tltmod$getHitEntityIdList();
    void tltmod$addToHitList(Entity entity);
    void tltmod$clearHitList();
    float tltmod$getBaseDamage();
    void tltmod$setBaseDamage(float baseDamage);
    int tltmod$getPerConsumption();
    void tltmod$setPerConsumption(int i);
    int tltmod$getPerBlockConsumption();
    void tltmod$setPerBlockConsumption(int i);
    int tltmod$getGeneration();
    void tltmod$setGeneration(int i);
    float tltmod$getDamageModifier();
    void tltmod$setDamageModifier(float f);

    default void addBaseDamage(float amount){
        this.tltmod$setBaseDamage(this.tltmod$getBaseDamage()+amount);
    }
    default void addEntityPerConsumption(int amount){
        this.tltmod$setPerConsumption(this.tltmod$getPerConsumption()+amount);
    }
    default void addBlockPerConsumption(int amount){
        this.tltmod$setPerBlockConsumption(this.tltmod$getPerBlockConsumption()+amount);
    }
    default void addGeneration(int amount){
        this.tltmod$setGeneration(this.tltmod$getGeneration()+amount);
    }
}

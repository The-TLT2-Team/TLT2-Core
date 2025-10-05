package com.fg.tltmod.api.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IMobCooldownProvider {
    int getCooldown(String key);
    void setCooldown(String key,int value);
    default void tickCooldown(Map<String,Integer> cooldownMap){
        List<String> keySet = new ArrayList<>(cooldownMap.keySet());
        keySet.forEach(key->{
            int ticks = cooldownMap.getOrDefault(key,0);
            if (ticks>0) cooldownMap.put(key,ticks-1);
        });
    }
}

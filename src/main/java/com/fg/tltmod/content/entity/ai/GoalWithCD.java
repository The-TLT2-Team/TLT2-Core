package com.fg.tltmod.content.entity.ai;

import com.fg.tltmod.api.interfaces.IMobCooldownProvider;
import net.minecraft.world.entity.ai.goal.Goal;

public class GoalWithCD extends Goal {
    protected final IMobCooldownProvider provider;
    protected final String cooldownKey;
    protected final int cd;
    public GoalWithCD(IMobCooldownProvider provider,String cooldownKey,int cd){
        this.provider = provider;
        this.cooldownKey = cooldownKey;
        this.cd = cd;
    }
    @Override
    public boolean canUse() {
        return provider.getCooldown(cooldownKey)<=0;
    }

    @Override
    public void stop() {
        provider.setCooldown(cooldownKey,cd);
        super.stop();
    }
}

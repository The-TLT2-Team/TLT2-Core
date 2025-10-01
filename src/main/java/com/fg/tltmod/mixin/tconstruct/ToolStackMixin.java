package com.fg.tltmod.mixin.tconstruct;

import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.util.TltcoreConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionData;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = ToolStack.class,remap = false)
public abstract class ToolStackMixin {
    @Shadow public abstract ToolDataNBT getPersistentData();

    @Inject(method = "rebuildStats",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/nbt/ModifierNBT$Builder;build()Lslimeknights/tconstruct/library/tools/nbt/ModifierNBT;",ordinal = 0),locals = LocalCapture.CAPTURE_FAILHARD)
    public void addCursedTrait(CallbackInfo ci, ToolDefinitionData toolData, MaterialNBT materials, ModifierNBT.Builder modBuilder){
        if (getPersistentData().getBoolean(TltcoreConstants.NbtLocations.KEY_IS_THE_CURSED_ONE)) modBuilder.add(TltCoreModifiers.THE_CURSED_ONE.get(),1);
    }
}

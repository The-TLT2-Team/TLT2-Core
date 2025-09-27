package com.fg.tltmod.data.providers;

import com.fg.tltmod.TltCore;
import net.minecraft.data.PackOutput;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.fluid.texture.AbstractFluidTextureProvider;
import slimeknights.mantle.fluid.texture.FluidTexture;


public class TltCoreFluidTextureProvider extends AbstractFluidTextureProvider {
    public TltCoreFluidTextureProvider(PackOutput packOutput) {
        super(packOutput, TltCore.MODID);
    }
    @Override
    public void addTextures() {
    }

    public FluidTexture.Builder commonFluid(FluidType fluid) {
        return super.texture(fluid).textures(TltCore.getResource("block/fluid/" + ForgeRegistries.FLUID_TYPES.get().getKey(fluid).getPath() + "/"), false, false);
    }

    @Override
    public String getName() {
        return "TLT Core Fluid Texture Provider";
    }
}

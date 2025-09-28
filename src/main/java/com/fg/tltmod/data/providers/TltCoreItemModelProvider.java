package com.fg.tltmod.data.providers;

import com.fg.tltmod.TltCore;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.registration.object.FluidObject;

public class TltCoreItemModelProvider extends ItemModelProvider {
    public static final String PARENT_SIMPLE_ITEM ="item/generated";
    public static final String PARENT_BUCKET_FLUID ="forge:item/bucket_drip";

    public TltCoreItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TltCore.MODID, existingFileHelper);
    }

    public void generateItemModel(Item item,String typePath){
        withExistingParent(ForgeRegistries.ITEMS.getKey(item).getPath(), PARENT_SIMPLE_ITEM).texture("layer0",getItemLocation(ForgeRegistries.ITEMS.getKey(item).getPath(),typePath));
    }
    public void generateBlockItemModel(BlockItem item){
        withExistingParent(ForgeRegistries.ITEMS.getKey(item).getPath(), getBlockItemLocation(ForgeRegistries.ITEMS.getKey(item).getPath()));
    }
    public void generateBucketItemModel(FluidObject<ForgeFlowingFluid> object,boolean flip){
        withExistingParent(object.getId().getPath()+"_bucket",PARENT_BUCKET_FLUID).customLoader((itemModelBuilder,existingFileHelper)->DynamicFluidContainerModelBuilder
                .begin(itemModelBuilder,existingFileHelper)
                .fluid(object.get())
                .flipGas(flip));
    }

    public ResourceLocation getItemLocation(String path,String typePath){
        return ResourceLocation.fromNamespaceAndPath(TltCore.MODID,"item/"+typePath+"/"+path);
    }
    public ResourceLocation getBlockItemLocation(String path){
        return ResourceLocation.fromNamespaceAndPath(TltCore.MODID,"block/"+path);
    }

    @Override
    protected void registerModels() {

    }
}

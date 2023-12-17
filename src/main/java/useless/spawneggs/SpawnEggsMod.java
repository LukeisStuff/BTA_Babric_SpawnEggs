package useless.spawneggs;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ItemHelper;
import turniplabs.halplibe.helper.TextureHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import useless.spawneggs.config.SpawnEggsConfig;
import useless.spawneggs.config.SpawnEggsConfigManager;
import useless.spawneggs.item.ItemSpawnEgg;

import java.awt.*;


public class SpawnEggsMod implements GameStartEntrypoint {
    public static final String MOD_ID = "spawneggs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final int[] baseEgg = TextureHelper.getOrCreateItemTexture(SpawnEggsMod.MOD_ID, "baseEgg.png");
    public static final int[] overlayEgg = TextureHelper.getOrCreateItemTexture(SpawnEggsMod.MOD_ID, "overlayEgg.png");
    public static int nextEggId;
    public static String baseKey = "item.spawneggs";
    public static SpawnEggsConfig config;
    public static boolean LOCK = true;
    public static Item createSpawnEgg(Class<? extends Entity> entityClass, Color colorBase, Color colorOverlay){
        return createSpawnEggWithNameOverride(entityClass, null, colorBase, colorOverlay);
    }
    public static Item createSpawnEggWithNameOverride(Class<? extends Entity> entityClass, String nameOverride, Color colorBase, Color colorOverlay){
        checkException();
        String entityName = EntityDispatcher.classToKeyMap.get(entityClass);
        String key = "spawn.egg." + entityName;
        int id = config.keyToIdMap.getOrDefault(key, findNextEggId());
        config.keyToIdMap.put(key, id);
        return ItemHelper.createItem(MOD_ID, new ItemSpawnEgg(key, id, entityClass, colorBase, colorOverlay, nameOverride), entityName.toLowerCase(), "spawnEggDefault.png");
    }
    public static void assignPickEntity(Class<? extends Entity> entityClass, IItemConvertible pickedItem){
        assignPickEntity(entityClass, pickedItem.getDefaultStack());
    }
    public static void assignPickEntity(Class<? extends Entity> entityClass, ItemStack pickedItem){
        checkException();
        ItemSpawnEgg.entityEggMap.put(entityClass, pickedItem);
    }
    private static void checkException(){
        if (LOCK){
            throw new RuntimeException("Do use method outside the 'spawneggs' entrypoint!");
        }
    }
    public static int findNextEggId(){
        while (Item.itemsList[nextEggId] != null){
            nextEggId++;
            if (nextEggId >= Item.itemsList.length){
                throw new RuntimeException("next Item id (" + nextEggId + ") is outside the valid range of length: " + Item.itemsList.length);
            }
        }
        return nextEggId;
    }

    @Override
    public void beforeGameStart() {
        config = SpawnEggsConfigManager.initializeConfig();
        nextEggId = Block.blocksList.length + config.startingIdOffset;
        if (config.regenAllIdsOnNextBoot){
            config.regenAllIdsOnNextBoot = false;
            config.keyToIdMap.clear();
        }
        LOCK = false;
        FabricLoader.getInstance().getEntrypoints("spawneggs", SpawnEggsEntrypoint.class).forEach(SpawnEggsEntrypoint::onLoad);
        LOCK = true;
        SpawnEggsConfigManager.save();
    }

    @Override
    public void afterGameStart() {

    }
}

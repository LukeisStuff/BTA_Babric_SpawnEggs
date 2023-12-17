package useless.spawneggs.item;

import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.core.block.entity.TileEntityMobSpawner;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import useless.prismaticlibe.ColoredTexture;
import useless.prismaticlibe.IColored;
import useless.spawneggs.SpawnEggsMod;

import java.awt.*;
import java.util.HashMap;
import java.util.Set;

public class ItemSpawnEgg extends Item implements IColored{
    public static HashMap<Class<? extends Entity>, ItemStack> entityEggMap = new HashMap<>();
    private final ColoredTexture[] eggsTextures;
    public Class<? extends Entity> entityClass;
    private final String nameOverrideKey;
    private String entityId;
    public ItemSpawnEgg(String name, int id, Class<? extends Entity> entityClass, Color colorBase, Color colorOverlay, String nameOverrideKey) {
        super(name, id);
        this.entityClass = entityClass;
        this.nameOverrideKey = nameOverrideKey;
        entityId = EntityDispatcher.classToKeyMap.get(entityClass);
        if (entityId == null){
            throw new RuntimeException("Provided class '" + entityClass.getName() + "' does not have a dispatcher id!");
        }
        eggsTextures = new ColoredTexture[]{new ColoredTexture(SpawnEggsMod.baseEgg, colorBase), new ColoredTexture(SpawnEggsMod.overlayEgg, colorOverlay)};
        SpawnEggsMod.assignPickEntity(entityClass, this);
    }
    public ItemSpawnEgg(String name, int id, Class<? extends Entity> entityClass, Color colorBase, Color colorOverlay){
        this(name, id, entityClass, colorBase, colorOverlay, null);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if (world.getBlockTileEntity(blockX, blockY, blockZ) instanceof TileEntityMobSpawner){
            TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getBlockTileEntity(blockX, blockY, blockZ);

            spawner.setMobId(entityId);
            return true;
        }
        itemstack.consumeItem(entityplayer);
        // Spawn entity from side used on
        blockX += side.getOffsetX();
        blockY += side.getOffsetY();
        blockZ += side.getOffsetZ();
        if (!world.isClientSide) {
            Set<String> keySet = EntityDispatcher.keyToClassMap.keySet();
            Class<? extends Entity> entityClass = null;
            for (String str : keySet) {
                if (!str.equalsIgnoreCase(entityId)) continue;
                entityId = str;
                entityClass = EntityDispatcher.keyToClassMap.get(entityId);
            }
            if (entityClass == null) {
                throw new CommandError("Could not find entity \"" + entityId + "\"");
            }

            // Spawn entity
            Entity entity = createEntity(entityClass, world);
            entity.moveTo(blockX + 0.5F, blockY, blockZ + 0.5F, entityplayer.yRot, 0.0f);
            entity.spawnInit();
            world.entityJoinedWorld(entity);
        }
        return true;
    }
    public String getTranslatedName(){
        I18n translator = I18n.getInstance();
        MobInfoRegistry.MobInfo info = MobInfoRegistry.getMobInfo(entityClass);
        if (nameOverrideKey != null){
            return translator.translateKey(nameOverrideKey);
        }
        if (info != null){
            return translator.translateKey(info.getNameTranslationKey());
        }
        return EntityDispatcher.classToKeyMap.get(entityClass);
    }
    public static Entity createEntity(Class<? extends Entity> entityClass, World world) {
        try {
            return entityClass.getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            throw new CommandError("Could not create Entity!");
        }
    }
    @Override
    public ColoredTexture[] getTextures(ItemStack itemStack) {
        return eggsTextures;
    }
}

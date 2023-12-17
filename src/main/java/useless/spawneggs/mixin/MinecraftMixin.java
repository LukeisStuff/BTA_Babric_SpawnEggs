package useless.spawneggs.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.HitResult;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.spawneggs.item.ItemSpawnEgg;
import useless.spawneggs.SpawnEggsMod;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
    @Shadow
    public EntityPlayerSP thePlayer;
    @Shadow
    public WorldRenderer worldRenderer;
    @Shadow
    public HitResult objectMouseOver;
    @Inject(method = "clickMiddleMouseButton()V", at = @At("HEAD"), cancellable = true)
    private void pickEntity(CallbackInfo ci){
        float ogReach = thePlayer.gamemode.getEntityReachDistance();
        thePlayer.gamemode.setEntityReachDistance(256);
        worldRenderer.getMouseOver(0);
        HitResult mouseOver = objectMouseOver;
        thePlayer.gamemode.setEntityReachDistance(ogReach);

        if (mouseOver != null && mouseOver.hitType == HitResult.HitType.ENTITY) {
            ItemStack item = ItemSpawnEgg.entityEggMap.get(mouseOver.entity.getClass());
            if (item != null){
                addItemToPlayer(item);
                ci.cancel();
            }
            else {
                SpawnEggsMod.LOGGER.warn("EntityClass: " + mouseOver.entity.getClass().getName() + " does not have a assigned pick entity item!");
            }
        }
    }

    @Unique
    private void addItemToPlayer(ItemStack itemStack){
        if (itemStack == null) {
            return;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = thePlayer.inventory.getStackInSlot(i);
            if (stack == null || stack.itemID != itemStack.itemID || stack.getMetadata() != itemStack.getMetadata()) continue;
            thePlayer.setCurrentItem(i);
            return;
        }
        int emptyHotbarSlot = -1;
        int slot = thePlayer.inventory.currentItem;
        for (int i = 0; i < 9; ++i) {
            if (thePlayer.inventory.getStackInSlot(i) != null) continue;
            emptyHotbarSlot = i;
            slot = i;
            break;
        }
        int itemSlot = -1;
        int stackSize = -1;
        for (int i = 9; i < 36; ++i) {
            ItemStack stack = thePlayer.inventory.getStackInSlot(i);
            if (stack == null || stack.itemID != itemStack.itemID || stack.getMetadata() != itemStack.getMetadata() || stackSize != -1 && stack.stackSize >= stackSize) continue;
            itemSlot = i;
            stackSize = stack.stackSize;
        }
        if (itemSlot == -1) {
            if (!thePlayer.getGamemode().consumeBlocks()) {
                int emptySlot = -1;
                for (int i = 0; i < thePlayer.inventory.getSizeInventory(); ++i) {
                    if (thePlayer.inventory.getStackInSlot(i) != null) continue;
                    emptySlot = i;
                    break;
                }
                int createItemInsertSlot = emptyHotbarSlot != -1 ? emptyHotbarSlot : thePlayer.inventory.currentItem;
                itemStack.stackSize = 1;
                if (emptySlot != -1) {
                    thePlayer.swapItems(emptySlot, createItemInsertSlot);
                }
                thePlayer.inventory.setInventorySlotContents(createItemInsertSlot, itemStack);
                thePlayer.setCurrentItem(createItemInsertSlot);
            }
            return;
        }
        thePlayer.swapItems(slot, itemSlot);
        thePlayer.setCurrentItem(slot);
    }
}

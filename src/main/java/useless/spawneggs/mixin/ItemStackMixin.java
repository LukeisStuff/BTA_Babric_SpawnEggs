package useless.spawneggs.mixin;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.spawneggs.item.ItemSpawnEgg;
import useless.spawneggs.SpawnEggsMod;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Shadow private CompoundTag tag;

    @Inject(method = "getDisplayName()Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void spawnEggNameConditions(CallbackInfoReturnable<String> cir){
        if (getItem() instanceof ItemSpawnEgg && !this.tag.getBoolean("overrideName")){
            ItemSpawnEgg spawnEgg = (ItemSpawnEgg) getItem();
            I18n translator = I18n.getInstance();
            cir.setReturnValue(spawnEgg.getTranslatedName() + " " + translator.translateKey(SpawnEggsMod.baseKey));
        }
    }
}

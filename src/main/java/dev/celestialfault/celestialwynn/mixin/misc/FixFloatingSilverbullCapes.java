package dev.celestialfault.celestialwynn.mixin.misc;

//? if <=1.21.1 {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.injection.At;
*///?}

import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;

// TODO this issue doesn't exist on 1.21.2+ with the armor rendering changes, so this should be entirely removed
//		whenever 1.21.1 support is dropped
@Mixin(PlayerEntityModel.class)
abstract class FixFloatingSilverbullCapes {
	//? if <=1.21.1 {
	/*@ModifyExpressionValue(
		method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
		)
	)
	public boolean celestialwynn$fixSilverbullCapes(boolean original, @Local(argsOnly = true) LivingEntity entity) {
		if(!CelestialWynn.isOnWynn() || !Config.getFixSilverbullCapes()) {
			return original;
		}

		var chestplate = entity.getEquippedStack(EquipmentSlot.CHEST);
		return (!chestplate.isEmpty() && chestplate.getItem().equals(Items.STONE_SHOVEL) && chestplate.getDamage() == 36) || original;
	}
	*///?}
}

package me.celestialfault.celestialwynn.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.celestialfault.celestialwynn.CelestialWynn;
import me.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityModel.class)
abstract class PlayerEntityModelMixin {
	@ModifyExpressionValue(
		method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
		)
	)
	public boolean removeHiddenArmorCapeAngle(boolean original, @Local(argsOnly = true) LivingEntity entity) {
		if(!CelestialWynn.isOnWynn || !Config.INSTANCE.fixSilverbullCapes.get()) {
			return original;
		}

		ItemStack chestplate = entity.getEquippedStack(EquipmentSlot.CHEST);
		return (!chestplate.isEmpty() && chestplate.getItem().equals(Items.STONE_SHOVEL) && chestplate.getDamage() == 36) || original;
	}
}

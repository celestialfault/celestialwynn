package dev.celestialfault.celestialwynn.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeFeatureRenderer.class)
abstract class FixFloatingSilverbullCapes {
	@ModifyExpressionValue(
		//? if >=1.21.4 {
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V",
		//?} else {
		/*method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
		*///?}
		at = @At(
			//? if >=1.21.4 {
			value = "FIELD",
			target = "Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;equippedChestStack:Lnet/minecraft/item/ItemStack;"
			//?} else {
			/*value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
			*///?}
		)
	)
	public ItemStack celestialwynn$fixSilverbullCapes(ItemStack original) {
		if(!CelestialWynn.isOnWynn() || !Config.getFixSilverbullCapes()) {
			return original;
		}

		return !original.isEmpty() && original.getItem().equals(Items.STONE_SHOVEL) && original.getDamage() == 36 ? ItemStack.EMPTY : original;
	}
}

package dev.celestialfault.celestialwynn.mixin.render;

import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// originally taken from from https://github.com/inglettronald/DulkirMod-Fabric/blob/master/src/main/java/com/dulkirfabric/mixin/render/HeldItemRendererMixin.java
// which in turn is taken from https://github.com/cosrnic/smallviewmodel/blob/main/src/main/java/uk/cosrnic/smallviewmodel/mixin/MixinHeldItemRenderer.java
@Mixin(HeldItemRenderer.class)
abstract class HeldItemScale {
	@Inject(
		method = "renderFirstPersonItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
		)
	)
	public void celestialwynn$modifyHeldItemScale(AbstractClientPlayerEntity player, float tickDelta, float pitch,
	                                              Hand hand, float swingProgress, ItemStack item, float equipProgress,
	                                              MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
	                                              CallbackInfo ci) {
		float x = Config.ItemScaling.getX() / 100f;
		float y = Config.ItemScaling.getY() / 100f;
		float z = Config.ItemScaling.getZ() / 100f;
		float scale = Config.ItemScaling.getScale();

		if(hand == Hand.OFF_HAND) {
			x = x > 0 ? -x : Math.abs(x);
		}

		matrices.translate(x, y, z);
		matrices.scale(scale, scale, scale);
	}
}

package me.celestialfault.celestialwynn.mixin;

import me.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// originally taken from from https://github.com/inglettronald/DulkirMod-Fabric/blob/master/src/main/java/com/dulkirfabric/mixin/render/HeldItemRendererMixin.java
// which in turn is taken from https://github.com/cosrnic/smallviewmodel/blob/main/src/main/java/uk/cosrnic/smallviewmodel/mixin/MixinHeldItemRenderer.java
@Mixin(HeldItemRenderer.class)
abstract class HeldItemRendererMixin {
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
		// ideally we would also affect the offhand, but wynn doesn't use the offhand for anything currently, and
		// properly doing so would require mirroring these matrix transforms, and I don't really feel like spending
		// time doing that for something you're possibly never going to see on wynn.
		if(hand != Hand.MAIN_HAND) return;

		Config.ItemScaling scaling = Config.INSTANCE.itemScaling;

		Vector3f position = new Vector3f(scaling.x.get(), scaling.y.get(), scaling.z.get()).div(100f);
		matrices.translate(position.x, position.y, position.z);

		float scale = scaling.scale.get();
		matrices.scale(scale, scale, scale);
	}
}

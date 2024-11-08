package dev.celestialfault.celestialwynn.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.config.Config;
import dev.celestialfault.celestialwynn.features.TerritoryBarHider;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

@Mixin(BossBarHud.class)
abstract class SkipTerritoryBarRender {
	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"))
	public <B extends ClientBossBar> Iterator<B> celestialwynn$dontRenderTerritoryBar(Collection<B> instance, Operation<Iterator<B>> original) {
		Iterator<B> iterator = original.call(instance);
		if(!Config.getHideTerritoryBar() || !CelestialWynn.isOnWynn()) {
			// noop if we aren't on wynn or don't have a target bossbar uuid
			return iterator;
		}

		return TerritoryBarHider.filterBars(iterator);
	}
}

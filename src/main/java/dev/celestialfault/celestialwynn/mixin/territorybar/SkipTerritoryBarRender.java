package dev.celestialfault.celestialwynn.mixin.territorybar;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.TerritoryBarHider;
import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

@Mixin(BossBarHud.class)
abstract class SkipTerritoryBarRender {
	// Note that we wrap the render iterator over simply canceling the relevant boss bar packets to avoid
	// breaking other mods which might depend on it actually existing, since all we want to do
	// is simply hide the rendered boss bar, with no real concern for *how* that's accomplished.
	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"))
	public <B extends ClientBossBar> Iterator<B> celestialwynn$dontRenderTerritoryBar(Collection<B> instance, Operation<Iterator<B>> original) {
		Iterator<B> iterator = original.call(instance);
		UUID target = TerritoryBarHider.getTerritoryBossbarUuid();
		if(!Config.getHideTerritoryBar() || !CelestialWynn.isOnWynn() || target == null) {
			// noop if we aren't on wynn or don't have a target bossbar uuid
			return iterator;
		}

		return TerritoryBarHider.filterBars(original.call(instance));
	}
}

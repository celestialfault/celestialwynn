package dev.celestialfault.celestialwynn.features

import net.minecraft.client.gui.hud.ClientBossBar
import java.util.UUID

object TerritoryBarHider {
	private val territoryBossbarUuid = UUID.fromString("d1ff1f36-d7c5-380f-9fa9-cd829c91cafe")

	@JvmStatic
	fun <B : ClientBossBar> filterBars(bars: Iterator<B>) = iterator {
		for(bar in bars) {
			if(bar.uuid != territoryBossbarUuid) yield(bar)
		}
	}
}

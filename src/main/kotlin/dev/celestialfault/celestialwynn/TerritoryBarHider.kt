package dev.celestialfault.celestialwynn

import net.minecraft.text.LiteralTextContent
import java.util.*
import java.util.regex.Pattern

object TerritoryBarHider {
	private val TERRITORY_REGEX = Pattern.compile("§c[^§]+§4 \\[\\w{3,5}]")

	// I'm unsure if this is actually a static UUID or if it's simply determined based on the player's own UUID
	// (or any similar means), but it *appears* to be static in some capacity. If not, oh well, we'll still
	// pick up on it at some point from an add/update name packet.
	@JvmStatic
	var territoryBossbarUuid: UUID? = UUID.fromString("d1ff1f36-d7c5-380f-9fa9-cd829c91cafe")
		private set

	@JvmStatic
	fun maybeHideBossBar(uuid: UUID, text: LiteralTextContent) {
		// we already know this bossbar is the territory one, so don't bother re-checking if it is
		if(uuid == territoryBossbarUuid) return

		if(TERRITORY_REGEX.matcher(text.string()).matches()) {
			CelestialWynn.LOGGER.info("Found territory bossbar with uuid {} and text {}", uuid, text)
			// Store the UUID of the territory bossbar for later hiding at render time; we do this instead of
			// simply canceling any packets relating to the boss bar to not break other mods that might still
			// depend on the boss bar existing.
			territoryBossbarUuid = uuid
		}
	}
}

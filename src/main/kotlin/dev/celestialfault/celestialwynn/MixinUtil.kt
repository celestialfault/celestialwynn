package dev.celestialfault.celestialwynn

import net.minecraft.client.gui.hud.ClientBossBar
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import java.util.*
import kotlin.jvm.optionals.getOrNull

object MixinUtil {
	/*? if >=1.21*/
	private val DING = Identifier.ofVanilla("entity.arrow.hit_player")
	/*? if <1.21*/
	/*private val DING = Identifier("minecraft", "entity.arrow.hit_player")*/

	private val territoryBossbarUuid: UUID = UUID.fromString("d1ff1f36-d7c5-380f-9fa9-cd829c91cafe")

	@JvmStatic
	fun <B : ClientBossBar> filterBars(bars: Iterator<B>) = iterator {
		for(bar in bars) {
			if(bar.uuid != territoryBossbarUuid) yield(bar)
		}
	}

	/**
	 * mojang, wtf is this.
	 */
	private fun getSoundFromPacket(packet: PlaySoundS2CPacket): SoundEvent? {
		val sound = packet.sound.keyOrValue
		return sound.left().map { Registries.SOUND_EVENT[it.value] }.orElseGet { sound.right().getOrNull() }
	}

	@JvmStatic
	fun isDing(packet: PlaySoundS2CPacket): Boolean {
		val sound = getSoundFromPacket(packet) ?: return false
		return sound.id == DING && packet.category == SoundCategory.PLAYERS && packet.pitch == 0.5f
	}
}

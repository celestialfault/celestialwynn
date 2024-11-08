package dev.celestialfault.celestialwynn.util

import dev.celestialfault.celestialwynn.CelestialWynn
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object Scheduler {
	private val tasks = mutableListOf<ScheduledTask>()

	init {
		ClientTickEvents.END_CLIENT_TICK.register { tick() }
	}

	fun schedule(delay: Int, repeat: Boolean = false, task: ScheduledTask.() -> Unit): ScheduledTask {
		require(delay >= 0) { "Delay must be a positive number of ticks" }
		return ScheduledTask(task, delay, repeat).also(tasks::add)
	}

	private fun tick() {
		tasks.filter { it.ticksRemaining-- <= 0 }.forEach { it.run() }
	}

	class ScheduledTask internal constructor(
		private val task: ScheduledTask.() -> Unit,
		private val ticks: Int,
		private val repeat: Boolean = false,
	) {
		private var cancelled = false
		internal var ticksRemaining = ticks

		fun cancel() {
			cancelled = true
		}

		fun run() {
			runCatching { task() }.onFailure { CelestialWynn.LOGGER.error("Failed to run scheduled method", it) }
			if(repeat && !cancelled) {
				ticksRemaining = ticks
			} else {
				tasks.remove(this)
			}
		}
	}
}

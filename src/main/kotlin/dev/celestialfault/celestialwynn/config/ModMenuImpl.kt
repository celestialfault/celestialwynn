package dev.celestialfault.celestialwynn.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi

class ModMenuImpl : ModMenuApi {
	override fun getModConfigScreenFactory() = ConfigScreenFactory(ConfigGUI::getConfigScreen)
}

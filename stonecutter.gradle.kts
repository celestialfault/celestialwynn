plugins {
	id("dev.kikugie.stonecutter")
	id("fabric-loom") version "1.9-SNAPSHOT" apply false
	//id("dev.kikugie.j52j") version "1.0" apply false // Enables asset processing by writing json5 files
	//id("me.modmuss50.mod-publish-plugin") version "0.5.+" apply false // Publishes builds to hosting websites
}
stonecutter active "1.21.4" /* [SC] DO NOT EDIT */

// Builds every version into `build/libs/{mod.version}/`
stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
	group = "project"
	ofTask("buildAndCollect")
}

stonecutter parameters {
	/* https://stonecutter.kikugie.dev/ */
	dependency("fapi", node!!.property("deps.fabric_api").toString())
}

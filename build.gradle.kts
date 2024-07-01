plugins {
    id("fabric-loom")
	kotlin("jvm") version("2.0.0")
}

class ModData {
    val id = property("mod.id").toString()
    val name = property("mod.name").toString()
    val version = property("mod.version").toString()
    val group = property("mod.group").toString()
}

class ModDependencies {
    operator fun get(name: String) = property("deps.$name").toString()
}

val mod = ModData()
val deps = ModDependencies()
val mcVersion = stonecutter.current.version
val mcDep = property("mod.mc_dep").toString()

version = "${mod.version}+$mcVersion"
group = mod.group
base { archivesName.set(mod.id) }

repositories {
	maven("https://maven.isxander.dev/releases")
	maven("https://maven.terraformersmc.com/")
	maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
	maven("https://maven.celestialfault.dev/snapshots")
}

dependencies {
    minecraft("com.mojang:minecraft:${mcVersion}")
    mappings("net.fabricmc:yarn:${mcVersion}+build.${deps["yarn_build"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${deps["fabric_loader"]}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${deps["fabric_api"]}+${mcVersion}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${deps["kotlin"]}")
	vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")

	implementation("me.celestialfault:celestial-config:${deps["celestialconfig"]}")
	include("me.celestialfault:celestial-config:${deps["celestialconfig"]}")

	modImplementation("com.terraformersmc:modmenu:${deps["modmenu"]}")
	// what the fuck
	if(mcVersion == "1.20.2") {
		modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:${deps["yacl"]}+${mcVersion}")
	} else {
		modImplementation("dev.isxander:yet-another-config-lib:${deps["yacl"]}+${mcVersion}-fabric")
	}

	modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:${deps["devauth"]}")
}

loom {
	accessWidenerPath = file("../../src/main/resources/celestialwynn.accesswidener")

    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}

java {
	val java = if (stonecutter.compare(mcVersion, "1.20.6") >= 0) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

kotlin {
	val java = if (stonecutter.compare(mcVersion, "1.20.6") >= 0) 21 else 17
	jvmToolchain(java)
}

tasks.processResources {
    inputs.property("id", mod.id)
    inputs.property("name", mod.name)
    inputs.property("version", mod.version)
    inputs.property("mcdep", mcDep)

    val map = mapOf(
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "mcdep" to mcDep
    )

    filesMatching("fabric.mod.json") { expand(map) }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))
    dependsOn("build")
}

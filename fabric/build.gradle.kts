plugins {
    id("multiloader-platform")
    id("net.fabricmc.fabric-loom")
}

loom {
    runs.named("client") {
        client()

        displayName = "fabric - Client"
        runDirectory = file("../run")
        appendProjectPathToDisplayName = false
        generateRunConfig = true
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    implementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")

    api("net.uku3lig:ukulib-fabric:${BuildConfig.UKULIB_VERSION}")
}

modrinth {
    loaders.add("quilt")
}
plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
    id("io.freefair.lombok") version "9.2.0"
}

version = "${project.property("mod_version")}+mc${project.property("minecraft_version")}"
group = project.property("maven_group") as String

repositories {
    maven {
        url = uri("https://maven.uku3lig.net/releases")
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    api("net.uku3lig:ukulib-fabric:${project.property("ukulib_version")}")
}

base {
    archivesName = project.property("archives_base_name") as String
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}
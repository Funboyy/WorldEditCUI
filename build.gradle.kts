import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import net.labymod.labygradle.common.extension.model.GameVersion
import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannels
import java.nio.file.Files

plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "org.example"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "de.funboyy.addon.worldedit.cui"

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                useOptiFine(true)

                mixin {
                    val versionMappings = file("./game-runner/mappings/").resolve("$versionId.tsrg")

                    if (versionMappings.exists()) {
                        extraMappings.add(versionMappings)
                    }

                    extraMappings.add(file("./game-runner/mappings/shared.tsrg"))
                }
            }
        }
    }

    addonInfo {
        namespace = "worldeditcui"
        displayName = "WorldEdit CUI"
        author = "lahwran, yetanotherx, Mumfrey, TomyLobo, mikroskeem, Funboyy"
        description = "Client-side user interface for WorldEdit"
        minecraftVersion = "1.8.9<1.21.2"
        version = rootProject.version.toString()
        releaseChannel = ReleaseChannels.PRODUCTION
    }
}

fun GameVersion.useOptiFine(enabled: Boolean) {
    if (!enabled) {
        return
    }

    val extra = project.extra
    val versionManifest: OptiVersionManifest
    val gson = GsonBuilder().create()

    if (!extra.has("of-cache")) {

        val file = rootProject.file("./game-runner/versions.json")

        if (!file.exists()) {
            println("Failed to find versions.json")
            return
        }

        versionManifest = Files.newBufferedReader(file.toPath()).use { gson.fromJson(it, OptiVersionManifest::class.java) }
        extra["of-cache"] = gson.toJson(versionManifest)
    } else {
        versionManifest = gson.fromJson(extra.get("of-cache") as String, OptiVersionManifest::class.java)
    }

    versionManifest.findVersion(versionId)?.apply {
        optiFineVersion.set(this.ofVersion.trim())
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}

data class OptiVersionManifest(val versions: List<OptiFineVersion>) {

    fun findVersion(gameVersion: String): OptiFineVersion? {
        return versions.find { it.gameVersion == gameVersion }
    }

}

data class OptiFineVersion(
        @SerializedName("game_version") val gameVersion: String,
        @SerializedName("of_version") val ofVersion: String,
        @SerializedName("preview") val preview: Boolean
)

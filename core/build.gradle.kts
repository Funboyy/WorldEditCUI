import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}

tasks {
    processResources {
        from(rootProject.file("game-runner/mappings/optifine")) {
            into("assets/optifine/mappings")
            include("*.srg")
        }
    }

    jar {
        exclude("net/optifine/**")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}
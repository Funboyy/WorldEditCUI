import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}

tasks {
    jar {
        exclude("net/optifine/**")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}
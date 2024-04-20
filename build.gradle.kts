plugins {
    application
}

repositories {
    mavenCentral()
}

fun DependencyHandlerScope.addLwjglNatives(base: String, implementation: (Dependency) -> Unit, runtimeOnly: (Dependency) -> Unit) {
    implementation(create(base))
    sequenceOf(
        "natives-linux",
        "natives-linux-arm64",
        "natives-macos",
        "natives-macos-arm64",
        "natives-windows",
        "natives-windows-arm64",
    ).map {
        create(base).apply {
            this as ExternalModuleDependency
            artifact {
                classifier = it
            }
        }
    }.forEach(runtimeOnly)
}

dependencies {
    compileOnly(libs.jspecify)
    compileOnly(libs.findbugs.jsr305) {
        because("LWJGL uses it")
    }

    implementation(libs.guava)

    implementation(platform(libs.lwjgl.bom))

    addLwjglNatives("org.lwjgl:lwjgl", ::implementation, ::runtimeOnly)
    addLwjglNatives("org.lwjgl:lwjgl-glfw", ::implementation, ::runtimeOnly)
    addLwjglNatives("org.lwjgl:lwjgl-opengl", ::implementation, ::runtimeOnly)
    addLwjglNatives("org.lwjgl:lwjgl-nanovg", ::implementation, ::runtimeOnly)
    addLwjglNatives("org.lwjgl:lwjgl-jemalloc", ::implementation, ::runtimeOnly)

    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

application {
    mainClass = "net.octyl.triplicate.Triplicate"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

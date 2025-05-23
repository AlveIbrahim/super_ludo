// Project-level build.gradle.kts
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

// DO NOT include any repositories block here
// They should only be in settings.gradle.kts

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
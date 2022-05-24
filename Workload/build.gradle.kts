plugins {
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
    kotlin("jvm")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":Processor"))
    implementation(project(":Annotation"))
    ksp(project(":Processor"))
}

ksp {
    arg("option1", "value1")
    arg("option2", "value2")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}
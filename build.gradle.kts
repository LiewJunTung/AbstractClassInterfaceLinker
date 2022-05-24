plugins {
    kotlin("jvm") version "1.6.10" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.6.10"))
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

group = "eu.syplex"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    kotlin("jvm") version "1.8.20"

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
}

tasks {
    shadowJar {
        fun relocate(pkg: String) = relocate(pkg, "eu.syplex.proxy.dependency.$pkg")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    compileKotlin {
        kotlinOptions { jvmTarget = "17" }
    }

    javadoc { options.encoding = Charsets.UTF_8.name() }

    processResources { filteringCharset = Charsets.UTF_8.name() }
}
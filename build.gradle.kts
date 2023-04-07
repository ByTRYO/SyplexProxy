group = "eu.syplex"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    kotlin("jvm") version "1.8.20"
    kotlin("kapt") version "1.8.20"

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("de.chojo.sadu", "sadu", "1.2.0")
    implementation("net.kyori:adventure-text-minimessage:4.13.0")

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    kapt("com.velocitypowered:velocity-api:3.1.1")
}

tasks {

    shadowJar {
        fun relocate(pkg: String) = relocate(pkg, "eu.syplex.proxy.dependency.$pkg")
        relocate("net.kyori.adventure")

        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }

        archiveFileName.set("Proxy-$version-dev.jar")
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

    assemble {
        dependsOn(shadowJar)
    }
}
group = "eu.syplex"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    kotlin("jvm") version "1.8.20"
    kotlin("kapt") version "1.8.20"

    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.20")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.20")
    implementation("de.chojo.sadu:sadu:1.3.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.1.3")
    implementation("net.kyori:adventure-text-minimessage:4.13.0")
    implementation("eu.cloudnetservice.cloudnet:bridge:4.0.0-RC8")
    implementation("eu.cloudnetservice.cloudnet:driver:4.0.0-RC8")

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    kapt("com.velocitypowered:velocity-api:3.1.1")
}

tasks {

    shadowJar {
        enabled = true
        relocationPrefix = "eu.syplex.proxy"
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
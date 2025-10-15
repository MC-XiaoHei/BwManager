@file:Suppress("VulnerableLibrariesLocal")

plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "cn.xor7.xiaohei"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "andrei1058-repo"
        url = uri("https://repo.andrei1058.dev/releases/")
    }
    maven {
        name = "alessiodp-repo"
        url = uri("https://repo.alessiodp.com/releases/")
    }
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8-R0.1-SNAPSHOT")
    compileOnly("com.alessiodp.parties:parties-api:3.2.16")
    compileOnly("com.andrei1058.bedwars:bedwars-api:25.9") {
        exclude(group = "org.spigotmc")
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.opencsv:opencsv:5.12.0")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

kotlin {
    jvmToolchain(8)
}

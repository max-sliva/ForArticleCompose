import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
//    id("org.jetbrains.compose")
    id("org.jetbrains.compose") version "1.6.0-dev1296"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    mavenCentral()
}
//
//javafx {
//    version = "19"
//}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
//    implementation(compose.desktop.linux_arm64) //for RPi
    implementation("io.github.java-native:jssc:2.9.6")
    implementation("org.json:json:20231013")
    implementation("com.formdev:flatlaf:3.4.1") //look'n'feel
//    implementation("io.github.vinceglb:filekit-core:0.8.2")
//    implementation("io.github.vinceglb:filekit-compose:0.8.2")
    //implementation("org.swinglabs.swingx:swingx-all:1.6.5-1")
//    implementation("io.github.oleksandrbalan:textflow:1.1.2")
//    implementation("dev.romainguy:combo-breaker:0.9.0")
}
//to make exe run in terminal:
//    ./gradlew createDistributable и скопировать папку items и файлы из папки ForExe
// ./gradlew runDistributable
compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
//            modules("java.instrument", "java.net.http", "jdk.jfr", "jdk.jsobject", "jdk.unsupported", "jdk.unsupported.desktop", "jdk.xml.dom")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = "ForArticleCompose"
            packageVersion = "1.0.0"
        }
    }
}

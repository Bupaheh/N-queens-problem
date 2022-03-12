plugins {
    kotlin("jvm") version "1.5.30"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.ow2.sat4j:org.ow2.sat4j.core:2.3.6")
}

application {
    mainClass.set("matlogTask02.MainKt")
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}
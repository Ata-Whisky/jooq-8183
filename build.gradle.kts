plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "com.example.codegenbuh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // jOOQ core (no codegen needed for this reproducer)
    implementation("org.jooq:jooq:3.19.14")

    // PostgreSQL JDBC driver
    implementation("org.postgresql:postgresql:42.7.4")

    // Testcontainers for PostgreSQL
    implementation("org.testcontainers:postgresql:1.20.4")

    // Optional: simple logger so Testcontainers/jOOQ can print logs without extra setup
    runtimeOnly("org.slf4j:slf4j-simple:2.0.16")

    testImplementation(kotlin("test"))
}

application {
    // "MainKt" when using top-level main() in the package below
    mainClass.set("com.example.codegenbuh.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

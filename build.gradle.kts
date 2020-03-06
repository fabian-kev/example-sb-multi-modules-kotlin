import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems.jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
    application
}

application {
    mainClassName = "com/fbiankevin/stack/api/StackCoreApplication.kt"
}

val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
//To prevent the root to build a jar
jar.enabled=false
bootJar.enabled=false

java.sourceCompatibility = JavaVersion.VERSION_1_8


allprojects {
    group = "com.fbiankevin.multimodules"
    version = "1.0"


    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    val jar: Jar by tasks
    val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

    if( name != "api" ){
        apply(plugin = "java-library")
        bootJar.enabled = false
        jar.enabled = true
    }

    dependencies {
        //we will import common module to all modules except itself.
        if(name != "common") {
            implementation(project(":common"))
        }
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
    }
}
//OTHER MODULES CONFIGURATION GOES HERE
project(":api") {
    dependencies {
        implementation(project(":identity"))
    }
}
project(":common") {
    dependencies {
    }
}
project(":identity") {
    dependencies {
    }
}





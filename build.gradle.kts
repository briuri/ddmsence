// Auto-Reload
// ./gradlew -t build

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "buri"
version = "1.0.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("lib")
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.forwarded.header)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.thymeleaf)
    implementation(libs.logback.classic)

    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
}

tasks.register("aws-stage") {
    group = "distribution"
    dependsOn("buildFatJar")
    doLast {
        copy {
            from(layout.projectDirectory.dir("src/main/resources/codedeploy"))
            into(layout.buildDirectory.dir("codedeploy"))
            include("**")
        }
        copy {
            from(layout.buildDirectory.dir("libs"))
            into(layout.buildDirectory.dir("codedeploy"))
            include("sparkour-all.jar")
        }
    }
}

tasks.distTar {
    enabled = false
}
tasks.distZip {
    enabled = false
}
tasks.jar {
    exclude("codedeploy/**")
    exclude("static/css/**")
    exclude("static/docs/**")
    exclude("static/files/**")
    exclude("static/images/**")
    exclude("static/js/**")
}
tasks.processResources {
    exclude("codedeploy/**")
    exclude("static/docs/**")
    exclude("static/files/**")
}
tasks.shadowJar {
    exclude("codedeploy/**")
    exclude("static/css/**")
    exclude("static/docs/**")
    exclude("static/files/**")
    exclude("static/images/**")
    exclude("static/js/**")
}
tasks.shadowDistTar {
    enabled = false
}
tasks.shadowDistZip {
    enabled = false
}
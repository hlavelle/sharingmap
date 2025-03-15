import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.ben-manes.versions") version "0.47.0"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "2.1.20-RC3"
    kotlin("plugin.spring") version "2.1.20-RC3"
    kotlin("plugin.jpa") version "2.1.20-RC3"
    kotlin("plugin.allopen") version "2.1.20-RC3"
}

group = "com"
version = "0.5.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.jsonwebtoken:jjwt:0.2")
    implementation("org.reactivestreams:reactive-streams:1.0.4")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
    implementation("jakarta.activation:jakarta.activation-api:2.1.0")
    implementation("tech.ailef:snap-admin:0.2.1")
    implementation("aws.sdk.kotlin:s3:0.25.0-beta"){
        exclude("com.squareup.okhttp3:okhttp")
    }
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:5.4.0")
    implementation("org.telegram:telegrambots:6.8.0")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.8.0")
}

// tasks.test {
//     useJUnitPlatform()
// }

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
    annotation("com.sharingmap.annotations.Mockable")
}


tasks.withType<JavaExec> {
    environment("AWS_ACCESS_KEY_ID", System.getenv("AWS_ACCESS_KEY_ID"))
    environment("AWS_SECRET_ACCESS_KEY", System.getenv("AWS_SECRET_ACCESS_KEY"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
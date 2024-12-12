plugins {
    java
    //id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.freefair.lombok") version "8.11"
}

group = "ru.job4j"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //implementation("org.springframework.boot:spring-boot-starter-web")
    //implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.1")


    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("org.apache.logging.log4j:log4j-core:2.24.2")
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("io.javalin:javalin:6.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")


    //testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.slf4j:slf4j-reload4j:2.0.16")
    //testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")

    testCompileOnly ("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.36")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
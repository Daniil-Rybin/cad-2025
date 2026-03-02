plugins {
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-jdbc:6.2.2")
    implementation("org.springframework:spring-aop:6.2.2")

    // AspectJ
    implementation("org.aspectj:aspectjweaver:1.9.21")

    // Annotations
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    // H2 database
    implementation("com.h2database:h2:2.2.224")

    // SLF4J + Logback
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Guava (если нужно)
    implementation("com.google.guava:guava:32.1.2-jre")
}

application {
    mainClass.set("ru.bsuedu.cad.lab.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaExec> {
    jvmArgs("-Dfile.encoding=UTF-8")
    standardOutput = System.out
    errorOutput = System.err
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Core
    implementation("org.springframework:spring-context:6.2.2")

    // AOP
    implementation("org.springframework:spring-aop:6.2.2")
    implementation("org.aspectj:aspectjweaver:1.9.21")

    // Аннотации (@PostConstruct)
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    // Можно удалить, если не используешь
    implementation(libs.guava)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.11.1")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.App"
}

tasks.withType<JavaExec> {
    jvmArgs("-Dfile.encoding=UTF-8")
    standardOutput = System.out
    errorOutput = System.err
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

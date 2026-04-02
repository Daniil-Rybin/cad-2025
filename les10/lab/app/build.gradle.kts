plugins {
    java
    war
}

group = "ru.bsuedu.cad.lab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring MVC
    implementation("org.springframework:spring-webmvc:6.1.3")

    // Spring Core
    implementation("org.springframework:spring-context:6.1.3")
    implementation("org.springframework:spring-orm:6.1.3")
    implementation("org.springframework.data:spring-data-jpa:3.2.3")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.4.2.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.4.2.Final")

    // HikariCP
    implementation("com.zaxxer:HikariCP:5.1.0")

    // H2 Database
    implementation("com.h2database:h2:2.2.224")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.5.0")

    // Apache Commons CSV
    implementation("org.apache.commons:commons-csv:1.10.0")

    // Jakarta EE
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")

    // Jackson для JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")

    // Thymeleaf
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")

    // Jakarta Persistence
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    // Для @PostConstruct
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // H2 Database - добавить с правильным scope
    implementation("com.h2database:h2:2.2.224")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.war {
    archiveFileName.set("zoostore.war")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
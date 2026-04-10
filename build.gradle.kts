plugins {
    id("java")
    id("war")
}

group = "church-cms"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    compile time
    compileOnly("jakarta.platform:jakarta.jakartaee-web-api:11.0.0")
//    runtime
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.2")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation(platform("org.testcontainers:testcontainers-bom:1.21.4"))
//    test
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.rest-assured:rest-assured:6.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-inline:+")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    testImplementation("org.testcontainers:junit-jupiter:1.21.4")
    testImplementation("org.testcontainers:mysql:1.21.4")
    testImplementation("com.zaxxer:HikariCP:7.0.2")
    testImplementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.20")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Copy>("deployToTomcat") {
    dependsOn("war")

    from(tasks.named("war"))
    into("/Users/cata/apache-tomcat-11.0.20/webapps")
    rename { "church-cms.war" }
}

tasks.named("build") {
    finalizedBy("deployToTomcat")
}

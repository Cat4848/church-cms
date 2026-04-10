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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-inline:+")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.2")
    compileOnly("jakarta.platform:jakarta.jakartaee-web-api:9.0.0")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
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

//tasks.named("build") {
//    finalizedBy("deployToTomcat")
//}

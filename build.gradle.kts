import org.gradle.wrapper.Download

plugins {
    id("java")
    id("de.undercouch.download") version "5.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.register("downloadNewrelic") {
    doLast {
            val newrelicDir = file("newrelic")
            if (!newrelicDir.exists()) {
                newrelicDir.mkdirs() // Create the directory if it doesn't exist
            }
        ant.invokeMethod("get", mapOf(
            "src" to "https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip",
            "dest" to file("newrelic/newrelic-java.zip")
        ))
    }
}

tasks.register<Copy>("unzipNewrelic") {
    from(zipTree(file("newrelic/newrelic-java.zip")))
    into(rootDir)
}

dependencies {
    // Vulnerable dependencies to trigger Dependabot alerts
    
    // CVE-2016-1000031 - File upload vulnerability
    implementation ("commons-fileupload:commons-fileupload:1.3")
    
    implementation ("org.apache.commons:commons-lang3:3.9")
    implementation ("org.apache.commons:commons-collections4:4.4")

    // CVE-2022-22965 (Spring4Shell), CVE-2022-22970
    implementation ("org.springframework.boot:spring-boot-starter-web:2.2.0.RELEASE")

    // CVE-2021-44228 (Log4Shell), CVE-2021-45046, CVE-2021-45105
    implementation ("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation ("org.apache.logging.log4j:log4j-api:2.14.1")

    // Multiple CVEs in older Gson versions
    implementation ("com.google.code.gson:gson:2.8.5")

    // CVE-2018-10237, CVE-2020-8908
    implementation ("com.google.guava:guava:18.0")

    // CVE-2017-7525, CVE-2017-15095, CVE-2018-7489, CVE-2018-14718-14721
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.6.0")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.6.0")
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.6.0")

    // CVE-2021-37533
    implementation ("commons-net:commons-net:3.3")
    
    // CVE-2018-11776, CVE-2019-0230, CVE-2019-0233 - Struts2 vulnerabilities
    implementation ("org.apache.struts:struts2-core:2.3.20")
    
    // CVE-2018-1000632 - DOM XSS vulnerability
    implementation ("dom4j:dom4j:1.6.1")
    
    // CVE-2019-17566 - Batik XML external entity vulnerability
    implementation ("org.apache.xmlgraphics:batik-transcoder:1.7")

    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")

}

tasks.test {
    useJUnitPlatform()
}
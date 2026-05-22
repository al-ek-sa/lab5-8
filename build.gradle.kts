plugins {
    java
    id("com.diffplug.spotless") version "6.25.0"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.spotless")

    group = "edu.itmo.piikt"
    version = "1.0-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        implementation("ch.qos.logback:logback-classic:1.4.14")
        implementation("org.slf4j:slf4j-api:2.0.9")
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
        implementation("org.codehaus.janino:janino:3.1.10")
        implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")
    }

    spotless {
        java {
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
    tasks.matching { it.name.contains("spotless") }.configureEach {
        enabled = false
    }
}
subprojects {
    tasks.matching { it.name == "spotlessCheck" }.configureEach {
        enabled = false
    }
    tasks.matching { it.name == "spotlessApply" }.configureEach {
        enabled = false
    }
}

subprojects {
    tasks.withType<Javadoc>().configureEach {
        options.encoding = "UTF-8"
        (options as StandardJavadocDocletOptions).apply {
            showFromPrivate()
            author(true)
            version(true)
            addStringOption("sourcepath", 
                "${project(":client").projectDir}/src/main/java:" +
                "${project(":server").projectDir}/src/main/java:" +
                "${project(":common").projectDir}/src/main/java"
            )
        }
    }
}

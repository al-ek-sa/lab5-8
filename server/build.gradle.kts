plugins {
    application
}

dependencies {
    implementation(project(":common"))
    implementation("com.sun.mail:jakarta.mail:2.0.2")
    implementation(platform("com.google.cloud:libraries-bom:26.72.0"))
    implementation("com.google.cloud:google-cloud-firestore")
    implementation("org.postgresql:postgresql:42.7.11")
}

application {
    mainClass.set("edu.itmo.piikt.server.MainServer")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "edu.itmo.piikt.server.MainServer"
    }
}

tasks.register<Jar>("shadeJar") {
    archiveClassifier.set("with-dependencies")
    dependsOn(tasks.compileJava, tasks.processResources)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    
    from(sourceSets.main.get().output)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

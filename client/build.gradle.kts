plugins {
    application
}

dependencies {
    implementation(project(":common"))
}

application {
    mainClass.set("edu.itmo.piikt.client.MainClient")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "edu.itmo.piikt.client.MainClient"
    }
}

tasks.register<Jar>("fatJar") {
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
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

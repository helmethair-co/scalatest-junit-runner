plugins {
    java
    scala
    `maven-publish`
    jacoco
    id("com.adarshr.test-logger") version "2.0.0"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    val junitPlatformVersion = "1.6.0"
    val junitJupiterVersion = "5.6.0"

    compileOnly("org.junit.platform:junit-platform-engine:$junitPlatformVersion")
    compileOnly("org.scalatest:scalatest_2.11:3.0.7")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit.platform:junit-platform-engine:1.6.0")
    testImplementation("org.scalatest:scalatest_2.11:3.1.1")
    testImplementation("org.scala-lang:scala-library:2.11.12")
    testImplementation("org.mockito:mockito-core:2.7.22")
}

sourceSets {
    test {
        withConvention(ScalaSourceSet::class) {
            scala {
                setSrcDirs(listOf("src/test/scala", "src/test/java"))
            }
        }
        java {
            setSrcDirs(emptyList<String>())
        }
    }
}

tasks {
    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
            excludeEngines("scalatest")
            exclude("tests")
        }
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        reports {
            xml.isEnabled = true
            html.isEnabled = true
            csv.isEnabled = false
        }
    }
    java {
        compileJava {
            options.compilerArgs.add("-Xlint:unchecked")
        }
    }
}

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.value("sources")
    from(sourceSets.main.get().java)
}

val stubJavaDocJar by tasks.registering(Jar::class) {
    archiveClassifier.value("javadoc")
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            groupId = "co.helmethair"
            artifactId = "scalatest-junit-runner"
            from(components["java"])
            artifact(sourceJar.get())
            artifact(stubJavaDocJar.get())

            pom {
                name.set("Junit5 Scalatest Runner")
                description.set("Scalatest runner for JUnit 5")
            }
        }
    }
}

project.extra["artifacts"] = arrayOf("maven")

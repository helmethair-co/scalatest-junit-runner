plugins {
    java
    scala
    `maven-publish`
    signing
    jacoco
    id("com.adarshr.test-logger") version "2.0.0"
    id("io.wusa.semver-git-plugin") version "2.0.2"
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
    repositories {
        maven {
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
    }
    publications {
        register("maven", MavenPublication::class) {
            groupId = "co.helmethair"
            artifactId = "scalatest-junit-runner"
            version = semver.info.toString()
            from(components["java"])
            artifact(sourceJar.get())
            artifact(stubJavaDocJar.get())

            pom {
                name.set("$groupId:$artifactId")
                description.set("JUnit 5 Scalatest runner")
                url.set("https://github.com/helmethair-co/scalatest-junit-runner")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("giurim")
                        name.set("Gyorgy Mora")
                        email.set("gyorgy.mora@helmethair.co")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/helmethair-co/scalatest-junit-runner.git")
                    developerConnection.set("scm:git:ssh://github.com:helmethair-co/scalatest-junit-runner.git")
                    url.set("https://github.com/helmethair-co/scalatest-junit-runner/tree/master")
                }
            }
        }
    }
}

project.extra["artifacts"] = arrayOf("maven")
project.version = semver.info
signing {
    sign(publishing.publications["maven"])
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
}

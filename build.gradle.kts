import java.util.Date as JDate

plugins {
    java
    scala
    `maven-publish`
    signing
    jacoco
    id("com.adarshr.test-logger") version "3.0.0"
    id("io.wusa.semver-git-plugin") version "2.3.7"
    id("com.jfrog.bintray") version "1.8.5"
}

semver {
    snapshotSuffix = "SNAPSHOT"
    dirtyMarker = "dirty"
    initialVersion = "0.1.4"
    tagPrefix = "v"
    branches {
        branch {
            regex = "main"
            incrementer = "NO_VERSION_INCREMENTER"
            formatter = Transformer { "${semver.info.version.major}.${semver.info.version.minor}.${semver.info.version.patch}_build.${semver.info.count}.sha.${semver.info.shortCommit}" }
        }
    }
}

val releaseVersionInfo = semver.info
val releaseVersion = releaseVersionInfo.toString()
val releaseDescription = "JUnit 5 Scalatest runner"
val releaseDate = JDate().toString()
val releaseArtifactName = "scalatest-junit-runner"
val releaseRepoName = "helmethair-co/$releaseArtifactName"
val releaseGitPath = "github.com/$releaseRepoName"
val releaseUrl = "https://$releaseGitPath"
val releaseLabels = arrayOf("scala", "scalatest", "junit", "junit5", "test", "testing", "gradle", "maven")
val releaseLicense = "MIT"
val releaseVcsUrl = "$releaseUrl.git"
val releaseIssuetrackerUrl = "$releaseUrl/issues"
val releaseGroupId = "co.helmethair"

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val junitPlatformVersion = "1.6.0"
    val junitJupiterVersion = "5.7.1"

    val testScalaLibraryVersion = "2.13"
    val testScalaVersion = "$testScalaLibraryVersion.4"

    compileOnly("org.junit.platform:junit-platform-engine:$junitPlatformVersion")
    compileOnly("org.scalatest:scalatest_2.11:3.3.0-SNAP3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit.platform:junit-platform-engine:1.6.0")
    testImplementation("org.scalatest:scalatest_$testScalaLibraryVersion:3.3.0-SNAP3")
    testImplementation("org.scala-lang:scala-library:$testScalaVersion")
    testImplementation("org.mockito:mockito-core:3.10.0")
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
            url = uri(if (releaseVersion.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
        jcenter{
            val releasesRepoUrl = "https://api.bintray.com/maven/helmethair/co.helmethair/co.helmethair:scalatest-junit-runner/"
            val snapshotsRepoUrl = "https://oss.jfrog.org/artifactory/oss-snapshot-local/"
            url = uri(if (releaseVersion.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = System.getenv("JCENTER_USER")
                password = System.getenv("JCENTER_PASSWORD")
            }
        }
    }
    publications {
        register("maven", MavenPublication::class) {
            groupId = releaseGroupId
            artifactId = releaseArtifactName
            version = releaseVersion
            from(components["java"])
            artifact(sourceJar.get())
            artifact(stubJavaDocJar.get())

            pom {
                name.set("$groupId:$artifactId")
                description.set(releaseDescription)
                url.set(releaseUrl)
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
                    connection.set("scm:git:git://$releaseGitPath.git")
                    developerConnection.set("scm:git:ssh://github.com:helmethair-co/scalatest-junit-runner.git")
                    url.set("$releaseUrl/tree/main")
                }
            }
        }
    }

    project.extra["artifacts"] = arrayOf("maven")
    project.version = releaseVersion
    signing {
        sign(publishing.publications["maven"])
        val signingKeyId: String? by project
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    }
}
bintray {
    user = System.getenv("JCENTER_USER")
    key = System.getenv("JCENTER_PASSWORD")
    publish = false
    override = true
    setPublications("maven")
    pkg.apply {
        repo = "co.helmethair"
        name = "$releaseGroupId:$releaseArtifactName"
        userOrg = "helmethair"
        setLicenses(releaseLicense)
        vcsUrl = releaseVcsUrl
        githubRepo = githubRepo
        description = releaseDescription
        setLabels(*releaseLabels)
        desc = description
        websiteUrl = releaseUrl
        issueTrackerUrl = releaseIssuetrackerUrl
        githubRepo = releaseRepoName

        version.apply {
            name = releaseVersion
            desc = releaseDescription
            released = releaseDate
            vcsTag = releaseVersion
        }
    }
}

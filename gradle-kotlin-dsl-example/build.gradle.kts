plugins {
    java
    scala
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-library:2.12.17")

    testImplementation("org.scalatest:scalatest_2.12:3.2.13")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.9.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.1")
    testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.2.0")
}

tasks {
    test{
        useJUnitPlatform {
            includeEngines("scalatest")
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
}

# ScalaTest Junit 5 runner

[![Build status](https://github.com/helmethair-co/scalatest-junit-runner/workflows/Java%20CI/badge.svg)](https://github.com/helmethair-co/scalatest-junit-runner/actions?query=workflow%3A%22Java+CI%22+branch%3Amaster+event%3Apush) [![codecov](https://codecov.io/gh/helmethair-co/scalatest-junit-runner/branch/master/graph/badge.svg)](https://codecov.io/gh/helmethair-co/scalatest-junit-runner)

[JUnit 5](https://junit.org/junit5/docs/current/user-guide/) runner library for [ScalaTest](http://www.scalatest.org/) tests. It can be used to fully [integrate Scalatest into Gradle](https://www.baeldung.com/junit-5-gradle) (version >= 4.5).
This approach is different than using the [gradle-scalatest](https://plugins.gradle.org/plugin/com.github.maiflai.scalatest) Gradle plugin.
## Getting Started

## Features
* Running ScalaTest test on the Junit 5 platform
* Fully integrate ScalaTest into gradle
* Report individual test runs
* Report errors to Junit 5 with stack-traces
* Support ScalaTest tags
* Optional early stopping after the first test fail

## How to use

### Gradle >= 4.5

```Groovy
dependencies {
  testRuntime 'co.helmethair:scalatest-junit-runner:<latest-version>'
}

test {
  useJUnitPlatform {
    includeEngines 'scalatest'
  }
}
```

### JUnit console

Add the library to the classpath and run your test


## Built With

* [ScalaTest](http://www.scalatest.org)
* [Kotlin](http://kotlinlang.org/) - Kotlin DSL for Gralde
* [Gradle](http://gradle.org/) - Build tool
* [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger) 
* [semver-git-plugin](https://github.com/ilovemilk/semver-git-plugin)


## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

We use [semantic versioning](http://semver.org/).

This project is licensed under the MIT License - see the [LICENSE](LICENSE) for details

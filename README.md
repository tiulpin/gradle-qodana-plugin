[![official JetBrains project](https://jb.gg/badges/official.svg)][jb:confluence-on-gh]
[![Twitter Follow](https://img.shields.io/twitter/follow/QodanaEvolves?style=flat)][jb:twitter]
[![Build](https://github.com/JetBrains/gradle-grammar-kit-plugin/workflows/Build/badge.svg)][gh:build]
[![Slack](https://img.shields.io/badge/Slack-%23qodana-blue)][jb:slack]

# Gradle Qodana Plugin

Gradle interface to run code inspections from Intellij IDEA.

## Docker Image with Qodana tool

Docker Hub: https://hub.docker.com/r/jetbrains/qodana

## Gradle Qodana Tasks

- `runInspections` starts Qodana inspections in docker container
- `stopInspections` stops docker container with Qodana
- `cleanInspections` cleanups Qodana output directory

## Gradle Qodana Configuration

> **Note:** Make sure you have `docker` already installed and available in your environment. 

Apply Gradle plugin `org.jetbrains.qodana` in Gradle configuration file:

- Groovy – `build.gradle`

  ```groovy
  plugins {
      id "org.jetbrains.qodana" version "0.1.5"
  }
  ```
  
- Kotlin DSL – `build.gradle.kts`

  ```kotlin
  plugins {
      id("org.jetbrains.qodana") version "0.1.5"
  }
  ```
  
Elements to configure plugin available in `qodana { }` top level configuration group:

- `projectPath` path to project on local machine
- `resultsPath` path to directory where should be 
- `profilePath` path to Qodana profile file
- `disabledPluginsPath` path to file that describes disabled IDEA plugins
- `jvmParameters` JVM parameters to start IDEA JVM


- `bind(local port, docker port)` binds port between local machine and docker container
- `mount(local path, docker path)` mounts directory between local machine and docker container
- `env(name, value)` defines environment variable


- `dockerImageName` name of docker image with Qodana tool
- `dockerContainerName` docker container name to identify qodana container
- `dockerPortBindings` bounded docker and local ports
- `dockerVolumeBindings` mounted docker and local directories
- `dockerEnvParameters` defined environment variables
- `dockerArguments` custom docker arguments to start docker container with Qodana tool

### Simple example

Add this to your Gradle configuration file:

- Groovy – `build.gradle`

  ```groovy
  plugins {
      // applies Gradle Qodana plugin to use it in project
      id "org.jetbrains.qodana" version "0.1.5"
  }
  
  qodana {
      // by default qodana.recommended will be used
      profilePath = "./someExternallyStoredProfile.xml"

      // by default result path is $projectPath/build/results
      resultsPath = "some/output/path"
  }
  ```

- Kotlin – `build.gradle.kts`

  ```kotlin
  plugins {
      // applies Gradle Qodana plugin to use it in project
      id("org.jetbrains.qodana") version "0.1.5"
  }
  
  qodana {
      // by default qodana.recommended will be used
      profilePath.set("./someExternallyStoredProfile.xml")

      // by default result path is $projectPath/build/results
      resultsPath.set("some/output/path")
  }
  ```

> **Note:** Docker requires at least 4GB of memory. Set it in Docker `Preferences > Resources > Memory` section.

Now you can run inspections with `runInspections` Gradle task:

```bash
gradle runInspections 
// or
./gradlew runInspections
```

Full guide for options and configuration parameters could be found on [qodana docs page](https://www.jetbrains.com/help/qodana/qodana-intellij-docker-readme.html#Using+an+existing+profile). 

## Build Locally

### Build 

Execute Gradle task `publishToMavenLocal` to build Gradle Qodana Plugin and publish it into local Maven repository.
By default, plugin will be published into `~/.mvn/org/jetbrins/qodana/` directory.

### Apply

Add Maven local repository into available repositories in your Gradle project.
For this you need to add following lines at the beginning of `settings.gradle[.kts]` file:

```groovy
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
```

Apply Gradle Qodana Plugin with snapshot version in Gradle configuration file and mount the Maven Local directory:

- Groovy – `build.gradle`

  ```groovy
  plugins {
      id "org.jetbrains.qodana" version "0.1.0-SNAPSHOT"
  }
  
  qodana {
      mount("/Users/me/.m2", "/root/.m2")
  }
  ```

- Kotlin DSL – `build.gradle.kts`

  ```kotlin
  plugins {
      id("org.jetbrains.qodana") version "0.1.0-SNAPSHOT"
  }

  qodana {
      mount("/Users/me/.m2", "/root/.m2")
  }
  ```

[gh:build]: https://github.com/JetBrains/gradle-qodana-plugin/actions?query=workflow%3ABuild
[jb:confluence-on-gh]: https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub
[jb:slack]: https://qodana.slack.com
[jb:twitter]: https://twitter.com/QodanaEvolves
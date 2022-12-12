# gradle-fortify-plugin
A Gradle plugin for building and publishing of Fortify artifacts for a static security analysis.

This is a rewrite of https://github.com/sw-samuraj/gradle-fortify-plugin

## Applying the plugin ##

### Gradle 2.1+ ###

```groovy
plugins {
    id "com.github.fortify-gradle" version "0.1.0-SNAPSHOT"
}
```
### All Gradle versions (or local repository) ##

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "com.github.bschramke:gradle-fortify-plugin:0.1.0-SNAPSHOT"
    }
}

apply plugin: "com.github.fortify-gradle"
```

## Using the plugin ##

### Prerequisities ###

**Java plugin**

The plugin is meant for analysis of Java source code. Therefore expects an application of the *Java plugin* and
by default is processing Java *source sets*, excluding *test* source code.

Moreover, it re-uses `sourceCompatibility` property inherited from the *Java plugin*.

**sourceanalyzer**

The plugin requires that you have a local installation of the `sourceanalyzer` tool and that
this command is available on `$PATH`.

### Tasks ###

**fortify**

`fortify` task runs the following commands:

```shell
sourceanalyzer -b <Fortify build ID> -clean
sourceanalyzer -b <Fortify build ID> -source <source compatibility> -cp <project compile classpath> src/**/*.java -exclude src/test/**/*.java
sourceanalyzer -b <Fortify build ID> -build-label <project version> -export-build-session build/fortify/<Fortify build ID>@<project version>.mbs
sourceanalyzer -b <Fortify build ID> -scan -f build/fortify/<project-name>-<project-version>.fpr
```

Result of this task will be a `<project-name>-<project-version>.fpr` file, located in the `build/fortify` directory.
The `<project-name>-<project-version>.fpr` file can be then uploaded to *Fortify Security Center* via `scp`, or *Jenkins*.

If you want to see exact `sourceanalyzer` commands, you can run *Gradle* with `-i`, or  `--info` switch:

```shell
$ gradle clean fortify --info
```

### Config options ###

**fortifyBuildID**

There must be a `fortify` part in the `build.gradle` file which defines a mandatory parameter `fortifyBuildID`.

```groovy
fortify {
    fortifyBuildID = 'my-fort-proj'
}
```

**sourceCompatibility**

The `sourceCompatibility` property is inherited from the *Java plugin*. It can be explicitly set via standard *Java
plugin* configuration:

```groovy
plugins {
    id 'java'
}

sourceCompatibility = 1.8
```

## Example ##

Usage of the plugin and example project can be found in the `example` directory.

## License ##

The **gradle-fortify-plugin** is published under [BSD 3-Clause](http://opensource.org/licenses/BSD-3-Clause) license.

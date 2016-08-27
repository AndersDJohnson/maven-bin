# maven-bin

![Maven Central](https://img.shields.io/maven-central/v/me.andrz.maven/maven-bin.svg)
[![Travis](https://img.shields.io/travis/AndersDJohnson/maven-bin.svg)](https://travis-ci.org/AndersDJohnson/maven-bin)
[![Appveyor](https://ci.appveyor.com/api/projects/status/nppnd6tgyv4o2osc?svg=true)](https://ci.appveyor.com/project/AndersDJohnson/maven-bin)
[![Codecov](https://img.shields.io/codecov/c/github/AndersDJohnson/maven-bin.svg)](http://codecov.io/github/AndersDJohnson/maven-bin)

A CLI to [install][] & [run][execute] executable libraries from [Maven] repositories,
with automagic classpathing of dependencies,
and [aliases][] on your global path.
Sort of like [npm]'s `install --global`.


## Use

### Executing

Executing a library will be sure it and all its dependencies are downloaded, then run a class in the JAR, defaulting to its main class.

```sh
mvbn org.apache.ant:ant -help
```

Or the long way:

```sh
mvn me.andrz.maven:maven-bin-maven-plugin:exec "-Dartifact=org.apache.ant:ant" "-Darguments=-help"
```

#### Non-Main Classes

Non-main classes can be specified with an `@...` suffix on the artifact coordinates, e.g.:

```sh
mvbn org.antlr:antlr4@org.antlr.v4.gui.TestRig
```

### Installing

Installing a library will first download as above, then create an executable script file
as a shortcut to execute it.

```sh
mvbn -i org.apache.ant:ant
```

Or the long way:

```sh
mvn me.andrz.maven:maven-bin-maven-plugin:install "-Dartifact=org.apache.ant:ant"
```

Now, you'll have a command for it, e.g.:

```sh
ant--org.apache.ant--1.9.6
```

But you'll probably want to specify [aliases][].

#### Aliases

```sh
mvbn -i -a ant org.apache.ant:ant
```

Or the long way:

```sh
mvn me.andrz.maven:maven-bin-maven-plugin:install "-Dartifact=org.apache.ant:ant" "-Dalias=ant"
```

Now, you'll have a command for it:

```sh
ant
```

### Classpaths

Get the classpath for an artifact:

```sh
mvbn -c org.antlr:antlr4
```

For example, to use when compiling:

```sh
javac -cp "$(mvbn -c org.antlr:antlr4)" MyGrammar*.java
```


## Install

### General

To install maven-bin:

#### 1. Install Core

```sh
mvn me.andrz.maven:maven-bin-maven-plugin:installSelf
```

#### 2. Add to PATH

Add `~/.mvbn/bin` to your system executable `PATH`.

##### Unix (macOS, Linux, etc.)

In your shell startup script (e.g. `.profile`, `.bashrc`, `.bash_profile`, `.zshrc`), add something like:

```sh
MAVEN_BIN="${HOME}/.mvbn/bin"
if [ -d "${MAVEN_BIN}" ]; then
  export PATH="${MAVEN_BIN}:$PATH"
fi
```

##### Windows

In your [PowerShell profile](http://www.howtogeek.com/50236/customizing-your-powershell-profile/), add something like:

```ps
$env:Path = $env:Path + ";${env:UserProfile}\.mvbn\bin;"
```

### Development

```sh
git clone https://github.com/AndersDJohnson/maven-bin.git
cd maven-bin
mvn install
```

## Features

* With a single command, install JARs and execute their main classes.
* Uses `settings.xml` so you can configure 3rd-party repos, e.g. [Bintray].

### Wishlist
* [x] Aliasing (default group ID and versions)
  * [x] Add aliased command executables to global path
* [ ] Auto-complete CLI
  * Maybe via a generic Groovy CliBuilder completion integration for Bash, zsh, PowerShell, etc.?
* [ ] Search repos
  * Base on Maven Central Search API? http://search.maven.org/#api


## Dev

Set log level for slf4j simple:

Just this package:

```sh
org.slf4j.simpleLogger.log.me.andrz.maven.bin=debug
```

Or default (all packages):

```sh
-Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```


## Research

```sh
mvn dependency:copy "-Dartifact=org.apache.ant:ant:RELEASE" "-DoutputDirectory=./bin"
```

```sh
mvn dependency:get "-Dartifact=org.apache.ant:ant:RELEASE"
```

```sh
mvn -f "C:\Users\Anders\.m2\repository\com\google\guava\guava\18.0\guava-18.0.pom" org.apache.maven.plugins:maven-dependency-plugin:2.10:build-classpath
```

```sh
mvn org.apache.maven.plugins:maven-dependency-plugin:2.10:build-classpath "-Dmdep.outputFile=foo"
```

```sh
mvn -f .\mvn help:effective-pom
```

* https://github.com/takari/maven-wrapper
* https://github.com/TimMoore/mojo-executor
* https://maven.apache.org/plugin-testing/
* https://maven.apache.org/plugin-developers/plugin-testing.html
* https://mizdebsk.fedorapeople.org/xmvn/
* https://stackoverflow.com/questions/11799923/programmatically-resolving-maven-dependencies-outside-of-a-plugin-get-reposito
* https://wiki.eclipse.org/Aether/Creating_a_Repository_System_Session (settings.xml)
* http://git.eclipse.org/c/aether/aether-ant.git/tree/src/main/java/org/eclipse/aether/internal/ant/AntRepoSys.java (settings.xml)
* https://stackoverflow.com/questions/10536221/fetching-maven-artifacts-programmatically
* http://aether.jcabi.com/
* https://stackoverflow.com/questions/27818659/loading-mavens-settings-xml-for-jcabi-aether-to-use
* https://github.com/mguymon/naether
* http://git.eclipse.org/c/aether/aether-demo.git/tree/aether-demo-snippets/src/main/java/org/eclipse/aether/examples
* https://issues.jboss.org/browse/FORGE-2184 (A required class was missing while executing: org/eclipse/aether/spi/connector/transport/TransporterFactory)
* https://maven.apache.org/ref/3.0.3/maven-core/apidocs/org/apache/maven/repository/RepositorySystem.html
* https://maven.apache.org/ref/3.2.5/maven-artifact/apidocs/org/apache/maven/artifact/repository/ArtifactRepository.html
* https://maven.apache.org/ref/3.2.5/maven-artifact/apidocs/org/apache/maven/artifact/repository/ArtifactRepository.html#getMirroredRepositories()
* https://maven.apache.org/ref/3.2.5/maven-artifact/apidocs/org/apache/maven/artifact/repository/ArtifactRepository.html#find(org.apache.maven.artifact.Artifact)
* https://maven.apache.org/ref/3.2.5/maven-artifact/apidocs/org/apache/maven/artifact/Artifact.html
* https://stackoverflow.com/questions/4381460/get-mavenproject-from-just-the-pom-xml-pom-parser
* http://sonatype.github.io/sonatype-aether/apidocs/org/sonatype/aether/repository/LocalRepository.html
* https://rolfje.wordpress.com/2013/07/18/non-java-binary-dependencies-in-maven/
* http://mojo.codehaus.org/exec-maven-plugin/index.html
* http://www.petrikainulainen.net/programming/tips-and-tricks/creating-a-runnable-binary-distribution-with-maven-assembly-plugin/
* https://stackoverflow.com/questions/2022032/building-a-runnable-jar-with-maven-2
* https://stackoverflow.com/questions/574594/how-can-i-create-an-executable-jar-with-dependencies-using-maven
* http://mojo.codehaus.org/exec-maven-plugin/
* http://maven.40175.n5.nabble.com/Can-mvn-install-packages-globally-e-g-command-line-tools-like-nutch-td5767828.html
* https://maven.apache.org/guides/mini/guide-using-one-source-directory.html
* http://blog.sonatype.com/2010/01/how-to-create-two-jars-from-one-project-and-why-you-shouldnt/
* https://github.com/cstamas/maven-indexer-examples
* https://stackoverflow.com/questions/2063378/how-can-i-get-the-list-of-artifacts-from-my-maven-repository
* https://github.com/kumarshantanu/lein-localrepo
* https://maven.apache.org/guides/plugin/guide-java-plugin-development.html
* http://books.sonatype.com/mvnref-book/reference/writing-plugins-sect-custom-plugin.html
* http://docs.codehaus.org/display/MAVENUSER/Mojo+Developer+Cookbook
* http://blog.sonatype.com/2011/01/how-to-use-aether-in-maven-plugins/

[bintray]: https://bintray.com/
[maven]: https://maven.apache.org/
[npm]: https://www.npmjs.com/
[install]: #installing
[execute]: #executing
[aliases]: #aliases

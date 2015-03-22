# maven-executable


## Usage

Ideally:

```
mvnx org.apache.ant:ant
```

Really:

```
mvn me.andrz:maven-executable:executable "-Dartifact=org.apache.ant:ant"
```

## Features

* Auto-install
* Search?


## Research

```
mvn dependency:copy "-Dartifact=org.apache.ant:ant:RELEASE" "-DoutputDirectory=./bin"
```

```
mvn dependency:get "-Dartifact=org.apache.ant:ant:RELEASE"
```

```
mvn -f "C:\Users\Anders\.m2\repository\com\google\guava\guava\18.0\guava-18.0.pom" org.apache.maven.plugins:maven-dependency-plugin:2.10:build-classpath
```

```
mvn org.apache.maven.plugins:maven-dependency-plugin:2.10:build-classpath "-Dmdep.outputFile=foo"
```

```
mvn -f .\mvn help:effective-pom
```


* https://mizdebsk.fedorapeople.org/xmvn/
* https://stackoverflow.com/questions/10536221/fetching-maven-artifacts-programmatically
* http://aether.jcabi.com/
* https://stackoverflow.com/questions/27818659/loading-mavens-settings-xml-for-jcabi-aether-to-use
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

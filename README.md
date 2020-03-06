This is a port of [pfdtk](https://www.pdflabs.com/tools/pdftk-server/)
into Java. The current goal is to make a translation as faithful as it
is reasonable and to fix any issues present in the original
(correctness takes precedence over compatibility, see the [differences](#known-differences-with-pdftk)),
leaving possible improvements and refactoring for
later. So far all code has been manually translated and it passes the
test suite of [php-pdftk](https://github.com/mikehaertl/php-pdftk),
but a lot more testing is needed. Due to the differences between C++
and Java, it is likely that a few bugs have sneaked in with respect to
the original; any help in catching them will be appreciated.

## Installation on Debian/Ubuntu

An official `pdftk-java` package is available for Debian >= 10 and
Ubuntu >= 18.10. There is also a package in a third-party repository
intended for *earlier* OS releases that can be installed as follows:

```
sudo add-apt-repository ppa:malteworld/ppa
sudo apt update
sudo apt install pdftk
```

## Installation on macOS

On macOS `pdftk-java` is available as a [homebrew](https://brew.sh) formula. It can be installed with the following command:

```
brew install pdftk-java
```

## Dependencies

 - jdk >= 1.7
 - commons-lang3
 - bcprov
 - gradle >= 4.10.3 or ant (build time)
 - ivy (optionally for ant, for resolving dependencies at build time)

## Building and running with Gradle
If you have gradle installed, you can produce a standard jar, which
requires a Java Runtime Environment plus additional libraries, a
standalone jar, which only requires a Java Runtime Environment, or a
standalone native binary, which does not require any runtime
dependencies. In order to build the binary, you need to have
[GraalVM](https://www.graalvm.org) with the [Native Image
Plugin](https://www.graalvm.org/docs/reference-manual/native-image/)
installed.

The build configuration is relatively simple so it should work with most
versions of gradle since 5.0 (tested 5.0 and 6.0.1) but if you have problems try
installing gradle wrapper at a particular version and then running the wrapper:
```
gradle wrapper --gradle-version 6.0.1
```

### Standard jar

To build a jar, simply run: 

```
gradle jar
```

and refer to the [ant instructions](#building-and-running-with-and) for running it.

### Standalone jar

To build the standalone jar, simply run: 

```
gradle shadowJar
```
Alternatively, a [pre-built jar](https://gitlab.com/pdftk-java/pdftk/-/jobs/artifacts/master/file/build/libs/pdftk-all.jar?job=gradle) is also available.

This can then be run with just java installed like:
```
java -jar build/libs/pdftk-all.jar
```

### Standalone binary (native image)
To build the standalone binary, simply run:

```
gradle nativeImage
```

This can then be run like:
```
./build/native-image/pdftk
```

## Building and running with ant

With ivy:
```
$ ant
```

Without ivy: install bcprov and commons-lang3, make a directory `lib`
and link `bcprov.jar` and `commons-lang3.jar` into it. Then:
```
$ ant jar
```

To run:
```
$ java -cp build/jar/pdftk.jar:lib/bcprov.jar:lib/commons-lang3.jar com.gitlab.pdftk_java.pdftk
```

## Known differences with pdftk

The following differences with respect to the original version of
pdftk are intended. Issue reports about other differences are welcome.

- Does not ask for owner password if not needed.
- Does not report some structure-only form fields.
- Reports some missing values in multi-valued form fields.

## Source organization

`java/com/` contains the translated Java sources. Currently these are
a few large files, but they should be split into one class per file.

`java/pdftk/` contains the sources for an old, yet-to-be-determined
version of the iText library. They were modified in the original C++
sources, hence it is not obvious whether they can be replaced by a
more recent vanilla version.

This is a port of [pdftk](https://www.pdflabs.com/tools/pdftk-server/)
into Java. The current goal is to make a translation as faithful as it
is reasonable and to fix any issues present in the original
(correctness takes precedence over compatibility, see the [differences](#known-differences-with-pdftk)),
leaving possible improvements and refactoring for
later. So far all code has been manually translated and it passes the
test suite of [php-pdftk](https://github.com/mikehaertl/php-pdftk),
but a lot more testing is needed. Due to the differences between C++
and Java, it is likely that a few bugs have sneaked in with respect to
the original; any help in catching them will be appreciated.

## Installation

### Using a package manager.

There are pdftk-java packages available in a few repositories, including

 - Arch
 - Debian >=10 / Ubuntu >= 18.10
 - Gentoo (`pdftk`)
 - Homebrew (macOS)
 - MacPorts (macOS)

Please refer to the documentation of your package manager for instructions.

### Pre-built binaries

The recommended way to install pdftk-java is through a package
manager, but if that is not an option there are pre-built binaries
available:

 - [Standalone jar](https://gitlab.com/pdftk-java/pdftk/-/jobs/artifacts/v3.1.1/file/build/libs/pdftk-all.jar?job=gradle), including dependencies. Requires a JRE at runtime.
 - [Native Image](https://gitlab.com/pdftk-java/pdftk/-/jobs/artifacts/v3.1.1/file/build/native-image/pdftk?job=nativeimage) for x86_64 GNU/Linux systems. Does not require any runtime dependencies.

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
dependencies.

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

and refer to the [ant instructions](#building-and-running-with-ant) for running it.

### Standalone jar

To build a standalone jar, simply run: 

```
gradle shadowJar
```

This can then be run with just java installed like:
```
java -jar build/libs/pdftk-all.jar
```

### Standalone binary (native image)

Building a standalone binary requires
[GraalVM](https://www.graalvm.org), which replaces the standard JDK,
with the [Native Image
Plugin](https://www.graalvm.org/docs/reference-manual/native-image/)
installed. To build a standalone binary, simply run:

```
export JAVA_HOME=/path/to/graalvm
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
- Does not escape form fields if UTF-8 output is selected.

## Source organization

`java/com/` contains the translated Java sources. Currently these are
a few large files, but they should be split into one class per file.

`java/pdftk/` contains the sources for an old, yet-to-be-determined
version of the iText library. They were modified in the original C++
sources, hence it is not obvious whether they can be replaced by a
more recent vanilla version.

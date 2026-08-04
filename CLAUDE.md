# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

This repository is currently the unmodified output of `maven-archetype-quickstart` — a placeholder "Hello World" Java project (`com.pavilion.App`) with a trivial JUnit test. There is no application architecture yet; the eventual backend for Project Pavilion has not been built out beyond this scaffold.

## Commands

Build the project:
```
mvn compile
```

Run all tests:
```
mvn test
```

Run a single test class:
```
mvn test -Dtest=AppTest
```

Package into a jar:
```
mvn package
```

Run the app directly (after compiling):
```
mvn exec:java -Dexec.mainClass="com.pavilion.App"
```

## Notes for future work

- `pom.xml` targets Java 1.7 (`maven.compiler.source`/`target`) — this is very outdated and will likely need bumping before adding real dependencies (e.g. a web framework, persistence layer).
- The only declared dependency is JUnit 4.11 (test scope). No framework (Spring, etc.), no persistence, no HTTP layer is set up yet.
- Base package is `com.pavilion`.

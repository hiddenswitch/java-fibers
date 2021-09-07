# quasar-core fork

This is a fork of Parallel Universe's Quasar fibers library for Java, updated to work only with Java 11+ without modules.

### What's new

 - Support for Java 12-16
 - Non-AOT support for GraalVM
 - Micrometer metrics
 - A gradle plugin

### What's fixed

 - Support for Java 9-11 ([#337](https://github.com/puniverse/quasar/issues/337), [#344](https://github.com/puniverse/quasar/issues/344, [#317](https://github.com/puniverse/quasar/issues/317))
 - A stack size bug ([#309](https://github.com/puniverse/quasar/issues/309))
 - [vertx-sync](https://github.com/hiddenswitch/vertx-fibers) fork is now updated ([#327](https://github.com/puniverse/quasar/issues/327))
 - Suspendable iterators ([#297](https://github.com/puniverse/quasar/pull/297))
 - A time error ([#319](https://github.com/puniverse/quasar/pull/319))
 - Package relocation for `asm` to prevent conflicts with other libraries that use it

### What's Missing

 - Kryo-based fiber serialization
 - `disruptor`, `galaxy`, `kotlin` and `reactive-streams` since they are out of scope for being tested by me

### What is included but unsupported

 - `actors`: passes tests but I do not use it in production
 - `maven-plugin`

It fixes important bugs related to package relocation, removes the Kryo-based serialization (since it was buggy), fixes an issue with timeouts, fixes an issue with stack sizes, and adds support for suspendable iterators.

### Development

> How do I edit `gradle-plugin-test`?

In IntelliJ, right click on `gradle-plugin-test/build.gradle` and choose **Link Gradle Project**.

To instrument a module, add `apply from: '../gradle/instrument.gradle'` to its **build.gradle** file. This will also create an `uninstrumentedJars` configuration that can be used when instrumentation is not possible, as with ahead-of-time compiled binaries.
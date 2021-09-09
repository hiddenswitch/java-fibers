# quasar-core fork

This is a fork of Parallel Universe's Quasar fibers library for Java, updated to work only with Java 11+ without modules.

### Requirements

 - Java 11 or higher
 
### Instrumenting in Gradle

Add the plugin to your `plugins` block in `build.gradle` and include a dependency on this library:

**build.gradle**
```groovy
plugins {
  id 'java-library'
  id 'com.hiddenswitch.fibers.instrument' version '1.0.3'
}

dependencies {
  api "com.hiddenswitch:quasar-core:10.0.2"
}
```

Your code is now instrumented. The `fibers` extension supports the following settings:

**build.gradle**
```groovy
fibers {
  // The Quasar instrumentation includes checks to see if the methods it is entering are instrumented.
  check = false
  // Prints instrumentation messages.
  verbose = false
  // Allows Java synchronized blocks to be instrumented.
  allowMonitors = false
  // Allows suspendable methods to call Java blocking methods.
  allowBlocking = false
  // Prints debug messaging.
  debug = false
  // Writes the instrumented classes
  writeClasses = true
  // Automatically determine which methods should be instrumented based on static analysis.
  scanSuspendables = false
  // Instrument the interfaces that suspendable methods override.
  scanSuspendableSupers = false
}
```

All `compileJava` tasks for all source sets will be instrumented.

### What's New

 - Support for Java 12-16
 - Non-AOT support for GraalVM
 - Micrometer metrics
 - A Gradle plugin

### What's Fixed

 - Support for Java 9-11 ([#337](https://github.com/puniverse/quasar/issues/337), [#344](https://github.com/puniverse/quasar/issues/344), [#317](https://github.com/puniverse/quasar/issues/317))
 - A stack size bug ([#309](https://github.com/puniverse/quasar/issues/309))
 - [vertx-sync](https://github.com/hiddenswitch/vertx-fibers) fork is now updated ([#327](https://github.com/puniverse/quasar/issues/327))
 - Suspendable iterators ([#297](https://github.com/puniverse/quasar/pull/297))
 - A time error ([#319](https://github.com/puniverse/quasar/pull/319))
 - Package relocation for `asm` to prevent conflicts with other libraries that use it.
 
### Unsupported

- GraalVM's Native Image cannot build instrumented libraries. (Comment on [oracle/graal/3759](https://github.com/oracle/graal/issues/3759)) to get Graal to fix this issue)
- `actors`: passes tests but unused anywhere in production
- `maven-plugin`

### Coming Soon

 - How to reference the uninstrumented libraries for building native images using Threads instead of Fibers.

### What's Missing

- Kryo-based fiber serialization
- `disruptor`, `galaxy`, `kotlin` and `reactive-streams` since they are out of scope for being tested by me

### Development

> How do I edit `gradle-plugin-test`?

In IntelliJ, right click on `gradle-plugin-test/build.gradle` and choose **Link Gradle Project**.
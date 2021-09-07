package com.hiddenswitch.fibers.gradle

import org.gradle.api.provider.Property

abstract class FiberInstrumentationExtension {
  /**
   * The Quasar instrumentation includes checks to see if the methods it is entering are instrumented.
   * @return
   */
  abstract Property<Boolean> getCheck();
  /**
   * Prints instrumentation messages.
   * @return
   */
  abstract Property<Boolean> getVerbose();
  /**
   * Allows Java {@code synchronized} blocks to be instrumented.
   * @return
   */
  abstract Property<Boolean> getAllowMonitors();
  /**
   * Allows suspendable methods to call Java blocking methods.
   * @return
   */
  abstract Property<Boolean> getAllowBlocking();
  /**
   * Prints debug messaging.
   * @return
   */
  abstract Property<Boolean> getDebug();
  /**
   * Writes the instrumented classes.
   * @return
   */
  abstract Property<Boolean> getWriteClasses();
  /**
   * Automatically determine which methods should be instrumented based on static analysis.
   * @return
   */
  abstract Property<Boolean> getScanSuspendables();
  /**
   * Instrument the interfaces that suspendable methods override.
   * @return
   */
  abstract Property<Boolean> getScanSuspendableSupers();

  FiberInstrumentationExtension() {
    check.convention(false)
    verbose.convention(false)
    allowMonitors.convention(false)
    allowBlocking.convention(false)
    debug.convention(false)
    writeClasses.convention(true)
    scanSuspendables.convention(false)
    scanSuspendableSupers.convention(false)
  }
}

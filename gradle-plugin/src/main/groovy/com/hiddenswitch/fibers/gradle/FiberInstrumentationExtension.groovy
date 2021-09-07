package com.hiddenswitch.fibers.gradle

import org.gradle.api.provider.Property

abstract class FiberInstrumentationExtension {
  abstract Property<Boolean> getCheck();
  abstract Property<Boolean> getVerbose();
  abstract Property<Boolean> getAllowMonitors();
  abstract Property<Boolean> getAllowBlocking();
  abstract Property<Boolean> getDebug();
  abstract Property<Boolean> getWriteClasses();
  abstract Property<Boolean> getScanSuspendables();
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

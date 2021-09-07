package com.hiddenswitch.fibers.gradle

import co.paralleluniverse.fibers.instrument.SimpleSuspendableClassifier

class TemporarySuspendableClassifier extends SimpleSuspendableClassifier {

  public static final String SUSPENDABLES_FILE_PROP = "fibers.suspendablesFile"
  public static final String SUSPENDABLE_SUPERS_FILE_PROP = "fibers.suspendableSupersFile"

  TemporarySuspendableClassifier() {
    super((ClassLoader) null)
    var suspendableFilePath = System.getProperty(SUSPENDABLES_FILE_PROP)
    var suspendableSupersFilePath = System.getProperty(SUSPENDABLE_SUPERS_FILE_PROP)
    if (suspendableFilePath != null) {
      SimpleSuspendableClassifier.parse(new URL("file:" + suspendableFilePath), suspendables, suspendableClasses)
    }
    if (suspendableSupersFilePath != null) {
      SimpleSuspendableClassifier.parse(new URL("file:" + suspendableSupersFilePath), suspendableSupers, suspendableSuperInterfaces)
    }
  }

}

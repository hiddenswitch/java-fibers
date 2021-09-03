package com.hiddenswitch.fibers.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction

class FiberInstrumentationTask extends DefaultTask {

  @TaskAction
  def instrument() {
    scanAndInstrument()
  }

  void scanAndInstrument(SourceSet sourceSet, List<Configuration> configs) {
    def classpath = '' + sourceSet.output.classesDirs.asPath + ':' + sourceSet.output.resourcesDir + ':' + configs*.asPath.join(':')

    ant.taskdef(
            name: 'scanSuspendables', classname: 'co.paralleluniverse.fibers.instrument.SuspendablesScanner',
            classpath: classpath)
    ant.scanSuspendables(
            auto: true,
            supersFile: "$project.sourceSets.main.resources.dirs.asPath/META-INF/suspendable-supers",
            append: false) {
      project.fileset(dir: sourceSet.output.classesDirs.asPath)
    }

    ant.taskdef(name: 'instrumentation', classname: 'co.paralleluniverse.fibers.instrument.InstrumentationTask',
            classpath: classpath)
    ant.instrumentation(check: 'true') {
      project.fileset(dir: sourceSet.output.classesDirs.asPath) {
        project.exclude(name: 'co/paralleluniverse/fibers/instrument/*.class')
      }
    }
  }
}

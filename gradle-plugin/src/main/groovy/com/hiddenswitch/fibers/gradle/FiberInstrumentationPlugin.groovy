package com.hiddenswitch.fibers.gradle


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.scala.ScalaCompile

class FiberInstrumentationPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    project.tasks.withType(AbstractCompile.class) { task ->
      task.doLast {
        if (!(task instanceof JavaCompile) && !(task instanceof GroovyCompile) && !(task instanceof ScalaCompile)) {
          return
        }

        project.ant.taskdef(name: 'instrumentation', classname: 'co.paralleluniverse.fibers.instrument.InstrumentationTask', classpath: project.sourceSets.main.runtimeClasspath.asPath)
        project.ant.instrumentation(check: 'true', allowMonitors: 'true', allowBlocking: 'true') {
          fileset(dir: task.outputs.files.asPath) {
            exclude(name: 'co/paralleluniverse/fibers/instrument/*.class')
          }
        }
      }
    }
  }
}

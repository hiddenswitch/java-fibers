package com.hiddenswitch.fibers.gradle

import co.paralleluniverse.fibers.instrument.InstrumentationTask
import co.paralleluniverse.fibers.instrument.SuspendablesScanner
import org.apache.tools.ant.types.FileSet
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.scala.ScalaCompile

import java.nio.file.Path

class FiberInstrumentationPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    def taskExtension = project.extensions.create('fibers', FiberInstrumentationExtension)

    project.tasks.withType(AbstractCompile.class) { Task task ->
      if (!(task instanceof JavaCompile) && !(task instanceof GroovyCompile) && !(task instanceof ScalaCompile)) {
        return
      }

      task.doLast {
        def generatesSuspendableFiles = taskExtension.scanSuspendables || taskExtension.scanSuspendableSupers
        def suspendableSupersFile = "$task.temporaryDir/scanner/META-INF/suspendable-supers".toString()
        def suspendablesFile = "$task.temporaryDir/scanner/META-INF/suspendables".toString()
        def classesDirectory = task.destinationDirectory.asFile.get()

        if (generatesSuspendableFiles) {
          def scanTask = new SuspendablesScanner(classesDirectory.toPath())
          scanTask.setURLs(task.classpath.collect { it.toURI().toURL() })
          scanTask.project = project.ant.project
          scanTask.auto = true
          scanTask.append = false

          if (taskExtension.scanSuspendables) {
            scanTask.suspendablesFile = suspendablesFile
          }

          if (taskExtension.scanSuspendableSupers) {
            scanTask.supersFile = suspendableSupersFile
          }

          scanTask.execute()

          if (taskExtension.scanSuspendables) {
            System.properties.put(TemporarySuspendableClassifier.SUSPENDABLES_FILE_PROP, suspendablesFile)
          }

          if (taskExtension.scanSuspendableSupers) {
            System.properties.put(TemporarySuspendableClassifier.SUSPENDABLE_SUPERS_FILE_PROP, suspendableSupersFile)
          }
        }

        def instrumentationTask = new InstrumentationTask() {
          @Override
          void log(Throwable t, int msgLevel) {
            LogLevel logLevel = toLogLevel(msgLevel)
            project.logger.log(logLevel, "", t)
          }

          @Override
          void log(String msg) {
            project.logger.info(msg)
          }

          @Override
          void log(String msg, Throwable t, int msgLevel) {
            project.logger.log(toLogLevel(msgLevel), msg, t)
          }

          @Override
          void log(String msg, int msgLevel) {
            project.logger.log(toLogLevel(msgLevel), msg)
          }

          private LogLevel toLogLevel(int msgLevel) {
            def logLevel = LogLevel.DEBUG
            switch (msgLevel) {
              case org.apache.tools.ant.Project.MSG_ERR:
                logLevel = LogLevel.ERROR
                break
              case org.apache.tools.ant.Project.MSG_DEBUG:
                logLevel = LogLevel.DEBUG
                break
              case org.apache.tools.ant.Project.MSG_INFO:
                logLevel = LogLevel.INFO
                break
              case org.apache.tools.ant.Project.MSG_WARN:
                logLevel = LogLevel.WARN
                break
            }
            return logLevel
          }
        }
        instrumentationTask.check = taskExtension.check
        instrumentationTask.verbose = taskExtension.verbose
        instrumentationTask.allowMonitors = taskExtension.allowMonitors
        instrumentationTask.allowMonitors = taskExtension.allowBlocking
        instrumentationTask.debug = taskExtension.debug
        instrumentationTask.writeClasses = taskExtension.writeClasses
        instrumentationTask.project = project.ant.project
        instrumentationTask.addAllClasspath(task.classpath.files.toList())
        def fileSet = new FileSet(dir: classesDirectory)
        fileSet.appendExcludes(new String[]{'co/paralleluniverse/fibers/instrument/*.class'})
        instrumentationTask.addFileSet(fileSet)
        instrumentationTask.execute()
      }
    }
  }
}

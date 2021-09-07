package com.hiddenswitch.fibers.gradle

import co.paralleluniverse.fibers.instrument.InstrumentationTask
import org.apache.tools.ant.types.FileSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.scala.ScalaCompile

class FiberInstrumentationPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    def taskExtension = project.extensions.create('fibers', FiberInstrumentationExtension)

    project.tasks.withType(AbstractCompile.class) { Task task ->
      task.doLast {
        if (!(task instanceof JavaCompile) && !(task instanceof GroovyCompile) && !(task instanceof ScalaCompile)) {
          return
        }

        def classesDirectory = task.destinationDirectory.asFile.get()

        var instrumentationTask = new InstrumentationTask() {
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
            var logLevel = LogLevel.DEBUG
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

        def set = new FileSet(dir: classesDirectory)
        instrumentationTask.project = project.ant.project
        instrumentationTask.addFileSet(set)
        instrumentationTask.execute()
      }
    }
  }
}

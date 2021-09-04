package com.hiddenswitch.fibers.gradle

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class FiberInstrumentationPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    if (getGradleMajorVersion(project) < 7) {
      throw new GradleException("Only supports Gradle 7 and later")
    }

    project.plugins.withType(JavaPlugin) {
      var instrumentedClasses = project.tasks.create('fiberInstrumentClasses')
    }
  }

  static File getJavaMainOutputDir(Project project)
  {
    def classesDir = project.sourceSets.main.java.classesDirectory.getAsFile().get()
    return classesDir
  }

  static int getGradleMajorVersion(Project project)
  {
    String gradleVersion = project.getGradle().getGradleVersion()
    Integer.valueOf(gradleVersion.substring(0, gradleVersion.indexOf(".")))
  }
}

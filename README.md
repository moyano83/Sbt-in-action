# Sbt-in-action

# Table of Contents

1. [Chapter 1: Why SBT?](#Chapter1)
2. [Chapter 2: Getting Started](#Chapter2)
2. [Chapter 3: Core Concepts](#Chapter3)

# Chapter 1: Why SBT? <a name="Chapter1"></a>

Sbt has default project layouts, built in tasks (compile, test, publish). Sbt provides flexibility and the ability to insert custom tasks easily, and each task has an output (value or file) and explicit dependencies which allows sbt to execute task in parallel. Sbt is composed of:

* Tasks: Run in parallel, there are defaults like test, compile, etc... and are writen in scala.
* Settings: It simply is a value.

Sbt allows for cross compililation using different versions of scala:

```
scalaVersion := "2.10.1"

crossScalaVersions := Seq("2.8.2", "2.9.2")
``` 

Sbt uses Ivy for its internal dependency resolution. It has a command line tool (it opens when we type `sbt` without parameters), and we can pass commands such as `compile` or `test` to it. If we want to run a task continually we use `~` like in `~test` (the whole cyle of test is executed when the source code changes).

# Chapter 2: Getting Started<a name="Chapter2"></a>

Executing `sbt` for the first time downloads the dependencies needed to run sbt, as well as it initiates the command line after (`exit` exits the command line). Every project using sbt should have two files:

* _project/build.properties_: Used to inform sbt which version it should use for your build.
* _build.sbt_: Actual settings for the build.

Sbt includes defaults to create projects (the default project layout is borrowed from Maven), but it is a good practice to include the previous files manually. The most important parameters to run in the shell are:

* `tasks`: List the task to run in the build
* `settings`: List the settings you can modify for the project
* `inspect`: Displays informatino about a given task or setting.

Classes can be compiled using the `compile` command, and run using the `run` command (main classes). To open an interactive scala shell to test your classes execute `console`, if you make any changes on the project, you can reload the sbt console using the `reload` command. As stated in chapter 1, you can run the test continuously every time the source code changes if you prepend the command with the `~` character. With `testOnly`, you can specify which test to run (use tab autocompletion to see the available tests).

# Chapter 3: Core Concepts<a name="Chapter3"></a>

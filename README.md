# Sbt-in-action

# Table of Contents

1. [Chapter 1: Why SBT?](#Chapter1)
2. [Chapter 2: Getting Started](#Chapter2)

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


# Sbt-in-action

# Table of Contents

1. [Chapter 1: Why SBT?](#Chapter1)
2. [Chapter 2: Getting Started](#Chapter2)
3. [Chapter 3: Core Concepts](#Chapter3)
4. [Chapter 4: The Default Build](#Chapter4)
5. [Chapter 5: Testing](#Chapter5)

# Chapter 1: Why SBT? <a name="Chapter1"></a>

Sbt has default project layouts, built in tasks (compile, test, publish). Sbt provides flexibility and the ability to insert custom tasks easily, and each task has an output (value or file) and explicit dependencies which allows sbt to execute task in parallel. Sbt is composed of:

* Tasks: Run in parallel, there are defaults like test, compile, etc... and are written in scala.
* Settings: It simply is a value.

Sbt allows for cross compilation using different versions of scala:

```scala
scalaVersion := "2.10.1"

crossScalaVersions := Seq("2.8.2", "2.9.2")
``` 

Sbt uses Ivy for its internal dependency resolution. It has a command line tool (it opens when we type `sbt` without parameters), and we can pass commands such as `compile` or `test` to it. If we want to run a task continually we use `~` like in `~test` (the whole cycle of test is executed when the source code changes).

# Chapter 2: Getting Started<a name="Chapter2"></a>

Executing `sbt` for the first time downloads the dependencies needed to run sbt, as well as it initiates the command line after (`exit` exits the command line). Every project using sbt should have two files:

* _project/build.properties_: Used to inform sbt which version it should use for your build.
* _build.sbt_: Actual settings for the build.

Sbt includes defaults to create projects (the default project layout is borrowed from Maven), but it is a good practice to include the previous files manually. The most important parameters to run in the shell are:

* `tasks`: List the task to run in the build
* `settings`: List the settings you can modify for the project
* `inspect`: Displays information about a given task or setting.

Classes can be compiled using the `compile` command, and run using the `run` command (main classes). To open an interactive scala shell to test your classes execute `console`, if you make any changes on the project, you can reload the sbt console using the `reload` command. You can run the test continuously every time the source code changes if you prepend the command with the `~` character. With `testOnly`, you can specify which test to run (use tab autocompletion to see the available tests).

# Chapter 3: Core Concepts<a name="Chapter3"></a>

The build.sbt file defines settings for the project, being a setting a key, an initialization, and an operator (`:=`) that associates the key with the initialization. Sbt reads all the settings defined in your build at load time and runs their initializations, which produce the final setting values used for your build. A setting is used to change an aspect of the build or add functionality. A setting has a type and an initialization of a setting can only return the same type than the setting. 
To define a dependency, use the `librarydependencies += "groupId" % "artifactId" % "version"` notation. The `libraryDependencies` key is of type `Seq[ModuleID]` and the `+=` operator takes the previous value of the setting and assign a new value to it (or use the `++=` operator to add more than one comma separated value). You can use previously defined settings to specify another setting like `"groupId" % "artifactId" % version.value`, where the `value` method of a setting returns the actual value of the setting.
A task in sbt is like a setting that runs every time you request its value. You can create new tasks by defining a variable or method: this is called a definition. A definition is executed before the settings, therefore settings can refer to a definition. You can use a setting after to give value to a definition:

```scala
val gitCommit = keyTask[String]("Determines the git commit")

gitCommit := Process("git rev-parse HEAD").lines.head
```

Sbt separates the computation of a value from the slot that stores the value. To display the result of a task use the `show` command, for example in the previous definition `show gitCommit`. If a task fails, it will stop the task with an error, but other tasks will continue to execute. When the user request a task to be executed, dependent tasks are also executed and pass the result to the executed task.The `resourceGenerators` setting is defined as a setting that stores all the tasks used to generate resources.
Configurations are namespaces for keys, sbt has several default configurations (Compile, Test, Runtime, Default, Pom, Optional, System, Provided, Docs and Sources). 
You can also define different namespaces by using sub-projects, to define a sub-project, add the following lines:

```scala
lazy val projectName = Project("projectName", file("locationRelativeToTheBaseDir")).settings()
```
 
To check the projects contained in a build, run the command `projects`. Sbt creates a root project that represent the same directory where the build.sbt is located. To execute a task in a project, prefix the task with the name of the project followed by 
`/`. To add new settings (for example dependencies) to a project, use the settings method, for example:

```scala
lazy val projectName = Project("projectName", file("locationRelativeToTheBaseDir")).settings(libraryDependencies ++= ...)
```

Project dependencies are defined using the `dependsOn` method on `Project`. Use the `lazy val` initialization on the sbt file to avoid problems with circular references. To share a task across different projects, you can define it using `in ThisBuild` qualifier to the task key when you define the setting. By default, sbt will run all un-prefixed tasks/settings against all the projects defined in the build.

# Chapter 4: The Default Build<a name="Chapter4></a>

In sbt, you can inspect the dependencies a task has by using the `inspect tree <task/setting>`, this command displays an ascii tree with detailing which settings/tasks the task is dependent on and what values those setting/tasks returns.
In Sbt there is two types of sources/resources for projects:

* _unmanaged sources/resources_: Sources discovered by convention, the user has to do the work for adding, modifying and tracking the source files. Makes use of file filters (only for sources) and default set of configurations to produce the sequence of source files for the project. 
* _managed sources/resources_: Sbt will create the track for you. 

An example of unmanaged sources can be seen by executing `show javaSource` which displays the source folder for java classes and it is borrowed from maven. Sbt mirrors different keys in different namespaces, an example are sources, and test sources files: Sbt places the _source file_ setting in the _Compile_ namespace and the _test source file_ setting in the _Test_ namespace. To change a default setting like `sourceDirectory` (which is dependant of the `baseDirectory` setting), we can do the following:

```scala
sourceDirectory := new File(baseDirectory.value, "customDirectory") //this will place sources in <project>/customDirectory
```

The _main_ and _test_ settings are dependent of _sourceDirectory_ of the _Global_ configuration, but both are defined in different scopes (_main_ in _Compile_, and _test_ in _Test_):

```scala
sourceDirectory in Compile := new File(sourceDirectory.value, "main") //same for tes in the Test configuration
```

This chain of settings is also used for the java, scala, resources folders (also in different configuration namespaces). Sbt applies filters to include/exclude source files in the unmanaged sources setting. The `include-filter` setting includes all *.java and *.scala files, and the `exclude-filter` excludes any hidden files (starting with _._), exclude filters take precedence over include filters. You can also change this behaviour:

```scala
exclude-filter in (Compile, unmanagedSources) := NothingFilter //Scoped to the unmanagedSources task in Compile
```

Dependencies on 3rd party libraries are also split in two parts:

* _internal dependencies_: Between projects defined in the same build
* _external dependencies_: Must be pulled from somewhere else like maven, ivy or the filesystem.
    - _unmanaged dependencies_: Sbt discovers this dependencies from default locations (i.e. the lib folder). Sbt will put all  dependencies found in the _unmanagedClasspath_ setting. The default path can also be changed by altering the _unmanaged-base_ setting.
    - _managed dependencies_: external dependencies declared in the build.sbt and resolved by the `update` task. `update` calls the `ivySbt` task which can be configured through the `resolvers` setting to specify how and where to find the dependencies.

With Sbt and as stated before, an managed classpath dependency is declared as a moduleID (which the method `%` provides a shortcut to create one), and can be declared in different scopes. The syntax is:

```scala
libraryDependencies := Seq("<organization>" % "<name>" % "<revision>" % "<configuration>")
```

We can use the method `%%` between the organization and name, so sbt will choose the right version of the scala binary version (i.e. scala 2.10 instead of 2.11). If no configuration is specified, the default one is used.
Sbt uses a task called `package` to wrap your production, `package` depends on `packageBin` which uses the `mappings` task to determine what should go in the production. `mappings` is of type `Seq[(File, String)]`, the files are the list of files to be included, and the and the Strings are the locations within the jar to include the files. For example, to include a license file in the jar base path:

```scala
// The LICENSE file in the base directory ends up in the base directory of the jar renamed to LICENSE.MIT
mappings in packageBin in Compile += (baseDirectory.value, "LICENSE") -> "LICENSE.MIT"  
```

It is possible to define the name of a project using the `name` setting which is of type `Setting[String]`, same for the `organization` and `version` settings. the `organization` and `version` settings are usually defined at build level

# Chapter 5: Testing <a name="Chapter5">
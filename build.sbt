
inThisBuild(List(
  organization := "io.get-coursier",
  homepage := Some(url("https://github.com/coursier/interface")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "alexarchambault",
      "Alexandre Archambault",
      "alexandre.archambault@gmail.com",
      url("https://github.com/alexarchambault")
    )
  )
))

lazy val interface = project
  .enablePlugins(ShadingPlugin)
  .settings(
    Settings.shared,
    // shading stuff
    publish := publish.in(Shading).value,
    publishLocal := publishLocal.in(Shading).value,
    inConfig(_root_.coursier.ShadingPlugin.Shading)(com.typesafe.sbt.pgp.PgpSettings.projectSettings),
    ShadingPlugin.projectSettings, // seems this has to be repeated, *after* the addition of PgpSettings…
    PgpKeys.publishSigned := PgpKeys.publishSigned.in(Shading).value,
    PgpKeys.publishLocalSigned := PgpKeys.publishLocalSigned.in(Shading).value,
    shadingNamespace := "coursierapi.shaded",
    shadeNamespaces ++= Set(
      "coursier",
      "io.github.soc.directories",
      "scala"
    ),

    autoScalaLibrary := false,
    libraryDependencies += "io.get-coursier" %% "coursier" % "1.1.0-M13-1" % "shaded",

  )

lazy val interpolators = project
  .dependsOn(interface)
  .settings(
    Settings.shared,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "com.lihaoyi" %% "utest" % "0.6.6" % Test
    ),
    testFrameworks += new TestFramework("utest.runner.Framework"),
  )

lazy val `coursier-interface` = project
  .in(file("."))
  .aggregate(interface, interpolators)
  .settings(
    publishArtifact := false
  )

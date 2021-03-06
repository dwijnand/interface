
import com.typesafe.tools.mima.plugin.MimaPlugin
import sbt._
import sbt.Keys._

object Settings {

  private def scala211 = "2.11.12"
  private def scala212 = "2.12.8"

  lazy val shared = Seq(
    scalaVersion := scala211,
    crossScalaVersions := Seq(scala212, scala211),
    scalacOptions += "-target:jvm-1.8",
    javacOptions ++= Seq(
      "-source", "1.8",
      "-target", "1.8"
    ),
    // mima
    MimaPlugin.autoImport.mimaPreviousArtifacts := {
      Mima.binaryCompatibilityVersions.map { ver =>
        organization.value %% moduleName.value % ver
      }
    }
  )

}

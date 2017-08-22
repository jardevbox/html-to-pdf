name := "html-to-pdf"

lazy val commonSettings = Seq(
  organization := "de.envisia.pdf",
  scalaVersion := "2.12.3",
  scalacOptions in(Compile, doc) ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation"
  ),
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-o"),
  publishMavenStyle in ThisBuild := true,
  pomIncludeRepository in ThisBuild := { _ => false },
  publishTo in ThisBuild := Some("envisia-internal" at "https://nexus.envisia.de/repository/internal/")
)

val pdfBoxVersion = "2.0.7"

lazy val `html-to-pdf-core` = (project in file("html-to-pdf-core"))
    .settings(commonSettings)

lazy val `html-to-pdf-pdfbox` = (project in file("html-to-pdf-pdfbox"))
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "de.rototor.pdfbox" % "graphics2d" % "0.3",
        "org.apache.pdfbox" % "pdfbox" % pdfBoxVersion
      )
    )
    .dependsOn(`html-to-pdf-core`)

lazy val `html-to-pdf-root` = (project in file("."))
    .aggregate(
      `html-to-pdf-core`,
      `html-to-pdf-pdfbox`
    )


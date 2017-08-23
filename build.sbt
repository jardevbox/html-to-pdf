import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

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
val zxingVersion = "3.3.0"
val jsoupVersion = "1.10.3"

lazy val `html-to-pdf-core` = (project in file("html-to-pdf-core"))
    .settings(commonSettings)

lazy val `html-to-pdf-jsoup` = (project in file("html-to-pdf-jsoup"))
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.jsoup" % "jsoup" % jsoupVersion
      )
    )

lazy val `html-to-pdf-pdfbox` = (project in file("html-to-pdf-pdfbox"))
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "com.google.zxing" % "core" % "3.3.0", // Barcode Support
        "de.rototor.pdfbox" % "graphics2d" % "0.3",
        "org.apache.pdfbox" % "pdfbox" % pdfBoxVersion
      )
    )
    .dependsOn(`html-to-pdf-core`)

lazy val `html-to-pdf-root` = (project in file("."))
    .settings(
      publishArtifact := false,
      // The above is enough for Maven repos but it doesn't prevent publishing of ivy.xml files
      publish := {},
      publishLocal := {}
    )
    .aggregate(
      `html-to-pdf-core`,
      `html-to-pdf-pdfbox`,
      `html-to-pdf-jsoup`
    )

// releasePublishArtifactsAction := PgpKeys.publishSigned.value // : Signed Publish
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies, // : ReleaseStep
  inquireVersions, // : ReleaseStep
  runClean, // : ReleaseStep
  runTest, // : ReleaseStep
  setReleaseVersion, // : ReleaseStep
  commitReleaseVersion, // : ReleaseStep, performs the initial git checks
  tagRelease, // : ReleaseStep
  publishArtifacts, // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion, // : ReleaseStep
  commitNextVersion, // : ReleaseStep
  pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
)

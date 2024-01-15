val scala3Version = "3.3.1"

lazy val commonSettings = Seq(
  Test / parallelExecution := false,
  scalaVersion := scala3Version,
  Compile / scalacOptions += "-Xlint -Yretain-trees",
  Compile / console / scalacOptions --= Seq("-Ywarn-unused", "-Ywarn-unused-import"),

)

lazy val lm = project
  .in(file("."))
  .settings(
    name := "lm",
    version := "0.1.0-SNAPSHOT",
    commonSettings,
  )
  .aggregate(
    common,
    web
  )

lazy val common = (project in file("common"))
  .settings(
    name := "lm.common",
    commonSettings,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-config" % "4.0.0-RC16",
      "dev.zio" %% "zio-config-magnolia" % "4.0.0-RC16",
      "dev.zio" %% "zio-config-typesafe" % "4.0.0-RC16",
      "dev.zio" %% "zio-config-refined" % "4.0.0-RC16",

      "dev.zio" %% "zio-http" % "3.0.0-RC3",
      "com.mysql" % "mysql-connector-j" % "8.2.0",
      "com.zaxxer" % "HikariCP" % "5.1.0",
      "org.liquibase" % "liquibase-core" % "4.25.0",
    ),
    publishArtifact := false
  )

lazy val crawler = (project in file("crawler"))
  .dependsOn(common)
  .settings(
    name := "lm.crawler",
    commonSettings,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-http" % "3.0.0-RC3",
      "dev.zio" %% "zio-json" % "0.6.2",
    ),
    publishArtifact := false
  )

lazy val web = (project in file("web"))
  .dependsOn(common)
  .settings(
    name := "lm.web",
    commonSettings,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-http" % "3.0.0-RC3",
    ),
    publishArtifact := false
  )

val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "lucru_monitor",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      //"com.typesafe" % "config" % "1.4.3",

"dev.zio" %% "zio-config" % "4.0.0-RC16",
"dev.zio" %% "zio-config-magnolia" % "4.0.0-RC16",
"dev.zio" %% "zio-config-typesafe" % "4.0.0-RC16",
"dev.zio" %% "zio-config-refined" % "4.0.0-RC16",

      "dev.zio" %% "zio-http" % "3.0.0-RC3",
      "com.mysql" % "mysql-connector-j" % "8.2.0",
      "com.zaxxer" % "HikariCP" % "5.1.0",
      "org.liquibase" % "liquibase-core" % "4.25.0",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )

import com.typesafe.sbt.packager.archetypes.ServerLoader

name := "booking-example"
version := "0.0.1"
lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(DebianPlugin)

scalaVersion := "2.11.8"
incOptions := incOptions.value.withNameHashing(true)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

routesImport += "core.Binders._"
routesGenerator := InjectedRoutesGenerator

//packager conf

maintainer in Linux := "Marcin Gosk <marcin.gosk@gmail.com>"

serverLoading in Debian := ServerLoader.SystemV

packageSummary in Linux := "booking-example"

packageDescription := "booking-example"

daemonUser in Linux := "booking-example"

daemonGroup in Linux := "booking-example"

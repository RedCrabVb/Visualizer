import Dependencies.{Spark, Other}

name := "Visualizer"

version := "0.1"

scalaVersion := "2.12.12"


lazy val root = (project in file ("."))
  .settings(libraryDependencies ++= Seq(Spark.Core, Spark.Sql, Other.commonsCli))
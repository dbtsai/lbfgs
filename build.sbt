sonatypeSettings

name := "lbfgs"

description := "A Java Port of L-BFGS variant Optimization Algorithms."

sbtVersion := "0.13.1"

scalaVersion := "2.11.2"

organization := "com.dbtsai.lbfgs"

version := "0.1-SNAPSHOT"

scalacOptions := Seq("-deprecation", "-unchecked")

libraryDependencies ++= Seq(
 "org.scalanlp" % "breeze_2.11" % "0.10",
 "com.github.fommil.netlib" % "all" % "1.1.2"
)
  
crossPaths := false

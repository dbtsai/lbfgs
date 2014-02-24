publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <parent>
    <groupId>org.sonatype.oss</groupId>
    <artifactId>oss-parent</artifactId>
    <version>7</version>
  </parent>
  <url>https://github.com/dbtsai/lbfgs</url>
  <licenses>
    <license>
      <name>Apache License 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
    <license>
      <name>GNU General Public License (GPL)</name>
      <url>http://www.gnu.org/licenses/gpl.html</url>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:dbtsai/lbfgs.git</url>
    <connection>scm:git:git://github.com/dbtsai/lbfgs.git</connection>
  </scm>
  <developers>
    <developer>
      <id>robert.dodier</id>
      <name>Robert Dodier</name>
    </developer>
    <developer>
      <id>dbtsai</id>
      <name>DB Tsai</name>
      <url>http://www.dbtsai.com/</url>
    </developer>
  </developers>)
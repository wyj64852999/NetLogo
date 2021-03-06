import sbt._
import sbt.File

trait SubApplication {
  def name:          String
  def jarName:       String
  def jvmOptions:    Seq[String]
  def jvmArguments:  Seq[String]
  def jvmProperties: Map[String, String] = Map()
  def additionalArtifacts(dist: File): Seq[File] = Seq()
}

object NetLogoCoreApp extends SubApplication {
  override def name          = "NetLogo"
  override def jarName       = "NetLogo"
  override def jvmOptions    = Seq()
  override def jvmArguments  = Seq()
}

object NetLogoThreeDApp extends SubApplication {
  override def name          = "NetLogo 3D"
  override def jarName       = "NetLogo"
  override def jvmOptions    = Seq("-Dorg.nlogo.is3d=true")
  override def jvmArguments  = Seq()
}

object NetLogoLoggingApp extends SubApplication {
  override def name          = "NetLogo Logging"
  override def jarName       = "NetLogo"
  override def jvmOptions    = Seq()
  override def jvmArguments  = Seq("--logging", "netlogo_logging.xml")
  override def additionalArtifacts(dist: File): Seq[File] =
    Seq(dist / "netlogo_logging.xml", dist / "netlogo_logging.dtd")
}

object HubNetClientApp extends SubApplication {
  override def name          = "HubNet Client"
  override def jarName       = "NetLogo"
  override def jvmOptions    = Seq()
  override def jvmArguments  = Seq()
  override def jvmProperties = Map("apple.laf.useScreenMenuBar" -> "true")
}

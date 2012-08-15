package org.nlogo.log

import java.net.URL
import java.io.{BufferedInputStream, FileOutputStream, BufferedOutputStream}
import java.nio.charset.Charset

import scala.io.Source

import org.apache.http.HttpResponse
import org.apache.http.client.methods.{HttpPost, HttpGet}
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity

// Redundant with other `NetUtils` files in our codebase, but separated for the sake of simplifying dependencies
object NetUtils {

  val DefaultByteEncoding = "ISO-8859-1"
  val DefaultReadSize = 1024

  // Sharing this has potential for statefulness problems....  Should we just make it anew for each request?
  val client = new org.apache.http.impl.client.DefaultHttpClient

  def httpGet(dest: URL): String = readResponse(client.execute(new HttpGet(dest.toURI)))

  def httpPost(postKVs: Map[String, String], dest: URL, encoding: String = DefaultByteEncoding): String = {
    import collection.JavaConverters.seqAsJavaListConverter
    val post = new HttpPost(dest.toURI)
    post.setEntity(new UrlEncodedFormEntity((postKVs map { case (key, value) => new BasicNameValuePair(key, value) } toSeq) asJava, Charset.forName("UTF-8")))
    readResponse(client.execute(post))
  }

  private def readResponse(response: HttpResponse) = Source.fromInputStream(response.getEntity.getContent).mkString.trim

  /**
   * @param from  The path of the file to download
   * @param to    The directory into which to download the file
   * @return      The full path of the downloaded file
   */
  def downloadFile(from: String, to: String): String = {

    val readSize = DefaultReadSize
    val fileName = from.reverse takeWhile (_ != '/') reverse
    val outPath  = to + System.getProperty("file.separator") + fileName

    val inStream  = new BufferedInputStream(new URL(from).openStream())
    val outStream = new FileOutputStream(outPath)
    val outBuffer = new BufferedOutputStream(outStream, readSize)
    val data = new Array[Byte](readSize)
    var x = inStream.read(data, 0, readSize)

    while (x >= 0) {
      outBuffer.write(data, 0, x)
      x = inStream.read(data, 0, readSize)
    }

    outBuffer.close()
    inStream.close()
    outStream.close()

    outPath

  }

}


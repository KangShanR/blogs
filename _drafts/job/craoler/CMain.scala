package info.youqian.akkatcpecho.crawler

import java.net.{HttpURLConnection, URL, URLEncoder}

import collection.JavaConverters._
import scala.io.Source

object CMain {
  val EntryUrl = "http://pd4b6j.v.vote8.cn/m?from=groupmessage&isappinstalled=0"
  val AjaxUrl  = "http://pd4b6j.v.vote8.cn/Front/Ajax/GetVote.ashx?VoteChannel=MobileWeb&OptionID=6929721&TimeStamp=%s&RefererUrl="

  val UserAgents = Seq(
    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"
  )
  def main(args: Array[String]): Unit = {
    for (i ← 0 until 50) {
      println(s"========================== $i =========================")
      openEntry()
    }
  }

  def openEntry(): Unit = {
    val conn = new URL(EntryUrl).openConnection().asInstanceOf[HttpURLConnection]
    setCommonHeaders(conn)

    val is = conn.getInputStream
    val map = conn.getHeaderFields()
//    map.asScala.foreach(x => {
//      println(x)
//    })

    val cookiesVal = map.get("Set-Cookie").asScala.map(_.split(";").head).mkString("; ")
    println(cookiesVal)

    println("res: ")
    val content = Source.fromInputStream(is, "UTF-8").getLines().filter(_.contains("hiddenTimeStampEncodeString")).mkString
    println(content)
    val begin = content.indexOf("value=\"") + "value=\"".size
    val end   = content.indexOf("\" />")
    val timestamp = content.substring(begin, end)
    println(timestamp)

    val tsParam = URLEncoder.encode(timestamp, "UTF-8")
    val url = AjaxUrl.format(tsParam)
    println(tsParam)
    println(url)

    for (i ← 0 until 3) {
      val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
      setCommonHeaders(conn)
      conn.addRequestProperty("Cookie", cookiesVal)
      conn.addRequestProperty("Referer", "http://pd4b6j.v.vote8.cn/m?from=groupmessage&isappinstalled=0")
      conn.addRequestProperty("X-Requested-With", "XMLHttpRequest")
      val is = conn.getInputStream
      val res = Source.fromInputStream(is, "UTF-8").mkString
      println(s"res[$i]: $res")
    }

  }

  private def setCommonHeaders(conn: HttpURLConnection) = {
    conn.addRequestProperty("Accept-Language", "zh-CN,en-US;q=0.8,en;q=0.6,zh;q=0.4,zh-TW;q=0.2,ja;q=0.2")
    conn.addRequestProperty("Upgrade-Insecure-Requests", "1")
    conn.addRequestProperty("Cache-Control", "max-age=0")
    conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    conn.addRequestProperty("User-Agent", UserAgents(0))
  }
}

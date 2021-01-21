layout: "post"
title: "http"
date: "2017-11-27 17:07"
---

### url

http://pd4b6j.v.vote8.cn/m?from=groupmessage&isappinstalled=0


## 请求头需要设置这些内容：

conn.addRequestProperty("Accept-Language", "zh-CN,en-US;q=0.8,en;q=0.6,zh;q=0.4,zh-TW;q=0.2,ja;q=0.2")
conn.addRequestProperty("Upgrade-Insecure-Requests", "1")
conn.addRequestProperty("Cache-Control", "max-age=0")
conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")

### result



## scala

```scala
package info.youqian.akkatcpecho.crawler

import java.net.{HttpURLConnection, URL}
import collection.JavaConverters._

import scala.io.Source

object CMain {
  val EntryUrl = "http://pd4b6j.v.vote8.cn/m?from=groupmessage&isappinstalled=0"

  val UserAgents = Seq(
    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"
  )
  def main(args: Array[String]): Unit = {
    openEntry()
  }

  def openEntry(): Unit = {
    val conn = new URL(EntryUrl).openConnection().asInstanceOf[HttpURLConnection]
    setCommonHeaders(conn)

    val is = conn.getInputStream
    val map = conn.getHeaderFields()
    map.asScala.foreach(x => {
      println(x)
    })

    println("res: ")
    val content = Source.fromInputStream(is, "UTF-8").mkString
    println(content)
  }

  private def setCommonHeaders(conn: HttpURLConnection) = {
    conn.addRequestProperty("Accept-Language", "zh-CN,en-US;q=0.8,en;q=0.6,zh;q=0.4,zh-TW;q=0.2,ja;q=0.2")
    conn.addRequestProperty("Upgrade-Insecure-Requests", "1")
    conn.addRequestProperty("Cache-Control", "max-age=0")
    conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    conn.addRequestProperty("User-Agent", UserAgents(0))
  }
}
```

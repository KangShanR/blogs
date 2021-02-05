# BlockingQueue

## 对于操作阻塞队列响应的四种策略

1. 报出异常
2. 返回特殊值 null / 0
3. 一直阻塞
4. 限制时间内阻塞

<table BORDER CELLPADDING=3 CELLSPACING=1>
 <caption>Summary of BlockingQueue methods</caption>
  <tr>
    <td></td>
    <td ALIGN=CENTER><em>Throws exception</em></td>
    <td ALIGN=CENTER><em>Special value</em></td>
    <td ALIGN=CENTER><em>Blocks</em></td>
    <td ALIGN=CENTER><em>Times out</em></td>
  </tr>
  <tr>
    <td><b>Insert</b></td>
    <td>{@link #add add(e)}</td>
    <td>{@link #offer offer(e)}</td>
    <td>{@link #put put(e)}</td>
    <td>{@link #offer(Object, long, TimeUnit) offer(e, time, unit)}</td>
  </tr>
  <tr>
    <td><b>Remove</b></td>
    <td>{@link #remove remove()}</td>
    <td>{@link #poll poll()}</td>
    <td>{@link #take take()}</td>
    <td>{@link #poll(long, TimeUnit) poll(time, unit)}</td>
  </tr>
  <tr>
    <td><b>Examine</b></td>
    <td>{@link #element element()}</td>
    <td>{@link #peek peek()}</td>
    <td><em>not applicable</em></td>
    <td><em>not applicable</em></td>
  </tr>
 </table>
# Filter

> filter in tomcat web project。[referrence](https://docs.oracle.com/javaee/7/api/)

## 概述

过滤器用于在每次请求到达 servlet 前对请求进行处理。

过滤器中的 dofilter(request, response, filterChain) 方法是过滤器的核心方法。官方对于此方法说法：

> The doFilter method of the Filter is called by the container each time a request/response pair is passed through the chain due to a client request for a resource at the end of the chain. The FilterChain passed in to this method allows the Filter to pass on the request and response to the next entity in the chain.
> A typical implementation of this method would follow the following pattern:

1. Examine the request
2. Optionally wrap the request object with a custom implementation to filter content or headers for input filtering
3. Optionally wrap the response object with a custom implementation to filter content or headers for output filtering
4. Either invoke the next entity in the chain using the FilterChain object (chain.doFilter()),
   1. or not pass on the request/response pair to the next entity in the filter chain to block the request processing
5. Directly set headers on the response after invocation of the next entity in the filter chain.

- 注册： 在 web.xml 中使用 `<filter>` 与 `<filter-mapping>` 标签进行注册。过滤链的先后顺序与 web.xml 中 `<filter-mapping>` 的顺序一致。

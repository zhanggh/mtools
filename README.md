mtools
======

基于springmvc下，便捷开发插件，主要是为了快速，方便地集成到新项目中去，并提供常规性的工具以及权限控制的一套组件，提供的功能包括：<br/>
1、数据库的操作（orm）<br/>
2、http/tcp的常规处理<br/>
3、数据安全的常规加解密工具<br/>
4、通用的管理后台权限管理模块<br/>
5、通用的java消息服务工具<br/>
6、便捷的ftp工具<br/>
7、便捷的邮件工具<br/>
8、便捷的缓存开发套件<br/>
9、便捷的系统日志监控模块<br/>
10、便捷的全文搜索模块，即配即用<br/>
11、静态页面生成工具<br/>
12、便捷的系统通知服务模块<br/>
13、以及其他常规工具，如文件操作，时间操作，字符串操作，字节操作等等方面的工具<br/>

关于工具包以及配置文件的具体功能说明<br/>
一、下列各包的用处<br/>
1、com.mtools.core.plugin<br/>
插件基类，除web包下面的插件以外，其他的插件都可以继承该基类，以便具备一些基本的能力<br/>
2、com.mtools.core.plugin.annotation
该包下是一些自定义的注解，如：免登录注解@AuthLogin 免权限验证注解@AuthAccess
以及获取当前登录用户对象注解：@CurrentUser
@temporary临时字段，加了该注解的pojo字段，则代表不会进行数据库的增删改查

3、com.mtools.core.plugin.aop.aspect
切面编程例子

4、com.mtools.core.plugin.auth
登录授权验证、角色管理等

5、com.mtools.core.plugin.constant
插件用到的常量

6、com.mtools.core.plugin.db
数据库工具

7、com.mtools.core.plugin.ehcache
ehcache缓存工具

8、com.mtools.core.plugin.entity
插件用到的实体类

9、com.mtools.core.plugin.entiy.vo
用于返回页面显示的视图类

10、com.mtools.core.plugin.freemark
页面静态化工具类，用于生成静态html

11、com.mtools.core.plugin.helper
常用工具箱，如日期处理，xml处理，json处理，加解密处理等等

12、com.mtools.core.plugin.lucene
lucene搜索工具类，用于建立索引，全文搜索

13、com.mtools.core.plugin.mail
邮件工具，用于发送邮件通知

14、com.mtools.core.plugin.monitor
系统监控插件，监控系统内存，线程数等等

15、com.mtools.core.plugin.notify
通知服务，如一般邮件通知，系统异常通知

16、com.mtools.core.plugin.optlog
系统日志插件，用于记录访问痕迹

17、com.mtools.core.plugin.properties
系统参数配置

18、com.mtools.core.plugin.security
系统安全插件，用于加解密

19、com.mtools.core.plugin.solr
solr搜索客户端插件，用于建立solr索引，全文查询

20、com.mtools.core.plugin.spring.resolver
使用springresolver的方式自定义注解

21、com.mtools.core.plugin.staticres
静态文件，html生成插件

22、com.mtools.core.plugin.sys

23、com.mtools.core.plugin.task
系统自动任务插件，用于执行某些定时任务

24、com.mtools.core.plugin.web.filter
系统过滤器，如异常过滤器，缓存过滤器

25、com.mtools.core.plugin.web.interceptor
系统拦截器，如session拦截器

26、com.mtools.core.plugin.web.jcaptcha
验证码插件

27、com.mtools.core.plugin.web.listener
系统监听器 

28、com.mtools.core.plugin.web.servlet.init
系统初始化servlet，用于初始化基础信息，配置参数等

29、com.mtools.core.plugin.netty.client
高性能异步请求netty客户端
30、com.mtools.core.plugin.netty.server
高性能异步请求netty服务端<br/>
31、com.mtools.core.plugin.rmi<br/>
java rmi通讯工具<br/>
32、com.mtools.core.plugin.jms.activemq、com.mtools.core.plugin.jms.listener<br/>
java消息服务工具，用于发送jms，监听消息等<br/>

二、下列配置文件的用处
1、common-mvc.xml
springMvc公共配置文件，在mvc配置文件中引入该公共配置，该配置已经包含了一些基础拦截器的配置，视图的配置，静态资源映射等等

2、common.params.properties
系统基础属性，如邮箱的参数值，线程池的参数值

3、core-config.xml
总配置文件，该配置文件引入了该组件所有需要的配置，
当新的工程引入该组件时，直接在新工程的配置文件中引入该配置文件即可，此时不需要在引入下面的配置文件

4、log4j.properties.template
log4j配置文件模版，每一个应用应该有且只有一个log4j配置文件

5、tools-monitor.xml
aop配置文件

6、tools.beans.xml
该组件用到的基本bean配置

7、tools.cache.xml
系统缓存配置文件，对ehcache的配置

8、alipay-druid-datasource.xml
数据库的配置，事务配置等，使用了druid数据连接池
9、tools.datasource01.xml
数据库的配置，事务配置等
10、tools.datasource02.xml
数据库的配置，事务配置等

11、tools.task.xml
自动任务配置，无特殊情况，无需修改该配置文件

12、将重构

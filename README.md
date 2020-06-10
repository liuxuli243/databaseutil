# databaseutil
数据库连接工具（MySQL、Oracle、DB2、SQL Server、Sybase、PostgreSql、Informix）Java客户端，可以查看所有的表、表结构、表的数据，还可以执行sql语句，表的数据、sql语句执行结果可以导出Excel
查看函数、存储过程
查看函数、存储过程的脚本

开发此工具的目的是为了不安装数据库客户端就可以查看数据库的表结构、表中的数据、以及自定义sql的查询
代码很简单，只运用jdbc进行处理，没有使用任何框架
部署很简单，无需修改任何配置文件，直接部署即可使用


目前查看函数、存储过程的脚本只支持mysql和oracle

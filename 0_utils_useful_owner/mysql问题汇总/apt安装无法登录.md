ubuntu上使用`apt`安装的mysql,初次安装成功后,无法登录.  
查看配置文件`/etc/mysql.debian.cnf`中的默认账号.  
使用该账号登录msyql,然后更改`mysql.user`表.  
更改`root`用户的`authentication_string`和`plugin`字段.步骤如下:  
- `use mysql`  
- `update user set authentication_string = password("your_passwd") where user = 'root';`  
- `update user set plugin = "mysql_native_password" wher user = 'root';`  
- `flush privileges;#多来几次`  

之后退出,使用root和更改的密码尝试登录.  

以下是`mysql14.14`的`debian.cnf`配置文件:  
```shell
# Automatically generated for Debian scripts. DO NOT TOUCH!
[client]
host     = localhost
user     = debian-sys-maint
password = ****************
socket   = /var/run/mysqld/mysqld.sock
[mysql_upgrade]
host     = localhost
user     = debian-sys-maint
password = ****************
socket   = /var/run/mysqld/mysqld.sock
```  

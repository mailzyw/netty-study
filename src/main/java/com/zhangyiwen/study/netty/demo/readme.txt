### 基于netty的server-client例子 ###
server接收client请求，返回当前服务器时间
client读取用户输入的命令，发请求给server，接收server的响应
命令为QUIT时，则client与server断开连接，client程序结束
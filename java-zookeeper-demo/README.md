# Zookeeper  

## Install  

> Download  

http://zookeeper.apache.org/releases.html  

> Start  

```
$ tar -zxf zookeeper-3.4.6.tar.gz
$ cd zookeeper-3.4.6
$ vi conf/zoo.cfg
```  

> default conf  

```
tickTime = 2000
dataDir = /path/to/zookeeper/data
clientPort = 2181
initLimit = 5
syncLimit = 2
```  

> Start  

```
$ bin/zkServer.sh start
```

> Start CLI  

```
$ bin/zkCli.sh
```  

> Stop  

```
$ bin/zkServer.sh stop
```  


## CLI  

- <a href="#start-znodes">Start znodes</a>
- <a href="#get-data">Get data</a>
- <a href="#watch">Watch</a>
- <a href="#set-data">Set data</a>
- <a href="#children-sub-znode">Create Children / Sub-znode</a>


<div id="start-znodes"></div>  

### Start znodes

> Create   
create /path /data

```
[zk: localhost:2181(CONNECTED) 0] create /FirstZnod "Myfirstz4zookeeper-app"
Created /FirstZnod
```  

> Create Sequential znode  
create -s /path /data

```
[zk: localhost:2181(CONNECTED) 1] create -s /FirstZnode second-data
Created /FirstZnode0000000001
```  

> Create Ephemeral znode  
create -e /path /data  

```
[zk: localhost:2181(CONNECTED) 2] create -e /SecondZnode “Ephemeral-data”
Created /SecondZnode
```  

=> client connection is lost => ephemeral znode will be deleted  

<div id="get-data"></div>  

### GetData  

> Get data   
get /path  


```
[zk: localhost:2181(CONNECTED) 13] get /FirstZnod
Myfirstz4zookeeper-app
cZxid = 0x4
ctime = Tue Aug 21 20:42:59 KST 2018
mZxid = 0x4
mtime = Tue Aug 21 20:42:59 KST 2018
pZxid = 0x4
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 22
numChildren = 0
[zk: localhost:2181(CONNECTED) 9] get /FirstZnode0000000001
second-data
cZxid = 0x5
ctime = Tue Aug 21 20:43:25 KST 2018
mZxid = 0x5
mtime = Tue Aug 21 20:43:25 KST 2018
pZxid = 0x5
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 11
numChildren = 0
[zk: localhost:2181(CONNECTED) 10]
[zk: localhost:2181(CONNECTED) 11] get /SecondZnode
“Ephemeral-data”
cZxid = 0x6
ctime = Tue Aug 21 20:46:14 KST 2018
mZxid = 0x6
mtime = Tue Aug 21 20:46:14 KST 2018
pZxid = 0x6
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x100068ccaa10000
dataLength = 20
numChildren = 0
```  

<div id="watch"></div>  

### Watch  

> watch  
get /path [watch] 1  

```
[zk: localhost:2181(CONNECTED) 14] get /FirstZnod 1
Myfirstz4zookeeper-app
cZxid = 0x4
ctime = Tue Aug 21 20:42:59 KST 2018
mZxid = 0x4
mtime = Tue Aug 21 20:42:59 KST 2018
pZxid = 0x4
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 22
numChildren = 0
```

=> it will wait for znode changes in the background  

<div id="set-data"></div>

### Set data  

> Set data & get   
set /path /data  

```
[zk: localhost:2181(CONNECTED) 15] set /SecondZnode Data-updated
cZxid = 0x6
ctime = Tue Aug 21 20:46:14 KST 2018
mZxid = 0x7
mtime = Tue Aug 21 20:55:22 KST 2018
pZxid = 0x6
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x100068ccaa10000
dataLength = 12
numChildren = 0
```


<div id="children-sub-znode"></div>  

### Create Children / Sub-znode  

> Create children  
create /parent/path/subnode/path /data  

```
[zk: localhost:2181(CONNECTED) 18] create /FirstZnod/Child1 firstchildren
Created /FirstZnod/Child1
[zk: localhost:2181(CONNECTED) 20] create /FirstZnod/Child2 secondchildren  
Created /FirstZnod/Child2
```  

> List children  
ls /path  

```
[zk: localhost:2181(CONNECTED) 24] ls /FirstZnod
[Child2, Child1]
```  

> Stat  
stat /path  

```
[zk: localhost:2181(CONNECTED) 25] stat /FirstZnod
cZxid = 0x4
ctime = Tue Aug 21 20:42:59 KST 2018
mZxid = 0x8
mtime = Tue Aug 21 20:56:16 KST 2018
pZxid = 0xc
cversion = 2
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 12
numChildren = 2
```  

> Remove   
rmr /path  

```
[zk: localhost:2181(CONNECTED) 26] rmr /FirstZnod
[zk: localhost:2181(CONNECTED) 27] get /FirstZnod
Node does not exist: /FirstZnod
```


---  

## How to use zookeeper in java  

1. Connect to the Zookeeper ensemble.  
=> assign a Session ID for the client  

2. Send heartbeats to the server periodically  

3. Get / Set the znodes as long as a session ID is active  

4. Disconnect from the ZooKeeper ensemble, once all the tasks are completed  

## Docker java api demo  

- ## Listen tcp port  

> create conf file to override docker.service file 

```aidl
$ mkdir -p /etc/systemd/system/docker.service.d
$ vi /etc/systemd/system/docker.service.d/startup_options.conf
```  

> override properties  

```aidl
[Service]
ExecStart=
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2376 -H unix:///var/run/docker.sock
```  

> reload docker  

```aidl
$ systemctl daemon-reload
$ systemctl restart docker
```

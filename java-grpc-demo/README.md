## gRPC demo  

- ## <a href="src/main/java/demo/sample1">Sample1</a>  
; how to use proto buf & simple rpc  

- ## <a href="src/main/java/demo/routeguide">RouteGuide</a>  
; how to use gRpc (simple rpc, server-to-client stream rpc, client-to-server rpc, bidirectional streaming)  

- ## <a href="src/main/java/demo/tls">gRpc with TLS</a>  
; how to use gRpc with TLS (no mutual auth, mutual auth) 

## Getting started  

> compile  

```aidl
$ mvn clean compile  
// then see target/demo/{routeguid | sample1 | tls}/proto/*.class
```  

> change running enum  

```aidl
// SampleRunner runner = SampleRunner.SAMPLE1;
// SampleRunner runner = SampleRunner.ROUTE_GUIDE;
SampleRunner runner = SampleRunner.TLS;
```  

> running main method in DemoApplication  

```aidl

```

   

  



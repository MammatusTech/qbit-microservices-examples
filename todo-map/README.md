You can find the tutorial for this code here [Tutorial on QBit Microservices Java Lib using REST](https://github.com/MammatusTech/qbit-microservices-examples/wiki/QBit-Microservices-Java-Lib-RESTful).



#### Other admin endpoints
```


$  curl http://localhost:7777/__admin/ok
true

$  curl http://localhost:7777/__admin/system/property/
{"java.runtime.name":"Java(TM) SE Runtime Environment",...}

$  curl http://localhost:7777/__admin/system/property?p=sun.cpu.endian
"little"

$ curl http://localhost:7777/__admin/env/variable/
{"PATH":"/usr/local/bin:/usr/bin:/bin:/usr/sbin:...",...}


$ curl http://localhost:7777/__admin/env/variable?v=HOME
//NOT WORKING

$ curl http://localhost:7777/__admin/available-processors
8

$ curl http://localhost:7777/__admin/memory/free
190226344


$ curl http://localhost:7777/__admin/memory/total
257425408


$ curl http://localhost:7777/__admin/memory/max
3817865216

$ curl http://localhost:7777/__admin/memory/heap/usage
69899384

$ curl http://localhost:7777/__admin/memory/non-heap/usage
22452280

$ curl http://localhost:7777/__admin/thread/count
33

$ curl http://localhost:7777/__admin/os/load-average
4.651855

$ curl http://localhost:7777/__admin/os/name
"Mac OS X"


$ curl http://localhost:7777/__admin/os/arch
"x86_64"

$ curl http://localhost:7777/__admin/os/version
"10.10.5"

$ curl http://localhost:7777/__admin/runtime/classpath
"...:..."


$ curl http://localhost:7777/__admin/runtime/boot-classpath
"/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/lib/resources.jar:..."


$ curl http://localhost:7777/__admin/runtime/vm-version
"25.45-b02"


$  curl http://localhost:7777/__admin/runtime/vm-vendor
"Oracle Corporation"

$ curl http://localhost:7777/__admin/runtime/lib-path
"/Users/rick/Library/Java/Extensions:/Library/Java/Extensions:..."


$ curl http://localhost:7777/__admin/runtime/spec-name
"Java Virtual Machine Specification"

$ curl http://localhost:7777/__admin/runtime/spec-version
"1.8"

$ curl http://localhost:7777/__admin/runtime/spec-vendor
"Oracle Corporation"


```

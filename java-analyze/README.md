# Java Analyze

<div id="index"></div>

### Index  

- <a href="#java.util.HashMap"> java/util/HashMap </a>  


---  

<div id="java.util.HashMap"></div>  

#### java.util.HashMap  

> hash 계산 in java8  
- ref : http://d2.naver.com/helloworld/831311  

```
...
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

...

final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    if ((p = tab[i = (n - 1) & hash]) == null)  
    ...
}

...  
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```  

=> 결론적으로 보조 해시 함수(supplement hash function)라고 볼 수 있음
1. java8에서 해시 충돌이 많이 발생하면 링크드 리스트 => 트리 사용하므로  
충돌 시 발생할 수 있는 성능 문제가 완화  
2. 최근의 해시 함수는 균등 분포가 잘 되게 만들어지는 경향이 많음  
 
  

```
public int indexOf(Object key) {
  int h;  
  // 상위 16비트만 XOR 연산
  h = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  // (length -1)과 hash 보조 함수 값의 논리곱으로 인덱스 결정
  return (table.length - 1) & h;
}

```

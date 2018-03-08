# Java Analyze

<div id="index"></div>

### Index  

- <a href="#java.util.HashMap"> java/util/HashMap </a>  


---  

<div id="java.util.HashMap"></div>  

#### java.util.HashMap  

> hash 계산 및 버킷 table 계산 in java8: java.util.HashMap
- ref : http://d2.naver.com/helloworld/831311  

```
final Node<K,V>[] resize() {
  ...
  else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
  ...
}
...
static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```  

1. 위의 tableSizeFor(int cap)을 통해 초기 capacity 설정 시, 설정 값보다  
크거나 같은 2^n 값으로 할당  
```aidl
1 => 1 , 2 => 2 , 3 => 4 , 4 => 4 ...
```   
2. resize()를 통해 newCap = oldCap << 1를 통해 기존 capacity의 2배로 확장  
(결록적으로 해시 버킷은 2^x 값 들로 배열을 할당)  

=> index = instance.hashCode() % M을 계산할 때 하위 x비트만 사용하게 되므로  
해시 함수가 32비트 영역을 고르게 사용하여도, 해시 충돌이 쉽게 발생  
=> 보조 해시 함수가 필요

```
...
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
...  
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
...
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    if ((p = tab[i = (n - 1) & hash]) == null)  
    ...
}
```  

=> 보조 해시 함수(supplement hash function)를 이용하여 상위 16 비트만 XOR 연산  

```aidl
이유는 네이버의 Hellow world에 아래와 같이 적혀 있음  
1. java8에서 해시 충돌이 많이 발생하면 링크드 리스트 => 트리 사용하므로  
충돌 시 발생할 수 있는 성능 문제가 완화  
2. 최근의 해시 함수는 균등 분포가 잘 되게 만들어지는 경향이 많음
```  

=> 결과적으로 HashMap의 버킷의 index는 아래와 같이 구할 수 있음.  

```
public int indexOf(Object key) {
  int h;  
  // 상위 16비트만 XOR 연산
  h = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  // (length -1)과 hash 보조 함수 값의 논리곱으로 인덱스 결정
  return (table.length - 1) & h;
}
```  

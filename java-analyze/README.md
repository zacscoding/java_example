# Java Analyze

<div id="index"></div>

### Index  

- <a href="#java.util.HashMap"> java/util/HashMap </a>  
- <a href="#java.util.ArrayList">java/util/ArrayList</a>


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


---  

<div id="java.util.ArrayList"></div>  

#### java.util.ArrayList in java8  

> 동적 배열  

```
...
private static final int DEFAULT_CAPACITY = 10;
...
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

...

public ArrayList() {
    // default 생성자로 인스턴스 생성시 empty array 할당
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}  

...

public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}

...

private void ensureCapacityInternal(int minCapacity) {
    // empty array이면 minCapacity = Math.max(10,1) = 10
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}

...

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

...

private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}

```  

1. Default 생성자로 인스턴스를 생성 + add() 호출  
=> ensureCapacityInternal() 메소드에서 ensureExplicitCapacity()의 매개변수 minCapacity를  
10으로 배열 할당  
=> ensureExplicitCapacity()의 if(10 - 0 > 0) == true이므로 grow  
=> newCapacity == 0 + (0 >> 1) == 0 이므로 if(newCapacity - minCapacity) == true  
=> 사이즈가 10인 배열로 할당  

2. 현재 배열의 elementData.length == 10, add() 10번째 호출  
=> add() => ensureExplicitCapacity(11) 호출  
=> ensureExplicitCapacity() 메소드에서 minCapacity(11) - elts.length(10) > 0 므로 grow(11) 호출  
=> oldCapacity는 10, newCapacity = 15이므로 위의 if문은 모두 false & length가 15인 배열로 재할당  
=> ArrayList의 동적 배열은 newCapacity = (oldCapacity) + (oldCapacity >> 1) 만큼 증가  
```
i.e 10 => 15 || 11 => 16 || 12 => 18 || 13 => 19 || 14 => 21 || 15 => 22 || 16 => 24 || 17 => 25 || 18 => 27 || 19 => 28 ...
기본 생성자를 이용하면,  
10 => 15 || 15 => 22 || 22 => 33 || 33 => 49 || 49 => 73 || 73 => 109 || 109 => 163 || 163 => 244 || 244 => 366 || 366 => 549 ...
```  

3. 재할당 시 상수(M)만큼 늘리면? ```e.g : newCapacity = oldCapacity + M```  
ref : 알고리즘 문제해결 전략 책  
가정 : 텅빈 배열로 시작해 N번 add() 호출 & 동적 할당 M(상수)씩  
=> 재할당의 수 K == O(N/M)  
-> M이 상수므로 N이 무한히 커지면, K == O(N)  
=> 재할당마다 복사하는 수 M, 2M, ... KM  
-> (K)(K+1)(M) / 2 == O(K^2) == O(N^2)  
결과적으로 N번의 add()하는데 O(N^2)의 시간이 걸림  
(즉 평균적으로 O(N^2)/N == O(N))  

4. 상수 시간에 add()를 구현하기 위해서 현재 원소에 비례해서 배열을 증가  
have to proof of (An+1 = An + (An / 2))























<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>  
---


# example

> Person class

```aidl
public class Person {
  private int age;
  private String name;
  private Address address;
  private List<String> hobbies;  
  private Company company;
```  

> Address class   

```aidl
public class Address {
  private String zipCode;
  private String city;    
}

```

> Company class  

```aidl
public class Company {
  private String name;
  private int salary;  
}
```
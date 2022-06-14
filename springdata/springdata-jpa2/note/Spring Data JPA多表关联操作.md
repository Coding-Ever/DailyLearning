

#### 首先配置关联关系
```java
@Entity     // 作为hibernate实体类
@Table(name = "cst_customer")   // 映射的表明
@Data
public class Customer {
    /*...省略其他代码...*/

    @OneToOne   // 单向关联  一对一
    @JoinColumn(name = "account_id")    // 设置外键的字段名
    private Account account;
}
```
关联操作@OneToOne注解设置：
```java
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {
    Class targetEntity() default void.class;
    /*cascade 设置关联操作
        ALL     所有持久化操作都会执行关联操作
        PERSIST 只有插入才会执行关联操作
        MERGE   只有修改才会执行关联操作
        REMOVE  只有删除才会执行关联操作
    */
    CascadeType[] cascade() default {};
    /*fetch   设置是否懒加载
        EAGER   立即加载（默认）
        LAZY    懒加载（直到用到对象时才会进行查询，可以提高查询效率，因为不是所有的关联对象都会用到）
    */
    FetchType fetch() default FetchType.EAGER;
    /*optional    限制关联的对象是否可以为null（默认为true）*/
    boolean optional() default true;
    /*mappedBy    将外键约束指向另一方维护。值 = 另一方的关联属性名*/
    String mappedBy() default "";
    /*orphanRemoval   关联移除（通常在修改的时候会用到）
        一旦把关联的数据设置为null，或者修改为其他的关联数据，如果想删除关联数据，就可以设置为true
        默认为false
    * */
    boolean orphanRemoval() default false;
}
```
```java
package com.ever.pojo;
/*引入lombok可以使用@Data注解，省去@Getter()/@Setter/构造器/toString()等方法*/
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "tb_account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
}
```
#### 


### 乐观锁
hibernate
防并发修改
```java
private @Version Long version;
```
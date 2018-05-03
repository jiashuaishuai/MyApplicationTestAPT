# Java注解

## 概念
* 官方翻译：java注解用于为java代码提供元数据。作为元数据，注解不直接影响你的代码执行，但也有一些类型的注解实际上可以用于着一目的，java注解是从java5开始添加到java的。
* 个人简单理解：注解就是标签，

## 语法
注解通过@interface关键字定义

```java
public @interface TestAnnotation{}
```
## 元注解
是一种基本注解，但它能够应用到其他注解上面。

### @Retention(保留期)
当@Retention应用到另一个注解上的时候，说明这个注解的存活时间如下值：

* RetentionPolicy.SOURCE 注解只在源码阶段保留，编译器进行编译时他将丢失忽略；
* RetentionPolicy.CLASS 注解只被保留到编译运行时，不会加载到JVM内存中
* RetentionPolicy.RUNTIME 注解可以保留到程序运行时，他会被加载到JVM中，可以再程序运行时获取他们。

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation{}
```

### @Documented(记录)
这个元注解主要是能够将注解中的元素包含在javadoc文档中

### @Target(目标)
目标，也就是说这个注解运用指定的地方如下值：

* ElementType.ANNOTATION_TYPE 可以给一个注解进行注解
* ElementType.CONSTRUCTOR 可以给一个构造函数进行注解
* ElementType.FIELD 可以给一个属性进行注解
* ElementType.LOCAL_VARIABLE 可以给一个全局变量进行注解
* ElementType.METHOD 可以给方法进行注解
* ElementType.PACKAGE 可以给一个包进行注解
* ElementType.PARAMETER 可以给一个方法内的参数进行注解
* ElementType.TYPE 可以给一个类型进行注解。类、接口、枚举

### @Inherited(遗传、继承)
并不是指注解本身可以继承，而是如果一个超类被@Inherited注解过的注解进行注解的话，那么他的子类没有任何注解应用的话，那么这个子类就继承了超类的注解。

```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface Test{}

@Test
public class A{}

public class B extends A{}
```

### @Repeatable(重复)
Repeatable是Java1.8才加加进来的，通常是注解的值可以同时取多个。
如：一个人是程序员，产品，UI多个职业

```java
@interface Persons{
    Person[] value();
}

@Repeatable(Persons.class)
public class Person{
    String role default"";
}

@Person("产品")
@Person("程序员")
@person("UI")
public class SuperMan{}
```

@Repeatable 注解了一个Person。而@Repeatable括号中的类相当于一个容器注解。其本身也是一个注解，但是按照规定，他必须有一个value属性，属性类型是被@Repeatable注解过的注解数组，注意是数组




## 注解的提取
想要正确检阅注解，离不开一个手段，呢就是反射。

### 注解与反射。
注解通过反射获取。首先可以通过Class对象的isAnnotationPresent()方法判断它是否应用了某个注解

```java
public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass){}
```

然后通过getAnnotation()方法获取Annotation对象

```java
public <A extends Annotation> A getAnnotation(Class<A> annotationClass){}
```
或者是getAnnotations()方法。

```java
public Annotation[] getAnnotations(){}
```


## APT

全称：annotation process tool 注解处理工具
apt处理要素：AbstarctProcess、代码处理（javaPoet）、处理器注册（AutoService）、apt

大致流程图：
![](MyApplicationTestAPT/7E35D985-9B12-4C2D-9A07-AE4F92786D94.png)


核心类：AbstractProcessor自定义注解处理器，必须腹泻process()方法，
通常：

```java
/** 
 * 每一个注解处理器类都必须有一个空的构造函数，默认不写就行; 
 * 建议：处理兼容问题，建议使用重载getSupportedAnnotationTypes()和getSupportedSourceVersion()方法
 * 代替@SupportedAnnotationTypes和@SupportedSurceVersion
 */  
 @AutoService(Processor.class)//注意这里导包javax下的Processor  
public class MyProcessor extends AbstractProcessor {  
  
    /** 
     * init()方法会被注解处理工具调用，并输入ProcessingEnviroment参数。 
     * ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer 
     * @param processingEnv 提供给 processor 用来访问工具框架的环境 
     */  
    @Override  
    public synchronized void init(ProcessingEnvironment processingEnv) {  
        super.init(processingEnv);  
    }  
  
    /** 
     * 这相当于每个处理器的主函数main()，你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。 
     * 输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素 
     * @param annotations   请求处理的注解类型 
     * @param roundEnv  有关当前和以前的信息环境 
     * @return  如果返回 true，则这些注解已声明并且不要求后续 Processor 处理它们； 
     *          如果返回 false，则这些注解未声明并且可能要求后续 Processor 处理它们 
     */  
    @Override  
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {  
        return false;  
    }  
  
    /** 
     * 这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称 
     * @return  注解器所支持的注解类型集合，如果没有这样的类型，则返回一个空集合 
     */  
    @Override  
    public Set<String> getSupportedAnnotationTypes() {  
        Set<String> annotataions = new LinkedHashSet<String>();  
        annotataions.add(MyAnnotation.class.getCanonicalName());  
        return annotataions;  
    }  
  
    /** 
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()，默认返回SourceVersion.RELEASE_6 
     * @return  使用的Java版本 
     */  
    @Override  
    public SourceVersion getSupportedSourceVersion() {  
        return SourceVersion.latestSupported();  
    }  
} 
```

### 常用接口

#### Element 和 TypeMirror

**Element：**
表示一个被注解的java程序元素，比如包、类、方法、字段，等多种元素种类，具体看getKind()方法中所指代的种类，
有以下几种子接口：

 ExecutableElement|表示某个(类或者接口)的方法、构造函数或初始化程序（静态或实例）包括注解类型元素；
 ------|------
 PackageElement|表示一个包程序元素；
 TypeElement|表示一个类或接口程序元素；
 VariableElement|表示一个字段、enum常量、(方法或构造方法)参数、局部变量或异常参数
 TypeParameterElement|表示一般类、接口、方法或构造方法元素的泛型参数

```java
package com.example;        // PackageElement  
  
import java.util.List;  
  
public class Sample         // TypeElement  
        <T extends List> {  // TypeParameterElement  
  
    private int num;        // VariableElement  
    String name;            // VariableElement  
  
    public Sample() {}      // ExecuteableElement  
  
    public void setName(    // ExecuteableElement  
            String name     // VariableElement  
            ) {}  
}  
```

**TypeElement**

上面提到Element具体对应的类型元素。通过Element.getKind方法返回ElementKind枚举值来表示具体种类，这里不贴代码了

**TypeMirror**
 
 表示的是java编程语言中的类型，比如：
 String name
 在Element代表的是源代码上的元素字段(FIELD),TypeMirror代表的的是(DECLARED)Element对应Java编程语言中的类型
 
 通过asType获得TYpeMirror，后getKind()返回TypeKind枚举值
 
 **Types**
  用来处理TypeMirror的工具，
  
  **Elements**
  用来处理Element的工具
  
  **Messager**
  日志打印
  
  获取方法：
    
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        Types types = processingEnv.getTypeUtils();
        messager.printMessage(Diagnostic.Kind.NOTE, "初始化。。。。。");
    }


日志查看：Gradle Console


**参考重要博客：**

理论三篇
https://blog.csdn.net/github_35180164/article/details/52171135 

实战一篇
https://blog.csdn.net/u011315960/article/details/64439575  





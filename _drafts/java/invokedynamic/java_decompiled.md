# decompile lambda codes

> 反编译lambda 代码，用以查看 java8 动态调用实现。

## original java codes

```java
public class OptionalTest {
  Function<Integer, String> f = (Integer i) -> i.toString();
  Consumer<Integer> c = (Integer i) -> {};
}
```

```java
Classfile /H:/programming/projects/DesignPattern/target/classes/java8inaction/optional/OptionalTest.class
  Last modified 2020-1-3; size 1552 bytes
  MD5 checksum c7b547e731aa23fab86d69e7e85c0224
  Compiled from "OptionalTest.java"
public class java8inaction.optional.OptionalTest
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #8.#31         // java/lang/Object."<init>":()V
   #2 = InvokeDynamic      #0:#37         // #0:apply:()Ljava/util/function/Function;
   #3 = Fieldref           #7.#38         // java8inaction/optional/OptionalTest.f:Ljava/util/function/Function;
   #4 = InvokeDynamic      #1:#42         // #1:accept:()Ljava/util/function/Consumer;
   #5 = Fieldref           #7.#43         // java8inaction/optional/OptionalTest.c:Ljava/util/function/Consumer;
   #6 = Methodref          #44.#45        // java/lang/Integer.toString:()Ljava/lang/String;
   #7 = Class              #46            // java8inaction/optional/OptionalTest
   #8 = Class              #47            // java/lang/Object
   #9 = Utf8               f
  #10 = Utf8               Ljava/util/function/Function;
  #11 = Utf8               Signature
  #12 = Utf8               Ljava/util/function/Function<Ljava/lang/Integer;Ljava/lang/String;>;
  #13 = Utf8               c
  #14 = Utf8               Ljava/util/function/Consumer;
  #15 = Utf8               Ljava/util/function/Consumer<Ljava/lang/Integer;>;
  #16 = Utf8               <init>
  #17 = Utf8               ()V
  #18 = Utf8               Code
  #19 = Utf8               LineNumberTable
  #20 = Utf8               LocalVariableTable
  #21 = Utf8               this
  #22 = Utf8               Ljava8inaction/optional/OptionalTest;
  #23 = Utf8               lambda$new$1
  #24 = Utf8               (Ljava/lang/Integer;)V
  #25 = Utf8               i
  #26 = Utf8               Ljava/lang/Integer;
  #27 = Utf8               lambda$new$0
  #28 = Utf8               (Ljava/lang/Integer;)Ljava/lang/String;
  #29 = Utf8               SourceFile
  #30 = Utf8               OptionalTest.java
  #31 = NameAndType        #16:#17        // "<init>":()V
  #32 = Utf8               BootstrapMethods
  #33 = MethodHandle       #6:#48         // invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #34 = MethodType         #49            //  (Ljava/lang/Object;)Ljava/lang/Object;
  #35 = MethodHandle       #6:#50         // invokestatic java8inaction/optional/OptionalTest.lambda$new$0:(Ljava/lang/Integer;)Ljava/lang/String;
  #36 = MethodType         #28            //  (Ljava/lang/Integer;)Ljava/lang/String;
  #37 = NameAndType        #51:#52        // apply:()Ljava/util/function/Function;
  #38 = NameAndType        #9:#10         // f:Ljava/util/function/Function;
  #39 = MethodType         #53            //  (Ljava/lang/Object;)V
  #40 = MethodHandle       #6:#54         // invokestatic java8inaction/optional/OptionalTest.lambda$new$1:(Ljava/lang/Integer;)V
  #41 = MethodType         #24            //  (Ljava/lang/Integer;)V
  #42 = NameAndType        #55:#56        // accept:()Ljava/util/function/Consumer;
  #43 = NameAndType        #13:#14        // c:Ljava/util/function/Consumer;
  #44 = Class              #57            // java/lang/Integer
  #45 = NameAndType        #58:#59        // toString:()Ljava/lang/String;
  #46 = Utf8               java8inaction/optional/OptionalTest
  #47 = Utf8               java/lang/Object
  #48 = Methodref          #60.#61        // java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #49 = Utf8               (Ljava/lang/Object;)Ljava/lang/Object;
  #50 = Methodref          #7.#62         // java8inaction/optional/OptionalTest.lambda$new$0:(Ljava/lang/Integer;)Ljava/lang/String;
  #51 = Utf8               apply
  #52 = Utf8               ()Ljava/util/function/Function;
  #53 = Utf8               (Ljava/lang/Object;)V
  #54 = Methodref          #7.#63         // java8inaction/optional/OptionalTest.lambda$new$1:(Ljava/lang/Integer;)V
  #55 = Utf8               accept
  #56 = Utf8               ()Ljava/util/function/Consumer;
  #57 = Utf8               java/lang/Integer
  #58 = Utf8               toString
  #59 = Utf8               ()Ljava/lang/String;
  #60 = Class              #64            // java/lang/invoke/LambdaMetafactory
  #61 = NameAndType        #65:#69        // metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #62 = NameAndType        #27:#28        // lambda$new$0:(Ljava/lang/Integer;)Ljava/lang/String;
  #63 = NameAndType        #23:#24        // lambda$new$1:(Ljava/lang/Integer;)V
  #64 = Utf8               java/lang/invoke/LambdaMetafactory
  #65 = Utf8               metafactory
  #66 = Class              #71            // java/lang/invoke/MethodHandles$Lookup
  #67 = Utf8               Lookup
  #68 = Utf8               InnerClasses
  #69 = Utf8               (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #70 = Class              #72            // java/lang/invoke/MethodHandles
  #71 = Utf8               java/lang/invoke/MethodHandles$Lookup
  #72 = Utf8               java/lang/invoke/MethodHandles
{
  java.util.function.Function<java.lang.Integer, java.lang.String> f;
    descriptor: Ljava/util/function/Function;
    flags:
    Signature: #12                          // Ljava/util/function/Function<Ljava/lang/Integer;Ljava/lang/String;>;

  java.util.function.Consumer<java.lang.Integer> c;
    descriptor: Ljava/util/function/Consumer;
    flags:
    Signature: #15                          // Ljava/util/function/Consumer<Ljava/lang/Integer;>;

  public java8inaction.optional.OptionalTest();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: invokedynamic #2,  0              // InvokeDynamic #0:apply:()Ljava/util/function/Function;
        10: putfield      #3                  // Field f:Ljava/util/function/Function;
        13: aload_0
        14: invokedynamic #4,  0              // InvokeDynamic #1:accept:()Ljava/util/function/Consumer;
        19: putfield      #5                  // Field c:Ljava/util/function/Consumer;
        22: return
      LineNumberTable:
        line 10: 0
        line 12: 4
        line 13: 13
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      23     0  this   Ljava8inaction/optional/OptionalTest;
}
SourceFile: "OptionalTest.java"
InnerClasses:
     public static final #67= #66 of #70; //Lookup=class java/lang/invoke/MethodHandles$Lookup of class java/lang/invoke/MethodHandles
BootstrapMethods:
  0: #33 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #34 (Ljava/lang/Object;)Ljava/lang/Object;
      #35 invokestatic java8inaction/optional/OptionalTest.lambda$new$0:(Ljava/lang/Integer;)Ljava/lang/String;
      #36 (Ljava/lang/Integer;)Ljava/lang/String;
  1: #33 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #39 (Ljava/lang/Object;)V
      #40 invokestatic java8inaction/optional/OptionalTest.lambda$new$1:(Ljava/lang/Integer;)V
      #41 (Ljava/lang/Integer;)V
```

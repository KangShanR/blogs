---
layout: "post"
title: "DynamicInvoke Class be compiled"
date: "2019-12-26 18:30"
---


Classfile /D:/mine/github/DesignPattern/target/classes/java8inaction/lazycompute/LazyList.class
  Last modified 2019-12-26; size 4620 bytes
  MD5 checksum ce31dbefa06a39fde443d4ffebe2ac87
  Compiled from "LazyList.java"
public class java8inaction.lazycompute.LazyList<T extends java.lang.Object> extends java.lang.Object
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
    #1 = Fieldref           #3.#88        // java8inaction/lazycompute/LazyList.tailSupplier:Ljava/util/function/Supplier;
    #2 = InterfaceMethodref #89.#90       // java/util/function/Supplier.get:()Ljava/lang/Object;
    #3 = Class              #91           // java8inaction/lazycompute/LazyList
    #4 = Methodref          #23.#92       // java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
    #5 = InvokeDynamic      #0:#98        // #0:get:(I)Ljava/util/function/Supplier;
    #6 = Methodref          #3.#99        // java8inaction/lazycompute/LazyList."<init>":(Ljava/lang/Object;Ljava/util/function/Supplier;)V
    #7 = Methodref          #3.#100       // java8inaction/lazycompute/LazyList.from2:(I)Ljava8inaction/lazycompute/LazyList;
    #8 = Fieldref           #3.#101       // java8inaction/lazycompute/LazyList.head:Ljava/lang/Object;
    #9 = Methodref          #3.#102       // java8inaction/lazycompute/LazyList.getHead:()Ljava/lang/Object;
   #10 = InterfaceMethodref #103.#104     // java/util/function/Predicate.test:(Ljava/lang/Object;)Z
   #11 = InvokeDynamic      #1:#106       // #1:get:(Ljava8inaction/lazycompute/LazyList;Ljava/util/function/Predicate;)Ljava/util/function/Supplier;
   #12 = Methodref          #3.#107       // java8inaction/lazycompute/LazyList.getTail:()Ljava8inaction/lazycompute/LazyList;
   #13 = Methodref          #3.#108       // java8inaction/lazycompute/LazyList.filter:(Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
   #14 = InvokeDynamic      #2:#110       // #2:get:(Ljava8inaction/lazycompute/LazyList;)Ljava/util/function/Supplier;
   #15 = Fieldref           #111.#112     // java/lang/System.out:Ljava/io/PrintStream;
   #16 = Methodref          #113.#114     // java/io/PrintStream.println:(Ljava/lang/Object;)V
   #17 = Methodref          #3.#115       // java8inaction/lazycompute/LazyList.printAll:(Ljava8inaction/lazycompute/LazyList;)V
   #18 = Methodref          #3.#116       // java8inaction/lazycompute/LazyList.from:(I)Ljava8inaction/lazycompute/LazyList;
   #19 = Methodref          #3.#117       // java8inaction/lazycompute/LazyList.primes:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
   #20 = Methodref          #24.#118      // java/lang/Object."<init>":()V
   #21 = InvokeDynamic      #3:#122       // #3:test:(Ljava8inaction/lazycompute/LazyList;)Ljava/util/function/Predicate;
   #22 = Methodref          #23.#123      // java/lang/Integer.intValue:()I
   #23 = Class              #124          // java/lang/Integer
   #24 = Class              #125          // java/lang/Object
   #25 = Utf8               head
   #26 = Utf8               Ljava/lang/Object;
   #27 = Utf8               Signature
   #28 = Utf8               TT;
   #29 = Utf8               tailSupplier
   #30 = Utf8               Ljava/util/function/Supplier;
   #31 = Utf8               Ljava/util/function/Supplier<Ljava8inaction/lazycompute/LazyList<TT;>;>;
   #32 = Utf8               getTail
   #33 = Utf8               ()Ljava8inaction/lazycompute/LazyList;
   #34 = Utf8               Code
   #35 = Utf8               LineNumberTable
   #36 = Utf8               LocalVariableTable
   #37 = Utf8               this
   #38 = Utf8               Ljava8inaction/lazycompute/LazyList;
   #39 = Utf8               LocalVariableTypeTable
   #40 = Utf8               Ljava8inaction/lazycompute/LazyList<TT;>;
   #41 = Utf8               ()Ljava8inaction/lazycompute/LazyList<TT;>;
   #42 = Utf8               from
   #43 = Utf8               (I)Ljava8inaction/lazycompute/LazyList;
   #44 = Utf8               i
   #45 = Utf8               I
   #46 = Utf8               (I)Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;
   #47 = Utf8               from2
   #48 = Utf8               Deprecated
   #49 = Utf8               RuntimeVisibleAnnotations
   #50 = Utf8               Ljava/lang/Deprecated;
   #51 = Utf8               filter
   #52 = Utf8               (Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
   #53 = Utf8               predicate
   #54 = Utf8               Ljava/util/function/Predicate;
   #55 = Utf8               Ljava/util/function/Predicate<TT;>;
   #56 = Utf8               StackMapTable
   #57 = Class              #91           // java8inaction/lazycompute/LazyList
   #58 = Utf8               (Ljava/util/function/Predicate<TT;>;)Ljava8inaction/lazycompute/LazyList<TT;>;
   #59 = Utf8               primes
   #60 = Utf8               (Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
   #61 = Utf8               prime
   #62 = Utf8               Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;
   #63 = Utf8               (Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;)Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;
   #64 = Utf8               printAll
   #65 = Utf8               (Ljava8inaction/lazycompute/LazyList;)V
   #66 = Utf8               list
   #67 = Utf8               <T:Ljava/lang/Object;>(Ljava8inaction/lazycompute/LazyList<TT;>;)V
   #68 = Utf8               main
   #69 = Utf8               ([Ljava/lang/String;)V
   #70 = Utf8               args
   #71 = Utf8               [Ljava/lang/String;
   #72 = Utf8               <init>
   #73 = Utf8               (Ljava/lang/Object;Ljava/util/function/Supplier;)V
   #74 = Utf8               (TT;Ljava/util/function/Supplier<Ljava8inaction/lazycompute/LazyList<TT;>;>;)V
   #75 = Utf8               getHead
   #76 = Utf8               ()Ljava/lang/Object;
   #77 = Utf8               ()TT;
   #78 = Utf8               lambda$primes$3
   #79 = Utf8               lambda$null$2
   #80 = Utf8               (Ljava8inaction/lazycompute/LazyList;Ljava/lang/Integer;)Z
   #81 = Utf8               n
   #82 = Utf8               Ljava/lang/Integer;
   #83 = Utf8               lambda$filter$1
   #84 = Utf8               lambda$from$0
   #85 = Utf8               <T:Ljava/lang/Object;>Ljava/lang/Object;
   #86 = Utf8               SourceFile
   #87 = Utf8               LazyList.java
   #88 = NameAndType        #29:#30       // tailSupplier:Ljava/util/function/Supplier;
   #89 = Class              #126          // java/util/function/Supplier
   #90 = NameAndType        #127:#76      // get:()Ljava/lang/Object;
   #91 = Utf8               java8inaction/lazycompute/LazyList
   #92 = NameAndType        #128:#129     // valueOf:(I)Ljava/lang/Integer;
   #93 = Utf8               BootstrapMethods
   #94 = MethodHandle       #6:#130       // invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
   #95 = MethodType         #76           //  ()Ljava/lang/Object;
   #96 = MethodHandle       #6:#131       // invokestatic java8inaction/lazycompute/LazyList.lambda$from$0:(I)Ljava8inaction/lazycompute/LazyList;
   #97 = MethodType         #33           //  ()Ljava8inaction/lazycompute/LazyList;
   #98 = NameAndType        #127:#132     // get:(I)Ljava/util/function/Supplier;
   #99 = NameAndType        #72:#73       // "<init>":(Ljava/lang/Object;Ljava/util/function/Supplier;)V
  #100 = NameAndType        #47:#43       // from2:(I)Ljava8inaction/lazycompute/LazyList;
  #101 = NameAndType        #25:#26       // head:Ljava/lang/Object;
  #102 = NameAndType        #75:#76       // getHead:()Ljava/lang/Object;
  #103 = Class              #133          // java/util/function/Predicate
  #104 = NameAndType        #134:#135     // test:(Ljava/lang/Object;)Z
  #105 = MethodHandle       #7:#136       // invokespecial java8inaction/lazycompute/LazyList.lambda$filter$1:(Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
  #106 = NameAndType        #127:#137     // get:(Ljava8inaction/lazycompute/LazyList;Ljava/util/function/Predicate;)Ljava/util/function/Supplier;
  #107 = NameAndType        #32:#33       // getTail:()Ljava8inaction/lazycompute/LazyList;
  #108 = NameAndType        #51:#52       // filter:(Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
  #109 = MethodHandle       #6:#138       // invokestatic java8inaction/lazycompute/LazyList.lambda$primes$3:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
  #110 = NameAndType        #127:#139     // get:(Ljava8inaction/lazycompute/LazyList;)Ljava/util/function/Supplier;
  #111 = Class              #140          // java/lang/System
  #112 = NameAndType        #141:#142     // out:Ljava/io/PrintStream;
  #113 = Class              #143          // java/io/PrintStream
  #114 = NameAndType        #144:#145     // println:(Ljava/lang/Object;)V
  #115 = NameAndType        #64:#65       // printAll:(Ljava8inaction/lazycompute/LazyList;)V
  #116 = NameAndType        #42:#43       // from:(I)Ljava8inaction/lazycompute/LazyList;
  #117 = NameAndType        #59:#60       // primes:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
  #118 = NameAndType        #72:#146      // "<init>":()V
  #119 = MethodType         #135          //  (Ljava/lang/Object;)Z
  #120 = MethodHandle       #6:#147       // invokestatic java8inaction/lazycompute/LazyList.lambda$null$2:(Ljava8inaction/lazycompute/LazyList;Ljava/lang/Integer;)Z
  #121 = MethodType         #148          //  (Ljava/lang/Integer;)Z
  #122 = NameAndType        #134:#149     // test:(Ljava8inaction/lazycompute/LazyList;)Ljava/util/function/Predicate;
  #123 = NameAndType        #150:#151     // intValue:()I
  #124 = Utf8               java/lang/Integer
  #125 = Utf8               java/lang/Object
  #126 = Utf8               java/util/function/Supplier
  #127 = Utf8               get
  #128 = Utf8               valueOf
  #129 = Utf8               (I)Ljava/lang/Integer;
  #130 = Methodref          #152.#153     // java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #131 = Methodref          #3.#154       // java8inaction/lazycompute/LazyList.lambda$from$0:(I)Ljava8inaction/lazycompute/LazyList;
  #132 = Utf8               (I)Ljava/util/function/Supplier;
  #133 = Utf8               java/util/function/Predicate
  #134 = Utf8               test
  #135 = Utf8               (Ljava/lang/Object;)Z
  #136 = Methodref          #3.#155       // java8inaction/lazycompute/LazyList.lambda$filter$1:(Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
  #137 = Utf8               (Ljava8inaction/lazycompute/LazyList;Ljava/util/function/Predicate;)Ljava/util/function/Supplier;
  #138 = Methodref          #3.#156       // java8inaction/lazycompute/LazyList.lambda$primes$3:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
  #139 = Utf8               (Ljava8inaction/lazycompute/LazyList;)Ljava/util/function/Supplier;
  #140 = Utf8               java/lang/System
  #141 = Utf8               out
  #142 = Utf8               Ljava/io/PrintStream;
  #143 = Utf8               java/io/PrintStream
  #144 = Utf8               println
  #145 = Utf8               (Ljava/lang/Object;)V
  #146 = Utf8               ()V
  #147 = Methodref          #3.#157       // java8inaction/lazycompute/LazyList.lambda$null$2:(Ljava8inaction/lazycompute/LazyList;Ljava/lang/Integer;)Z
  #148 = Utf8               (Ljava/lang/Integer;)Z
  #149 = Utf8               (Ljava8inaction/lazycompute/LazyList;)Ljava/util/function/Predicate;
  #150 = Utf8               intValue
  #151 = Utf8               ()I
  #152 = Class              #158          // java/lang/invoke/LambdaMetafactory
  #153 = NameAndType        #159:#163     // metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #154 = NameAndType        #84:#43       // lambda$from$0:(I)Ljava8inaction/lazycompute/LazyList;
  #155 = NameAndType        #83:#52       // lambda$filter$1:(Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
  #156 = NameAndType        #78:#60       // lambda$primes$3:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
  #157 = NameAndType        #79:#80       // lambda$null$2:(Ljava8inaction/lazycompute/LazyList;Ljava/lang/Integer;)Z
  #158 = Utf8               java/lang/invoke/LambdaMetafactory
  #159 = Utf8               metafactory
  #160 = Class              #165          // java/lang/invoke/MethodHandles$Lookup
  #161 = Utf8               Lookup
  #162 = Utf8               InnerClasses
  #163 = Utf8               (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #164 = Class              #166          // java/lang/invoke/MethodHandles
  #165 = Utf8               java/lang/invoke/MethodHandles$Lookup
  #166 = Utf8               java/lang/invoke/MethodHandles
{
  T head;
    descriptor: Ljava/lang/Object;
    flags:
    Signature: #28                          // TT;

  java.util.function.Supplier<java8inaction.lazycompute.LazyList<T>> tailSupplier;
    descriptor: Ljava/util/function/Supplier;
    flags:
    Signature: #31                          // Ljava/util/function/Supplier<Ljava8inaction/lazycompute/LazyList<TT;>;>;

  java8inaction.lazycompute.LazyList<T> getTail();
    descriptor: ()Ljava8inaction/lazycompute/LazyList;
    flags:
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: getfield      #1                  // Field tailSupplier:Ljava/util/function/Supplier;
         4: invokeinterface #2,  1            // InterfaceMethod java/util/function/Supplier.get:()Ljava/lang/Object;
         9: checkcast     #3                  // class java8inaction/lazycompute/LazyList
        12: areturn
      LineNumberTable:
        line 30: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      13     0  this   Ljava8inaction/lazycompute/LazyList;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
            0      13     0  this   Ljava8inaction/lazycompute/LazyList<TT;>;
    Signature: #41                          // ()Ljava8inaction/lazycompute/LazyList<TT;>;

  static java8inaction.lazycompute.LazyList<java.lang.Integer> from(int);
    descriptor: (I)Ljava8inaction/lazycompute/LazyList;
    flags: ACC_STATIC
    Code:
      stack=4, locals=1, args_size=1
         0: new           #3                  // class java8inaction/lazycompute/LazyList
         3: dup
         4: iload_0
         5: invokestatic  #4                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
         8: iload_0
         9: invokedynamic #5,  0              // InvokeDynamic #0:get:(I)Ljava/util/function/Supplier;
        14: invokespecial #6                  // Method "<init>":(Ljava/lang/Object;Ljava/util/function/Supplier;)V
        17: areturn
      LineNumberTable:
        line 42: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      18     0     i   I
    Signature: #46                          // (I)Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;

  static java8inaction.lazycompute.LazyList<java.lang.Integer> from2(int);
    descriptor: (I)Ljava8inaction/lazycompute/LazyList;
    flags: ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: iload_0
         1: iconst_1
         2: iadd
         3: invokestatic  #7                  // Method from2:(I)Ljava8inaction/lazycompute/LazyList;
         6: areturn
      LineNumberTable:
        line 55: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       7     0     i   I
    Deprecated: true
    Signature: #46                          // (I)Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;
    RuntimeVisibleAnnotations:
      0: #50()

  java8inaction.lazycompute.LazyList<T> filter(java.util.function.Predicate<T>);
    descriptor: (Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
    flags:
    Code:
      stack=5, locals=2, args_size=2
         0: aload_0
         1: getfield      #8                  // Field head:Ljava/lang/Object;
         4: ifnonnull     11
         7: aload_0
         8: goto          53
        11: aload_1
        12: aload_0
        13: invokevirtual #9                  // Method getHead:()Ljava/lang/Object;
        16: invokeinterface #10,  2           // InterfaceMethod java/util/function/Predicate.test:(Ljava/lang/Object;)Z
        21: ifeq          45
        24: new           #3                  // class java8inaction/lazycompute/LazyList
        27: dup
        28: aload_0
        29: invokevirtual #9                  // Method getHead:()Ljava/lang/Object;
        32: aload_0
        33: aload_1
        34: invokedynamic #11,  0             // InvokeDynamic #1:get:(Ljava8inaction/lazycompute/LazyList;Ljava/util/function/Predicate;)Ljava/util/function/Supplier;
        39: invokespecial #6                  // Method "<init>":(Ljava/lang/Object;Ljava/util/function/Supplier;)V
        42: goto          53
        45: aload_0
        46: invokevirtual #12                 // Method getTail:()Ljava8inaction/lazycompute/LazyList;
        49: aload_1
        50: invokevirtual #13                 // Method filter:(Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
        53: areturn
      LineNumberTable:
        line 65: 0
        line 67: 13
        line 68: 29
        line 70: 46
        line 65: 53
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      54     0  this   Ljava8inaction/lazycompute/LazyList;
            0      54     1 predicate   Ljava/util/function/Predicate;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
            0      54     0  this   Ljava8inaction/lazycompute/LazyList<TT;>;
            0      54     1 predicate   Ljava/util/function/Predicate<TT;>;
      StackMapTable: number_of_entries = 3
        frame_type = 11 /* same */
        frame_type = 33 /* same */
        frame_type = 71 /* same_locals_1_stack_item */
          stack = [ class java8inaction/lazycompute/LazyList ]
    Signature: #58                          // (Ljava/util/function/Predicate<TT;>;)Ljava8inaction/lazycompute/LazyList<TT;>;

  static java8inaction.lazycompute.LazyList<java.lang.Integer> primes(java8inaction.lazycompute.LazyList<java.lang.Integer>);
    descriptor: (Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
    flags: ACC_STATIC
    Code:
      stack=4, locals=1, args_size=1
         0: new           #3                  // class java8inaction/lazycompute/LazyList
         3: dup
         4: aload_0
         5: getfield      #8                  // Field head:Ljava/lang/Object;
         8: aload_0
         9: invokedynamic #14,  0             // InvokeDynamic #2:get:(Ljava8inaction/lazycompute/LazyList;)Ljava/util/function/Supplier;
        14: invokespecial #6                  // Method "<init>":(Ljava/lang/Object;Ljava/util/function/Supplier;)V
        17: areturn
      LineNumberTable:
        line 80: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      18     0 prime   Ljava8inaction/lazycompute/LazyList;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
            0      18     0 prime   Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;
    Signature: #63                          // (Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;)Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;

  static <T extends java.lang.Object> void printAll(java8inaction.lazycompute.LazyList<T>);
    descriptor: (Ljava8inaction/lazycompute/LazyList;)V
    flags: ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: ifnull        14
         4: getstatic     #15                 // Field java/lang/System.out:Ljava/io/PrintStream;
         7: aload_0
         8: invokevirtual #9                  // Method getHead:()Ljava/lang/Object;
        11: invokevirtual #16                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
        14: aload_0
        15: invokevirtual #12                 // Method getTail:()Ljava8inaction/lazycompute/LazyList;
        18: invokestatic  #17                 // Method printAll:(Ljava8inaction/lazycompute/LazyList;)V
        21: return
      LineNumberTable:
        line 93: 0
        line 94: 4
        line 96: 14
        line 97: 21
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      22     0  list   Ljava8inaction/lazycompute/LazyList;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
            0      22     0  list   Ljava8inaction/lazycompute/LazyList<TT;>;
      StackMapTable: number_of_entries = 1
        frame_type = 14 /* same */
    Signature: #67                          // <T:Ljava/lang/Object;>(Ljava8inaction/lazycompute/LazyList<TT;>;)V

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=2, args_size=1
         0: iconst_2
         1: invokestatic  #18                 // Method from:(I)Ljava8inaction/lazycompute/LazyList;
         4: astore_1
         5: getstatic     #15                 // Field java/lang/System.out:Ljava/io/PrintStream;
         8: aload_1
         9: invokestatic  #19                 // Method primes:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
        12: invokevirtual #9                  // Method getHead:()Ljava/lang/Object;
        15: invokevirtual #16                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
        18: getstatic     #15                 // Field java/lang/System.out:Ljava/io/PrintStream;
        21: aload_1
        22: invokestatic  #19                 // Method primes:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
        25: invokevirtual #12                 // Method getTail:()Ljava8inaction/lazycompute/LazyList;
        28: getfield      #8                  // Field head:Ljava/lang/Object;
        31: invokevirtual #16                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
        34: getstatic     #15                 // Field java/lang/System.out:Ljava/io/PrintStream;
        37: aload_1
        38: invokestatic  #19                 // Method primes:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
        41: invokevirtual #12                 // Method getTail:()Ljava8inaction/lazycompute/LazyList;
        44: invokevirtual #12                 // Method getTail:()Ljava8inaction/lazycompute/LazyList;
        47: invokevirtual #9                  // Method getHead:()Ljava/lang/Object;
        50: invokevirtual #16                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
        53: return
      LineNumberTable:
        line 100: 0
        line 101: 5
        line 102: 18
        line 103: 34
        line 107: 53
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      54     0  args   [Ljava/lang/String;
            5      49     1  head   Ljava8inaction/lazycompute/LazyList;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
            5      49     1  head   Ljava8inaction/lazycompute/LazyList<Ljava/lang/Integer;>;

  public java8inaction.lazycompute.LazyList(T, java.util.function.Supplier<java8inaction.lazycompute.LazyList<T>>);
    descriptor: (Ljava/lang/Object;Ljava/util/function/Supplier;)V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=3
         0: aload_0
         1: invokespecial #20                 // Method java/lang/Object."<init>":()V
         4: aload_0
         5: aload_1
         6: putfield      #8                  // Field head:Ljava/lang/Object;
         9: aload_0
        10: aload_2
        11: putfield      #1                  // Field tailSupplier:Ljava/util/function/Supplier;
        14: return
      LineNumberTable:
        line 14: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      15     0  this   Ljava8inaction/lazycompute/LazyList;
            0      15     1  head   Ljava/lang/Object;
            0      15     2 tailSupplier   Ljava/util/function/Supplier;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
            0      15     0  this   Ljava8inaction/lazycompute/LazyList<TT;>;
            0      15     1  head   TT;
            0      15     2 tailSupplier   Ljava/util/function/Supplier<Ljava8inaction/lazycompute/LazyList<TT;>;>;
    Signature: #74                          // (TT;Ljava/util/function/Supplier<Ljava8inaction/lazycompute/LazyList<TT;>;>;)V

  public T getHead();
    descriptor: ()Ljava/lang/Object;
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: getfield      #8                  // Field head:Ljava/lang/Object;
         4: areturn
      LineNumberTable:
        line 19: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Ljava8inaction/lazycompute/LazyList;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Ljava8inaction/lazycompute/LazyList<TT;>;
    Signature: #77                          // ()TT;
}
Signature: #85                          // <T:Ljava/lang/Object;>Ljava/lang/Object;
SourceFile: "LazyList.java"
InnerClasses:
     public static final #161= #160 of #164; //Lookup=class java/lang/invoke/MethodHandles$Lookup of class java/lang/invoke/MethodHandles
BootstrapMethods:
  0: #94 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #95 ()Ljava/lang/Object;
      #96 invokestatic java8inaction/lazycompute/LazyList.lambda$from$0:(I)Ljava8inaction/lazycompute/LazyList;
      #97 ()Ljava8inaction/lazycompute/LazyList;
  1: #94 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #95 ()Ljava/lang/Object;
      #105 invokespecial java8inaction/lazycompute/LazyList.lambda$filter$1:(Ljava/util/function/Predicate;)Ljava8inaction/lazycompute/LazyList;
      #97 ()Ljava8inaction/lazycompute/LazyList;
  2: #94 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #95 ()Ljava/lang/Object;
      #109 invokestatic java8inaction/lazycompute/LazyList.lambda$primes$3:(Ljava8inaction/lazycompute/LazyList;)Ljava8inaction/lazycompute/LazyList;
      #97 ()Ljava8inaction/lazycompute/LazyList;
  3: #94 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #119 (Ljava/lang/Object;)Z
      #120 invokestatic java8inaction/lazycompute/LazyList.lambda$null$2:(Ljava8inaction/lazycompute/LazyList;Ljava/lang/Integer;)Z
      #121 (Ljava/lang/Integer;)Z

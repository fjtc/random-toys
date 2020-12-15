# JavaWrapperTest

## Description

This is a very simple benchmark code for **Java** primitive wrap/unwrap 
operations. It can be used to demonstrate why the primitive wrappers are not
suitable for scenarios and why it is important to restrict their use to the 
specified use cases only.

This example shows the same operation being executed using primitives and 
wrappers and as expected, the usage of wrappers are usually 10 times slower
than the use of primitives alone.

Aside from the execution time, it is also important to notice that the code with
wrappers also exert an extreme pressure over the garbage collector because the
wrappers are immutable and requires new instances for each changes in the
values.

## Compile and test

To compile it just run:

```
$ javac JavaWrapperTest.java
```

To run it, just use:

```
$ java JavaWrapperTest
```

The expected output is (OpenJDK 8):

```
noWRap v=2305843005992468481 in 589ms
wrap v=2305843005992468481 in 5601ms
noWRap	wrap
589	5601
```

## Technical details

The functions tested are:

```java
	private static long noWrap() {
		long v = 0;

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			v += i;
		}
		return v;
	}

	private static long wrap() {
		Long v = Long.valueOf(0);

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			v += i;
		}
		return v;
	}
```

Although they result in the same final value, the usage of a single wrapper in
the ``wrap()`` function generates a slightly distinct bytecodes:

```
  private static long noWrap();
    Code:
       0: lconst_0
       1: lstore_0
       2: iconst_0
       3: istore_2
       4: iload_2
       5: ldc           #3                  // int 2147483647
       7: if_icmpge     21
      10: lload_0
      11: iload_2
      12: i2l
      13: ladd
      14: lstore_0
      15: iinc          2, 1
      18: goto          4
      21: lload_0
      22: lreturn

  private static long wrap();
    Code:
       0: lconst_0
       1: invokestatic  #4                  // Method java/lang/Long.valueOf:(J)Ljava/lang/Long;
       4: astore_0
       5: iconst_0
       6: istore_1
       7: iload_1
       8: ldc           #3                  // int 2147483647
      10: if_icmpge     30
      13: aload_0
      14: invokevirtual #5                  // Method java/lang/Long.longValue:()J
      17: iload_1
      18: i2l
      19: ladd
      20: invokestatic  #4                  // Method java/lang/Long.valueOf:(J)Ljava/lang/Long;
      23: astore_0
      24: iinc          1, 1
      27: goto          7
      30: aload_0
      31: invokevirtual #5                  // Method java/lang/Long.longValue:()J
      34: lreturn
```

## Disclamer

The contents of this code is not designed to provide a full analysis over this
subject, just a simple example to show how it works. At the time of the creation
of this code, I tested it with both Java 8 and Java 11 and the results are almost
the same.

It is possible that, with the adaptative optimizations applied by the JVM, it 
will end up being replaced by the constant itself, thus invalidating this
benchmark.

## License

This code is released under the **CC0** license. See **COPYING** for further 
infomration.


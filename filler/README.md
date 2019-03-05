# Filler

## Description

This is a very simple Java program that illustrates how to use NIO to
fill the current disk with zeroes.

## How to compile

Just execute **mvn install**.

## How to use

Run it by using the command **java -jar filler-0.0.1-SNAPSHOT.jar**.

## Testing on Linux

It is possible to test the behavior of this tool by creating a very small
disk image, mount it and run the program inside it.

1. Create the disk image by executing "fallocate -l 1mib tmp.dsk";
1. Create the file system inside the disk image  "mkfs.ext4 tmp.dsk";
1. Mount the disk image with write privileges (e.g.: "gnome-disk-image-mounter --writable tmp.dsk");
1. Open a terminal inside the disk and run "java -jar path-to-filler-0.0.1-SNAPSHOT.jar";

This will eventually result in something like:

```
java.io.IOException: No space left on device
	at java.base/sun.nio.ch.FileDispatcherImpl.write0(Native Method)
	at java.base/sun.nio.ch.FileDispatcherImpl.write(FileDispatcherImpl.java:62)
	at java.base/sun.nio.ch.IOUtil.writeFromNativeBuffer(IOUtil.java:115)
	at java.base/sun.nio.ch.IOUtil.write(IOUtil.java:58)
	at java.base/sun.nio.ch.FileChannelImpl.write(FileChannelImpl.java:280)
	at br.com.brokenbits.randomtoys.filler.Filler.main(Filler.java:70)
```
when the disk is full.

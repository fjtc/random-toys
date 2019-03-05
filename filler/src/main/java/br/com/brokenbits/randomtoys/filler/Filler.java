/* 
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org>
 */
package br.com.brokenbits.randomtoys.filler;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * This class implements a simple disk filler using NIO. It creates the file
 * <b>delete.me</b> and fills it with zeroes until the disk is full. Once the
 * program ends, the file is deleted.
 */
public class Filler {

	public static void main(String [] args) {
		File file = new File("delete.me");

        // Verifies if the file exists. If so, exit without doing nothing.
        if (file.exists()) {
            System.err.printf("The file %1$s already exists. Delete it before continue.", file.getAbsolutePath());
            System.exit(1);
        }

        // Sets the file to be deleted on exit.
		file.deleteOnExit();
		try {
            long total = 0;
            // Open the file for read and write synchronously.
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws")) {
                // Create the file channel to speed up the access to the underlying file.
                FileChannel channel = randomAccessFile.getChannel();
                // Try to create a direct byte buffer whenever possible.
                ByteBuffer bytes = ByteBuffer.allocateDirect(1024 * 4);
                // Write the zeroes to the file using the byte buffer. It will
                // stop if the last call of channel.write() could not write all
                // bytes inside the buffer (it should not happen inside a disk)
                // or a "java.io.IOException: No space left on device" is
                // thrown.
                do {
                    // Rewind the byte buffer to its initial position. If this
                    // operation is not performed, no bytes will be available to
                    // be written in the next round.
                    bytes.rewind();
                    // Write all the contents of the byte buffer into the file.
                    // It consumes all bytes from the buffer.
                    channel.write(bytes);
                    // The position inside the buffer will be the total number
                    // of bytes written to the file.
                    total += bytes.position();
                } while (bytes.remaining() == 0); 
            }
            System.out.printf("%1$d bytes written.\n", total);
		} catch(Exception e){
            e.printStackTrace();
        } 
	}
}


// JavaWrapperTest - A small benchmark for Java primitive wrappers
// Written in 2019 by Fabio Jun Takada Chino 
//
// To the extent possible under law, the author(s) have dedicated all copyright
// and related and neighboring rights to this software to the public domain
// worldwide. This software is distributed without any warranty.
//
// You should have received a copy of the CC0 Public Domain Dedication along 
// with this software. If not, 
// see <http://creativecommons.org/publicdomain/zero/1.0/>. 
public class JavaWrapperTest {

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

	public static void main(String [] args) {
		
		long start = System.currentTimeMillis();
		long v = noWrap();
		long noWrapTiming = System.currentTimeMillis() - start;
		System.out.printf("noWRap v=%1$d in %2$dms\n", v, noWrapTiming);
		
		start = System.currentTimeMillis();
		v = wrap();
        long wrapTiming = System.currentTimeMillis() - start;
		System.out.printf("wrap v=%1$d in %2$dms\n", v, wrapTiming);
		
		System.out.println("noWRap\twrap");
		System.out.printf("%1$d\t%2$d\n", noWrapTiming, wrapTiming);
	}
}


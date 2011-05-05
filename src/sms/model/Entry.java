package sms.model;

import java.math.*;

public class Entry implements java.io.Serializable {
	public char c;
	public BigInteger count;
	protected Entry() {
	}
	public Entry(char c, BigInteger count) {
		this.c = c;
		this.count = count;
	}
	public Entry(char c, int count) {
		this(c, BigInteger.valueOf(count));
	}
	public Entry(Entry that) {
		this.c = that.c;
		this.count = that.count.or(BigInteger.ZERO);
	}
}

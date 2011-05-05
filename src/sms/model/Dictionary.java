package sms.model;

import java.util.*;
import java.math.*;

public class Dictionary implements java.io.Serializable {
	ArrayList<Entry> entries;
	BigInteger totalSize = null;
	
	protected Dictionary() { //don't use this, only for deserialization
	}
	
	public Dictionary(Collection<Entry> c) {
		entries = new ArrayList<Entry>();
		for(Entry e: c)
			if(e.count.signum() == 1)
				entries.add(new Entry(e));
			else
				entries.add(new Entry(e.c, 1)); //insert pseudo count
	}
	public Dictionary(Map<Character, Integer> map) {
		entries = new ArrayList<Entry>();
		for(Map.Entry<Character, Integer> e: map.entrySet())
			if(e.getValue() > 0)
				entries.add(new Entry(e.getKey(), e.getValue()));
			else
				entries.add(new Entry(e.getKey(), 1)); //insert pseudo count
	}
	// copy ctor
	public Dictionary(Dictionary that) {
		this(that.entries);
	}
	public BigInteger getTotalSize() {
		if(totalSize == null) {
			totalSize = BigInteger.ZERO;
			for(Entry e: entries)
				totalSize = totalSize.add(e.count);
		}
		return totalSize;
	}
	
	public Dictionary scale(BigInteger s) {
		Dictionary result = new Dictionary(this);
		
		for(Entry e: result.entries)
			e.count = e.count.multiply(s);
		return result;
	}
	public BigInteger getOffset(Character c) {
		BigInteger result = BigInteger.ZERO;
		for(Entry e: entries)
			if(e.c != c)
				result = result.add(e.count);
			else
				break;
		if(result.compareTo(getTotalSize()) >= 0) {
			System.err.println(this);
			throw new RuntimeException("unmapped character: \'" + c + "\'");
		}
		return result;
	}
	public Entry query(BigDecimal key) {
		for(int i = 0; i < entries.size(); ++i) {
			Entry e = entries.get(i);
			BigDecimal c = new BigDecimal(e.count);
			if(key.compareTo(c) >= 0)
				key = key.subtract(c);
			else
				return e;
		}
		System.err.println(this);
		throw new RuntimeException("key " + key + " is not in dictionary's range " + getTotalSize());
	}
	
	public String toString() {
		String result = "";
		for(Entry e: entries)
			result += e.c + ":=" + e.count + " ";
		result += "\n";
		return result;
	}
}

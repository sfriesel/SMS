package sms.codec;

import java.util.Deque;
import java.util.ArrayDeque;
import java.math.*;
import sms.model.*;
import sms.converter.*;

public class Encoder {
	Trie<Character, Dictionary> trie;
	
	public Encoder(Trie<Character, Dictionary> trie) {
		this.trie = trie;
	}
	
	private Dictionary getDictionary(Deque<Character> prefix) {
		Trie<Character, Dictionary> t = trie;
		for(Character c: prefix) {
			if(t.hasChild(c))
				t = t.getChild(c);
			else
				break;
		}
		
		return t.getData();
	}
	
	public static String bigDecimal2Bits(BigDecimal b) {
		if(b.signum() == 0)
			return "0";
		else {
			String result = "";
			while(b.signum() > 0) {
				b = b.multiply(BigDecimal.valueOf(2));
				if(b.compareTo(BigDecimal.ONE) >= 0) {
					b = b.subtract(BigDecimal.ONE);
					result += "1";
				
				} else {
					result += "0";
				}
			}
			return result;
		}
	}
	
	public BigDecimal encode(String input) {
		input = Converter.convert(input);
		Deque<Character> prefixDeque = new ArrayDeque<Character>();
		
		BigInteger absoluteSize = BigInteger.ONE;
		BigInteger lowerBound = BigInteger.ZERO;
		BigInteger width = absoluteSize;
		
		input = input + '\4';
		
		for(Character c: input.toCharArray()) {
			Dictionary d = getDictionary(prefixDeque);
			
			BigInteger scale = d.getTotalSize();
			d = d.scale(width);
			
			absoluteSize = absoluteSize.multiply(scale);
			lowerBound = lowerBound.multiply(scale);
			
			BigInteger offset = d.getOffset(c);
			lowerBound = lowerBound.add(offset);
			Entry e = d.query(new BigDecimal(offset)); //FIXME
			assert(e.c == c);
			width = e.count;
			
			prefixDeque.addFirst(c);
		}
		
		//calculate best binary number
		
		BigInteger lower = lowerBound;
		BigInteger upper = lowerBound.add(width);
		BigInteger size = absoluteSize;
		
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal x = new BigDecimal("0.5");
		BigDecimal mag = x;
		
		int bitsUsed = 0;
		
		//System.err.print("Code: ");
		while(lower.compareTo(BigInteger.ZERO) > 0) {
			
			lower = lower.shiftLeft(1);
			upper = upper.shiftLeft(1);
			if(size.compareTo(upper) < 0) {
				lower = lower.subtract(size);
				upper = upper.subtract(size);
				result = result.add(mag);
				//System.err.print("1");
			}
			//else
				//System.err.print("0");
			mag = mag.multiply(x);
			bitsUsed++;
		}
		System.err.printf("(%5.4f bit/char)\n", 1.0 * bitsUsed / (input.length()-1));
		return result;
	}
}

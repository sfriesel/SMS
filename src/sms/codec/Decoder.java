package sms.codec;

import java.util.ArrayDeque;
import java.util.Deque;
import java.math.*;
import sms.model.*;

public class Decoder {
	Trie<Character, Dictionary> trie;
	
	public Decoder(Trie<Character, Dictionary> trie) {
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
	
	public static BigDecimal bits2BigDecimal(String in) {
		char[] s = in.toCharArray();
		int power = 1;
		BigDecimal res = BigDecimal.ZERO;
		BigDecimal half = new BigDecimal(1.0/2.0);
		for(int i = 0; i < s.length; i++) {
			if(s[i] == '1') {
				res = res.add(half.pow(power));
			}
			++power;
		}
		return res;
	}
	
	public String decode(BigDecimal data) {
		Deque<Character> prefixDeque = new ArrayDeque<Character>();
		
		Dictionary d = trie.getData();
		BigDecimal k = data.multiply(new BigDecimal(d.getTotalSize()));
		
		while(true) {
			Entry e = d.query(k);
			
			k = k.subtract(new BigDecimal(d.getOffset(e.c))); //FIXME
			
			if(e.c == '\4')
				break;
			
			prefixDeque.addFirst(e.c);
			
			d = getDictionary(prefixDeque);
			k = k.multiply(new BigDecimal(d.getTotalSize()));
			
			d = d.scale(e.count);
		}
		
		String result = "";
		while(!prefixDeque.isEmpty())
			result = result + prefixDeque.removeLast();
		
		return result;
	}
}

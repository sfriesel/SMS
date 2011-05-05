package sms.codec;

import java.io.*;
import sms.model.*;
import java.math.*;

public class BatchEncoder {
	public static void main(String[] args) throws Exception {
		BufferedReader inStream = new BufferedReader(new InputStreamReader(System.in, "ISO-8859-1"));
		
		Trie<Character, Dictionary> t = new TrieLoader().loadTrie();
		
		Encoder e = new Encoder(t);
		
		String line;
		while((line = inStream.readLine()) != null) {
			BigDecimal result = e.encode(line);
			System.out.println(Encoder.bigDecimal2Bits(result));
		}
	}
}

package sms.codec;

import java.io.*;
import sms.model.*;
import java.math.*;

class BatchDecoder {
	public static void main(String[] args) throws Exception {
		BufferedReader inStream = new BufferedReader(new InputStreamReader(System.in));
		
		Trie<Character, Dictionary> t = new TrieLoader().loadTrie();
		
		Decoder d = new Decoder(t);
		
		String line;
		while((line = inStream.readLine()) != null) {
			BigDecimal num = Decoder.bits2BigDecimal(line);
			String result = d.decode(num);
			System.out.println(result);
		}
	}
}

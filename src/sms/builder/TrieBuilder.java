package sms.builder;

import java.util.*;
import java.math.*;
import java.io.*;
import sms.model.*;
import sms.converter.*;

public class TrieBuilder {
	private static final Character EOT = new Character('\4'); //EOT, Ctrl-D
	private static long dictCount = 0;
	
	public Trie<Character, sms.model.Dictionary> buildWithPrefix(String prefix, BufferedReader in, int cutoff) throws UnsupportedEncodingException, IOException {
		//System.err.println("Buiding trie with prefix \"" + prefix + "\"");
		Trie<Character, sms.model.Dictionary> trie;
		HashMap<Character, Integer> map = new HashMap<Character, Integer>(60);
		map.put(EOT, 1);
		for(Character c: Converter.getChars().toCharArray())
			map.put(c, 1);
		
		String line;
		
		//FIXME this temporary b/c of input pollution
		int eotHelperCounter = 0;
		
		in.reset();
		while((line = in.readLine()) != null) {
			line = Converter.convert(line);
			for(int pos = line.indexOf(prefix); pos != -1; pos = line.indexOf(prefix, pos+1)) {
				if(pos + prefix.length() < line.length()) {
					Character c = line.charAt(pos+prefix.length());
					//try {
					map.put(c, map.get(c) + 1);
					//} catch(NullPointerException e) {
					//	System.err.println("unmapped character " + c);
					//	throw e;
					//}
					eotHelperCounter++;
				} else if(pos + prefix.length() == line.length()) {
					map.put(EOT, map.get(EOT) + 1);
					break;
				}
			}
		}
		
		//FIXME dito
		map.put(EOT, 1 + eotHelperCounter/15);
		
		sms.model.Dictionary dict = new sms.model.Dictionary(map);
		trie = new Trie<Character, sms.model.Dictionary>(dict);
		
		if(dict.getTotalSize().compareTo(BigInteger.valueOf(cutoff + Converter.getChars().length())) < 0)
			return trie;
		
		dictCount++;
		
		for(Map.Entry<Character, Integer> e: map.entrySet()) {
			if(e.getValue() > cutoff && e.getKey() != EOT) {
				if(prefix.equals(""))
					System.err.println("building sub trie... " + e.getKey());
				Trie<Character, sms.model.Dictionary> child = buildWithPrefix(e.getKey() + prefix, in, cutoff);
				
				if(child.getData().getTotalSize().compareTo(BigInteger.valueOf(cutoff + Converter.getChars().length())) > 0)
					trie.setChild(e.getKey(), child);
			}
		}
		
		return trie;
	}
	
	public Trie<Character, sms.model.Dictionary> build(InputStream input, int cutoff) throws UnsupportedEncodingException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input, "ISO-8859-1"));
		in.mark(10000000); //buffer 10MB; needs to be adjusted for bigger training files!
		
		System.err.println("[TrieBuilder] generating model with cutoff = " + cutoff);
		
		Trie<Character, sms.model.Dictionary> result = buildWithPrefix("", in, cutoff);
		
		System.err.println("[TrieBuilder] generated " + dictCount + " dictionaries");
		
		return result;
	}
	
	public void export(String filename) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
		oos.writeObject(this);
	}
	
	public static void main(String[] args) throws Exception {
		//TrieBuilder b = new TrieBuilder();
		//Trie<Character, sms.model.Dictionary> x = b.build(System.in);
		
		//System.err.println(x);
	}
}

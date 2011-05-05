package sms.codec;

import java.math.*;
import java.io.*;
import sms.model.*;

public class TestCase {
	public static boolean runTest(Encoder e, Decoder d, String s) {
		System.err.println("encoding \"" + s + "\"...");
		BigDecimal z = e.encode(s);
		System.err.println("decoding...");
		System.err.println("\"" + d.decode(z) + "\"");
		
		return true;
	}
	public static void main(String[] args) throws Exception {
		FileInputStream trieFile;
		
		int cutoff = 120;
		if(args.length > 0) {
			cutoff = Integer.valueOf(args[0]);
		}
		
		while(true) {
			try {
				trieFile = new FileInputStream("../data/dasher.trie");
			} catch(FileNotFoundException e) {
				FileInputStream training = new FileInputStream("../data/dasher.txt");
				Trie<Character, Dictionary> temp = new sms.builder.TrieBuilder().build(training, cutoff);
				
				FileOutputStream fos = new FileOutputStream("../data/dasher.trie");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				
				oos.writeObject(temp);
				
				continue;
			}
			break;
		}
		ObjectInputStream ois = new ObjectInputStream(trieFile);
		
		Trie<Character, Dictionary> t = (Trie<Character, Dictionary>) (ois.readObject());
		
		System.out.println(t);
		
		Decoder d = new Decoder(t);
		Encoder e = new Encoder(t);
		
		runTest(e, d, "MEINE HERREN ES FUNKTIONIERT!");
		runTest(e, d, "Ich tue hier mal einen Testfall rein");
		runTest(e, d, "Florian, möge dir diese Nachricht dir auf dem Strahle des Stahls zukommen.");
	}
}

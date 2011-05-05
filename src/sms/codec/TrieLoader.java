package sms.codec;

import sms.model.*;
import java.io.*;

public class TrieLoader {
	static String trieExtension = ".trie";
	static String trainingExtension = ".txt";
	int cutoff = 120;
	String basepath;
	public TrieLoader() {
		this("../data/training-iso");
	}
	public TrieLoader(String basepath) {
		this.basepath = basepath;
	}
	
	protected void createTrieFile() throws IOException {
		FileInputStream training = new FileInputStream(basepath + trainingExtension);
		Trie<Character, Dictionary> temp = new sms.builder.TrieBuilder().build(training, cutoff);
		
		FileOutputStream fos = new FileOutputStream(basepath + trieExtension);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(temp);
	}
	
	public Trie<Character, Dictionary> loadTrie() throws IOException {
		FileInputStream trieFile;
		while(true) {
			try {
				//try to load the serialized trie
				trieFile = new FileInputStream(basepath + trieExtension);
				
				ObjectInputStream ois = new ObjectInputStream(trieFile);
				
				return (Trie<Character, Dictionary>) (ois.readObject());
			} catch(Exception e) {
				System.err.println("trie needs to be rebuild...");
				createTrieFile();
				continue;
			}
		}
		
	}
}

package sms.converter;

public class Converter {
	static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ :-)!.";
	
	public static String getChars() { return chars; }
	
	public static String convert(String input) {
		String result = input;
		result = result.toUpperCase();
		result = result.replaceAll("\n", " ");
		result = result.replaceAll("Ä", "AE");
		//System.err.println("\u00C4");
//		System.err.println(result);
		result = result.replaceAll("Ö", "OE");
//		System.err.println(result)
		result = result.replaceAll("Ü", "UE");
		result = result.replaceAll("ß", "SS");
		result = result.replaceAll("[^A-Z :\\-)!]", ".");
		return result;
	}
	
	public static void main(String[] args) {
		System.err.println(convert("ÄÖÜßaed?!:-)"));
	}
}

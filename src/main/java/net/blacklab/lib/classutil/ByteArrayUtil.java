package net.blacklab.lib.classutil;

import java.util.ArrayList;

public class ByteArrayUtil {
	
	public static byte[] replaceByteCodes(byte[] target, byte[] from, byte[] to) {
		ArrayList<Byte> result = new ArrayList<>();
		
		int index = 0;
		while (index < target.length) {
			// Search replaced bytes
			int matchCounter = 0;
			for (int j = 0; j < from.length && target[index+j] == from[j]; j++) {
				matchCounter++;
			}
			// Matched completely
			if (matchCounter == from.length) {
				System.out.println("Execute replace.");
				for (int k = 0; k < to.length; k++) {
					result.add(to[k]);
				}
				index += to.length;
			} else {
				result.add(target[index]);
				index++;
			}
		}
		return converrtByteListToArray(result);
	}
	
	private static ArrayList<Byte> convertByteArrayToList(byte[] src) {
		ArrayList<Byte> result = new ArrayList<>();
		for (int i = 0; i < src.length; i++) {
			result.add(src[i]);
		}
		return result;
	}
	
	private static byte[] converrtByteListToArray(ArrayList<Byte> src) {
		byte result[] = new byte[src.size()];
		for (int i = 0; i < src.size(); i++) {
			result[i] = src.get(i);
		}
		return result;
	}
}

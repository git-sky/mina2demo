package cn.com.sky.mina2.simulator2.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.BitSet;
import java.util.Calendar;

/**
 * 瀛楄妭杞崲宸ュ叿绫�
 * 
 * @author XieHaiSheng
 * 
 */
public class BytesConvert {

	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * @param b
	 *            楂樹綅鍦ㄥ墠
	 * @return
	 */
	public static int bytes2Int(byte... b) {
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		int length = b.length > 4 ? 4 : b.length;
		for (int i = 0; i < length; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}

	public static float bytes2Float(byte[] b) {
		int l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		return Float.intBitsToFloat(bytes2Int(b));
	}

	public static long bytes2long(byte... b) {
		long temp = 0;
		long res = 0;
		int length = b.length > 8 ? 8 : b.length;
		for (int i = 0; i < length; i++) {
			res <<= 8;
			temp = b[i] & 0xff;
			res |= temp;
		}
		return res;
	}

	public static BitSet fromByte(byte... b) {
		BitSet bits = new BitSet(b.length * 8);
		int index = 0;
		// 浠庝綆浣嶅紑濮�
		for (int j = b.length - 1; j > -1; j--) {
			for (int i = 0; i < 8; i++) {
				bits.set(index, (b[j] & 1) == 1);
				b[j] >>= 1;
				index++;
			}
		}

		return bits;
	}

	public static void printBitSet(BitSet bs) {
		StringBuffer buf = new StringBuffer();
		buf.append("[\n");
		for (int i = 0; i < bs.size(); i++) {
			if (i < bs.size() - 1) {
				buf.append(bs.get(i) + ",");
			} else {
				buf.append(bs.get(i));
			}
			if ((i + 1) % 8 == 0 && i != 0) {
				buf.append("\n");
			}
		}
		buf.append("]");
		System.out.println(buf.toString());
	}

	/**
	 * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃涓�
	 * 
	 * @param data
	 *            byte[]
	 * @param toDigits
	 *            鐢ㄤ簬鎺у埗杈撳嚭鐨刢har[]
	 * @return 鍗佸叚杩涘埗String
	 */
	public static String encodeHexStr(byte[] data) {
		return new String(encodeHex(data, DIGITS_UPPER));
	}

	public static byte[] getBytes(int data) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) (data & 0xff);
		bytes[2] = (byte) ((data >> 8) & 0xff);
		bytes[1] = (byte) ((data >> 16) & 0xff);
		bytes[0] = (byte) ((data >> 24) & 0xff);

		return bytes;
	}

	public static byte[] getBytes(double data) {
		long intBits = Double.doubleToLongBits(data);
		return getBytes(intBits);
	}

	public static byte[] float2byte(float f) {

		// 鎶奻loat杞崲涓篵yte[]
		int fbit = Float.floatToIntBits(f);

		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (fbit >> (24 - i * 8));
		}

		// 缈昏浆鏁扮粍
		int len = b.length;
		// 寤虹珛涓�釜涓庢簮鏁扮粍鍏冪礌绫诲瀷鐩稿悓鐨勬暟缁�
		byte[] dest = new byte[len];
		// 涓轰簡闃叉淇敼婧愭暟缁勶紝灏嗘簮鏁扮粍鎷疯礉涓�唤鍓湰
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 灏嗛『浣嶇i涓笌鍊掓暟绗琲涓氦鎹�
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}

		return dest;

	}

	/**
	 * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃鏁扮粍
	 * 
	 * @param data
	 *            byte[]
	 * @param toDigits
	 *            鐢ㄤ簬鎺у埗杈撳嚭鐨刢har[]
	 * @return 鍗佸叚杩涘埗char[]
	 */
	public static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	public static String toFullBinaryString(int num) {
		char[] chs = new char[Integer.SIZE];
		for (int i = 0; i < Integer.SIZE; i++) {
			chs[Integer.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
		}
		return new String(chs);
	}

	/*
	 * 灏嗗崄鍏繘鍒跺瓧绗︽暟缁勮浆鎹负瀛楄妭鏁扮粍
	 * 
	 * @param data 鍗佸叚杩涘埗char[]
	 * 
	 * @return byte[]
	 * 
	 * @throws RuntimeException 濡傛灉婧愬崄鍏繘鍒跺瓧绗︽暟缁勬槸涓�釜濂囨�鐨勯暱搴︼紝灏嗘姏鍑鸿繍琛屾椂寮傚父
	 */
	public static byte[] decodeHex(String str) {
		str = str.replaceAll(",", "");
		char[] data = str.toCharArray();
		int len = data.length;
		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}
		byte[] out = new byte[len >> 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

	protected static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character " + ch
					+ " at index " + index);
		}
		return digit;
	}

	public static void printHexString(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
		}
		System.out.println();
	}

	// float杞琤yte[]
	public static byte[] floatToByte(float v) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		byte[] ret = new byte[4];
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(v);
		bb.get(ret);
		return ret;
	}

	// byte[]杞琭loat
	public static float byteToFloat(byte[] v) {
		ByteBuffer bb = ByteBuffer.wrap(v);
		FloatBuffer fb = bb.asFloatBuffer();
		return fb.get();
	}

	public static byte[] long2Bytes(long iSource, int iArrayLen) {

		int len = iArrayLen > 4 ? 4 : iArrayLen;

		byte[] b = new byte[len];
		for (int i = 0; i < len; i++) {
			b[i] = (byte) (iSource >> 8 * (len - i - 1) & 0xFF);
		}

		return b;
	}

	public static void main(String[] args) {
//		// System.out.println(printBitSet(fromByte(0x20)));
//		// System.out.println(bytes2Int((byte)0xFF));
//		int status = BytesConvert.bytes2Int((byte) 0x00, (byte) 0x01);
//		String s = "044A1C41";
//		// B442894C,B8FB474D,B2015901CC000010DD001FF201
//		// B442894C,B8FB474D,B2015901CC000010DD001FF201
//		// printHexString(getBytes(116.498705*30000*60));
//		double t = bytes2long(decodeHex(s));
//		System.out.println(t / 30000 / 60);// 0354188046625120
		
		long times=Calendar.getInstance().getTimeInMillis()/1000;
		
		System.out.println(times);
		
		System.out.println(encodeHexStr(long2Bytes(times,4)));
		
		long receiveTime = BytesConvert.bytes2long(long2Bytes(times,4)) * 1000;
		System.out.println(receiveTime);
		
	}

}

package cn.com.sky.mina2.simulator3.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.BitSet;

/**
 * 字节转换工具类
 * @author XieHaiSheng
 *
 */
public class BytesConvert {

	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	/**
	 * @param b
	 * 高位在前
	 * @return
	 */
	public static int bytes2Int(byte... b) {
        int mask=0xff;
        int temp=0;
        int n=0;
        int length=b.length>4?4:b.length;
        for(int i=0;i<length;i++){
           n<<=8;
           temp=b[i]&mask;
           n|=temp;
       }
	   return n;
	}
	
	 public static float bytes2Float(byte[] b)
    {
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
		int length=b.length>8?8:b.length;
		for (int i=0;i<length;i++) {
			res <<= 8;
			temp = b[i] & 0xff;
			res |= temp;
		}
		return res;
	}
	
	public static BitSet fromByte(byte... b)
	{
	    BitSet bits = new BitSet(b.length*8);
	    int index=0;
	    //从低位开始
	    for(int j=b.length-1;j>-1;j--){
	    	for (int i=0; i < 8; i++)
		    {
		        bits.set(index, (b[j] & 1) == 1);
		        b[j] >>= 1;
	    	    index++;
		    }
	    }
	    
	    return bits;
	}
	
	 public static void printBitSet(BitSet bs){
	        StringBuffer buf=new StringBuffer();  
	        buf.append("[\n");  
	        for(int i=0;i<bs.size();i++){
	            if(i<bs.size()-1){  
	                buf.append(bs.get(i)+",");  
	            }else{  
	                buf.append(bs.get(i));  
	            }
	            if((i+1)%8==0&&i!=0){  
	                buf.append("\n");  
	            }
	        }
	        buf.append("]");  
	        System.out.println(buf.toString());  
	    }
	
	    /**
	     * 将字节数组转换为十六进制字符串
	     *
	     * @param data
	     *            byte[]
	     * @param toDigits
	     *            用于控制输出的char[]
	     * @return 十六进制String
	     */
	 public static String encodeHexStr(byte[] data) {
	        return new String(encodeHex(data, DIGITS_UPPER));
	 }
	 
	 public static byte[] getBytes(int data)
	    {
	        byte[] bytes = new byte[4];
	        bytes[3] = (byte) (data & 0xff);
	        bytes[2] = (byte) ((data >> 8) & 0xff);
	        bytes[1] = (byte) ((data >> 16) & 0xff);
	        bytes[0] = (byte) ((data >> 24) & 0xff);
	       
	        return bytes;
	    }
	 
	 public static byte[] getBytes(double data)
    {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }
	 
	 public static byte[] float2byte(float f) {
	      
		    // 把float转换为byte[]  
		    int fbit = Float.floatToIntBits(f);  
		      
		    byte[] b = new byte[4];    
		    for (int i = 0; i < 4; i++) {    
		        b[i] = (byte) (fbit >> (24 - i * 8));    
		    }   
		    
		    // 翻转数组  
		    int len = b.length;  
		    // 建立一个与源数组元素类型相同的数组  
		    byte[] dest = new byte[len];  
		    // 为了防止修改源数组，将源数组拷贝一份副本  
		    System.arraycopy(b, 0, dest, 0, len);  
		    byte temp;  
		    // 将顺位第i个与倒数第i个交换  
		    for (int i = 0; i < len / 2; ++i) {  
		        temp = dest[i];  
		        dest[i] = dest[len - i - 1];  
		        dest[len - i - 1] = temp;  
		    }  
		    
		    return dest;  
		      
		}  
	 
	
	    
	    /**
	     * 将字节数组转换为十六进制字符数组
	     *
	     * @param data
	     *            byte[]
	     * @param toDigits
	     *            用于控制输出的char[]
	     * @return 十六进制char[]
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
	        for(int i = 0; i < Integer.SIZE; i++) {
	            chs[Integer.SIZE - 1 - i] = (char)(((num >> i) & 1) + '0');
	        }
	        return new String(chs);        
	 }
	
	 /* 将十六进制字符数组转换为字节数组
    *
    * @param data
    *            十六进制char[]
    * @return byte[]
    * @throws RuntimeException
    *             如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
    */
	 public static byte[] decodeHex(String str) {
		    str=str.replaceAll(",", "");
		    char[] data=str.toCharArray();
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
			 System.out.print(hex.toUpperCase() ); 
		 } 
		 System.out.println(); 
	}
	 
	 
	//float转byte[]
	 public static byte[] floatToByte(float v) {
	         ByteBuffer bb = ByteBuffer.allocate(4);
	         byte[] ret = new byte [4];
	         FloatBuffer fb = bb.asFloatBuffer();
	         fb.put(v);
	         bb.get(ret);
	         return ret;
	 }

	 //byte[]转float
	 public static float byteToFloat(byte[] v){
	         ByteBuffer bb = ByteBuffer.wrap(v);
	         FloatBuffer fb = bb.asFloatBuffer();
	         return fb.get();
	 }
	 
	 
	 /**
		 * @函数功能: 10进制串转为BCD码
		 * @输入参数: 10进制串
		 * @输出结果: BCD码
		 */
		public static byte[] str2Bcd(String asc) {
			int len = asc.length();
			int mod = len % 2;
			if (mod != 0) {
				asc = "0" + asc;
				len = asc.length();
			}
			byte abt[] = new byte[len];
			if (len >= 2) {
				len = len / 2;
			}
			byte bbt[] = new byte[len];
			abt = asc.getBytes();
			int j, k;
			for (int p = 0; p < asc.length() / 2; p++) {
				if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
					j = abt[2 * p] - '0';
				} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
					j = abt[2 * p] - 'a' + 0x0a;
				} else {
					j = abt[2 * p] - 'A' + 0x0a;
				}
				if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
					k = abt[2 * p + 1] - '0';
				} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
					k = abt[2 * p + 1] - 'a' + 0x0a;
				} else {
					k = abt[2 * p + 1] - 'A' + 0x0a;
				}
				int a = (j << 4) + k;
				byte b = (byte) a;
				bbt[p] = b;
			}
			return bbt;
		}

		/**
		 * Convert hex string to byte[]
		 * 
		 * @param hexString
		 *            the hex string
		 * @return byte[]
		 */
		public static byte[] hexStringToBytes(String hexString) {
			if (hexString == null || hexString.equals("")) {
				return null;
			}
			hexString = hexString.toUpperCase();
			int length = hexString.length() / 2;
			char[] hexChars = hexString.toCharArray();
			byte[] d = new byte[length];
			for (int i = 0; i < length; i++) {
				int pos = i * 2;
				d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
			}
			return d;
		}
		
		/**
		 * Convert char to byte
		 * 
		 * @param c
		 *            char
		 * @return byte
		 */
		private static byte charToByte(char c) {
			return (byte) "0123456789ABCDEF".indexOf(c);
		}
	 
	public static void main(String[] args) {
		//System.out.println(printBitSet(fromByte(0x20)));
		//System.out.println(bytes2Int((byte)0xFF));
		int status=BytesConvert.bytes2Int((byte)0x00,(byte)0x01);
		 String s="044A1C41";
		 //B442894C,B8FB474D,B2015901CC000010DD001FF201
		 //B442894C,B8FB474D,B2015901CC000010DD001FF201
		//printHexString(getBytes(116.498705*30000*60));
		double t=bytes2long(decodeHex(s));
		System.out.println(t/30000/60);//0354188046625120
	}
	
}

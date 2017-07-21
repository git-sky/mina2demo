package cn.com.sky.mina2.simulator3.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	static Pattern p = Pattern.compile("^\\s*$", 2);
	static Pattern numberpattern = Pattern.compile("[0-9]*");
	  
	public static boolean empty(String str)
	  {
	    if (str == null) {
	      return true;
	    }
	    Matcher m = p.matcher(str);
	    return m.matches();
	  }
	  
	  public static boolean isNumber(String str)
	  {
	    Matcher match = numberpattern.matcher(str);
	    return match.matches();
	  }
	  
	  public static String removeEndLetter(String str)
	  {
	    if ((str == null) || (str.length() <= 0)) {
	      return "";
	    }
	    return str.substring(0, str.length() - 1);
	  }
	  
	  public static String join(Collection<String> list, String split)
	  {
	    StringBuilder buf = new StringBuilder();
	    for (String str : list) {
	      if ((str != null) && (!"".equals(str))) {
	        buf.append(str).append(split);
	      }
	    }
	    return removeEndLetter(buf.toString());
	  }
	  
	  public static String join(Object[] ary, String split)
	  {
	    if (ary == null) {
	      return "";
	    }
	    StringBuilder buf = new StringBuilder();
	    for (int i = 0; i < ary.length; i++) {
	      if ((ary[i] != null) && (!"".equals(ary[i]))) {
	        buf.append(ary[i]).append(split);
	      }
	    }
	    return removeEndLetter(buf.toString());
	  }
	  
	  public static String firstLetterUpcase(String str)
	  {
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	  }
	  
}

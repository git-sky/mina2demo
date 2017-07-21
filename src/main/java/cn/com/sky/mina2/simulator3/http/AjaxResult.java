package cn.com.sky.mina2.simulator3.http;

public class AjaxResult {

	private boolean success = false;
	  protected String code;
	  private String text;
	  public Object data;
	  
	  public static AjaxResult fail(String text)
	  {
		  AjaxResult aar = new AjaxResult(text, null);
	    aar.text = text;
	    return aar;
	  }
	  
	  public static AjaxResult success(Object data)
	  {
		  AjaxResult aar = new AjaxResult("", data);
	     aar.success = true;
	    return aar;
	  }
	  
	  private AjaxResult(String text, Object data)
	  {
	    this.text = text;
	    this.data = data;
	  }
	  
	  public String getCode()
	  {
	    return this.code;
	  }
	  
	  public void setCode(String code)
	  {
	    this.code = code;
	  }
	  
	  public String getText()
	  {
	    return this.text;
	  }
	  
	  public void setText(String text)
	  {
	    this.text = text;
	  }
	  
	  public Object getData()
	  {
	    return this.data;
	  }
	  
	  public void setData(Object data)
	  {
	    this.data = data;
	  }
	  
	  public boolean getSuccess()
	  {
	    return this.success;
	  }
}

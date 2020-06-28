package Shared;
public class BotException extends Exception{
	private static final long serialVersionUID = 2445519030930669665L;	
	public static final int INFO_CODE = 200;
	public static final int ERROR_CODE = 400;
	public static final int CRASH_CODE = 500;
	private int errorCode;

	public BotException(String str){
		super(str);
	}
	public BotException(String str, int errorCode){
		super(str);
		this.errorCode = errorCode;
	}
	public BotException(Throwable e){
		super(e);
	}
	public BotException(Throwable e, int errorCode){
		super(e);
		this.errorCode = errorCode;
	}
	public BotException(String str, Throwable e){
		super(str, e);
	}
	public BotException(Exception e, String str, int errorCode) {
		super(str, e);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}
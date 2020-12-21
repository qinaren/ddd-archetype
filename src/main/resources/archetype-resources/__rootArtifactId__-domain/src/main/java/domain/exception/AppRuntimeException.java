#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 自定义异常类
 */
public class AppRuntimeException extends RuntimeException {
	/**唯一ID*/
	private static final long serialVersionUID = 1L;

	/**
	 * 用来反应业务异常的错误码，一般会结合配置文件
	 */
	private String errorCode;

	/**
	 * 用来反应业务异常信息的错误信息，一般会结合配置来说明<code>errorCode</code>
	 */
	private String errorMessage;

	/**
	 * 异常信息描述，用来描述<code>cause</code>
	 */
	private String showMessage;

	/**
	 * 触发该异常的原因
	 */
	private Throwable cause;

	/**默认构造器*/
	public AppRuntimeException() {
	}
	/**构造器*/
	public AppRuntimeException(String errorCode) {
		this.errorCode = errorCode;
	}
	/**构造器*/
	public AppRuntimeException(Throwable cause) {
		super(cause);
	}
	/**构造器*/
	public AppRuntimeException(String errorCode, String errorMessage) {
		//super(showMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
	/**构造器*/
	public AppRuntimeException(String errorCode, String errorMessage, String showMessage) {
		super(showMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.showMessage = showMessage;
	}

	/**构造器*/
	public AppRuntimeException(String showMessage, Throwable cause) {
		super(showMessage, cause);
		this.showMessage = showMessage;
		this.cause = cause;
	}
	/**构造器*/
	public AppRuntimeException(String errorCode, String errorMessage, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.cause = cause;
		this.errorMessage = errorMessage;
	}
	/**构造器*/
	public AppRuntimeException(String errorCode, String errorMessage, String showMessage, Throwable cause) {
		super(showMessage,cause);
		this.cause = cause;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.showMessage=showMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**返回消息*/
	public String getShowMessage() {
		if (this.showMessage == null) {
			return super.getMessage();
		}
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Description:获取异常信息
	 *
	 * @return
	 */
	@Override
	public String getMessage() {
		String msg = super.getMessage();
		String causeMsg = null;
		if (this.cause != null) {
			causeMsg = this.cause.getMessage();
		}
		if (msg != null) {
			if (causeMsg != null) {
				return msg + " caused by: " + causeMsg;
			}
			return msg;
		}
		return causeMsg;
	}

	/**
	 * Description:获取异常堆栈信息
	 *
	 * @return
	 */
	public String getErrorStack() {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bo);
		printStackTrace(ps);
		String errorStack = new String(bo.toByteArray());
		return errorStack;
	}

	@Override
	public Throwable fillInStackTrace(){
		return this;
	}

	@Override
	public String toString() {
		if (this.cause == null) {
			return super.toString();
		}
		return super.toString() + " caused by: " + this.cause.toString();
	}
}

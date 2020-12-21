#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.exception;

/**
 * 统一异常码常量
 * @author ${author}
 * @version 1.0
 * @date 2020-12-21 20:05
 **/
public class ExceptionConstants {
    /**
     * 成功
     */
    public static final String SUCCESS = "SUCCESS";
    /**
     * 其他运行时异常
     */
    public static final String UN_KNOW_EXCEPTION = "UN_KNOW_EXCEPTION";
    /**
     * 参数校验失败
     */
    public static final String PARAM_EXCEPTION = "PARAM_EXCEPTION";

}

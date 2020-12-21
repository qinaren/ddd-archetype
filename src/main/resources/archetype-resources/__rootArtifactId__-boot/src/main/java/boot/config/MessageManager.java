#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot.config;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 支持国际化的信息映射工具类
 * @author ${author}
 * @version 1.0
 * @date 2020-12-21 20:05
 */
@Component
public class MessageManager {
    /**
     * 消息的策略
     */
    private final MessageSource messageSource;

    /**
     * 构造方法
     */
    public MessageManager(MessageSource messageManager) {
        this.messageSource = messageManager;
    }

    /**
     * 根据消息键和参数 获取消息
     * 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return
     */
    public String message(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return message(code,args,locale);
    }

    /**
     *
     * @param code 消费键
     * @param args 参数
     * @param locale 国际化的Locale对象
     * @return
     */
    public String message(String code,Object[] args,Locale locale) {
        if(messageSource == null){
            return null;
        }
        return messageSource.getMessage(code, args, locale);
    }

}

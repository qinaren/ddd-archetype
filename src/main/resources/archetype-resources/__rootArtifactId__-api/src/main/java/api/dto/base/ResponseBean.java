#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api.dto.base;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * API统一响应参数
 * @author ${author}
 * @version 1.0
 * @date 2020-12-21 20:05
 */
@Data
public class ResponseBean<T> implements Serializable {
    /**
     * 响应码
     */
    private String resultCode;
    /**
     * 响应信息
     */
    private String resultInfo;
    /**
     * 响应时间
     */
    private String resTime;
    /**
     * 响应数据
     */
    private T resultData;
    /**
     * 扩展参数
     */
    private HashMap<String, String> extMap;

    /**
     * 默认成功
     * @return 响应对象
     */
    public static <T> ResponseBean<T> success() {
        return build("SUCCESS");
    }

    /**
     * 有返回数据的成功
     * @return 响应对象
     */
    public static <T> ResponseBean<T> success(T resultData) {
        ResponseBean<T> resBean = success();
        resBean.setResultData(resultData);
        return resBean;
    }

    /**
     * 通过响应码构造响应对象
     * @param resultCode 响应码
     * @param <T> 响应数据类型
     * @return 响应对象
     */
    public static <T> ResponseBean<T> build(String resultCode){
        ResponseBean<T> responseBean = new ResponseBean<T>();
        responseBean.setResultCode(resultCode);
        return responseBean;
    }
}

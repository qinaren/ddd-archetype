#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.shared;

import java.io.Serializable;

/**
 * 值对象标识，T为值对象
 * @author ${author}
 * @version 1.0
 * @date 2020-12-21 20:05
 */
public interface ValueObject<T> extends Serializable {
    /**
     *
     * @param other
     * @return
     */
    default boolean sameValueAs(T other){
        if (other == null){
            return false;
        }
        return sameValueAsIsNotNull(other);
    }

    /**
     *
     * @param other
     * @return
     */
    boolean sameValueAsIsNotNull(T other);
}

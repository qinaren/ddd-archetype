#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.shared;

import java.io.Serializable;

/**
 * 聚合标识，T为实体，ID为实体唯一标识
 * @author ${author}
 * @version 1.0
 * @date 2020-12-21 20:05
 */
public interface Entity<T,ID extends ValueObject<ID>> extends Serializable {

    /**
     *
     * @param other
     * @return
     */
    default boolean sameIdentityAs(T other){
        if (other == null){
            return false;
        }
        return sameIdentityAsIsNotNull(other);
    }

    /**
     *
     * @param other
     * @return
     */
    boolean sameIdentityAsIsNotNull(T other);
}

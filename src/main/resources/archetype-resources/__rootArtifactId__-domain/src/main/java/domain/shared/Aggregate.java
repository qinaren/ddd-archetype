#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.shared;

import java.io.Serializable;

/**
 * 聚合标识，T为聚合，ID为唯一标识
 * @author ${author}
 * @version 1.0
 * @date 2020-12-21 20:05
 */
public interface Aggregate<T,ID extends ValueObject<ID>> extends Serializable {
}

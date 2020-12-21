#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

/**
 * 查询服务，针对一些查询服务没必要通过Application、Domain再到Repository。查询服务可以有自己的查询模型，也可以使用Repository里面的PO
 */
package ${package}.query;
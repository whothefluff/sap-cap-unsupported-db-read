package customer.sap_cap_unsupported_db_read.externalcatalogservice.mysql;

import static lombok.AccessLevel.PROTECTED;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sap.cds.ql.cqn.CqnPredicate;
import com.sap.cds.ql.cqn.CqnSearchPredicate;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.CqnSelectListItem;
import com.sap.cds.ql.cqn.CqnSortSpecification;
import com.sap.cds.ql.cqn.CqnSource;

import customer.sap_cap_unsupported_db_read.externalcatalogservice.jpql.CqnParser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.XSlf4j;

@XSlf4j

@Accessors( chain = true,
            fluent = true )
@FieldDefaults( level = PROTECTED,
                makeFinal = true )
@RequiredArgsConstructor
@Component
public class Parser implements CqnParser{

    EntityManager entityManager;
    private static String EMPTY = "";

    public TypedQuery<Object[]> buildFrom( final CqnSelect select, final String entityName ){

        log.entry( select, entityName );
        //new TransformationToSelect( Select.from( select ) ).applyTransformations( );
        val distinctClause = this.distinct( select.isDistinct( ) );
        val selectedFields = this.fields( select.items( ) );
        val filterWhereClause = this.filterWhere( select.from( ) );
        val whereClause = this.where( select.where( ) );
        val whereSearchClause = this.searchWhere( select.search( ), whereClause.toString( ) );
        val groupByClause = this.groupBy( select.groupBy( ) );
        val havingClause = this.having( select.having( ) );
        val orderByClause = this.orderBy( select.orderBy( ) );
        val hints = this.hints( select.hints( ) );
        val offset = this.offset( select.skip( ) );
        val rowLimit = this.rowLimit( select.top( ) );
        val query = new StringBuilder( "SELECT " ).append( distinctClause )
                                                      .append( selectedFields )
                                                      .append( " FROM ").append( entityName )
                                                      .append( filterWhereClause ).append( whereClause ).append( whereSearchClause )
                                                      .append( groupByClause )
                                                      .append( havingClause )
                                                      .append( orderByClause )
                                                      .toString( );
        val jpql = this.entityManager.createQuery( query, Object[].class );
        hints.forEach( jpql::setHint );
        jpql.setFirstResult( offset );
        jpql.setMaxResults( rowLimit );
        return log.exit( jpql );

    }

    @Override
    public String distinct( final Boolean distinct ){

        log.entry( distinct );
        val clause = distinct.equals( Boolean.TRUE ) ? "DISTINCT "
                                                     : EMPTY;
        return log.exit( clause );

    }

    @Override
    public String fields( final List<CqnSelectListItem> fields ){

        log.entry( fields );
        val visitor = new Visitor( );
        fields.forEach( visitor::visit );
        val clause = visitor.stack( ).stream( ).collect( Collectors.joining( ", ") );
        return log.exit( clause );
        
    }

    @Override
    public String filterWhere( final CqnSource source ){

        log.entry( source );
        val visitor = new Visitor( );
        source.accept( visitor );
        val conditions = visitor.stack( ).stream( ).collect( Collectors.joining( ) );
        val clause = conditions.isBlank( ) ? EMPTY
                                           : " WHERE " + conditions;
        return log.exit( clause );

    }

    @Override
    public String where( final Optional<CqnPredicate> where ){

        log.entry( where );
        val visitor = new Visitor( );
        where.ifPresent( w -> w.accept( visitor ) );
        val conditions = visitor.stack( ).stream( ).collect( Collectors.joining( ) );
        val clause = conditions.isBlank( ) ? EMPTY
                                           : " WHERE " + conditions;
        return log.exit( clause );

    }

    @Override
    public String searchWhere( final Optional<CqnPredicate> search, final String whereClause ){

        log.entry( search, whereClause );
        val visitor = new Visitor( );
        search.ifPresent( s -> ( ( CqnSearchPredicate )s ).accept( visitor ) );
        val searchConditions = visitor.stack( ).stream( ).collect( Collectors.joining( ) );
        val clause = searchConditions.isBlank( ) ? EMPTY
                                                 : whereClause.isBlank( ) ? " WHERE " + searchConditions
                                                                          : " AND ( " + searchConditions + " )";
        return log.exit( clause );

    }

    @Override
    public String orderBy( final List<CqnSortSpecification> orderBy ){

        log.entry( orderBy );
        val visitor = new Visitor( );
        orderBy.forEach( visitor::visit );
        val ordering = visitor.stack( ).reversed( ).stream( ).collect( Collectors.joining( ", " ) );
        val clause = ordering.isBlank( ) ? EMPTY
                                         : " ORDER BY " + ordering;
        return log.exit( clause );

    }

    @Override
    public Integer offset( final long offset ){

        log.entry( offset );
        return log.exit( Math.toIntExact( offset ) );

    }

    @Override
    public Integer rowLimit( final long rowLimit ){

        log.entry( rowLimit );
        return log.exit( rowLimit > 0 ? Math.toIntExact( rowLimit )
                                      : Integer.MAX_VALUE );

    }

    @Override 
    public Map<String,Object> hints( final Map<String,Object> hints ){

        log.entry( hints );
        val hintsPlusReadOnly = new HashMap<String,Object>( hints );
        hintsPlusReadOnly.put( "org.hibernate.readOnly", true );
        return log.exit( Collections.unmodifiableMap( hintsPlusReadOnly ) );

    }
    
}
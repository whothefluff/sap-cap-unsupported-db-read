package customer.sap_cap_unsupported_db_read.externalcatalogservice.jpql;

import static com.sap.cds.services.ErrorStatuses.NOT_IMPLEMENTED;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sap.cds.ql.cqn.CqnPredicate;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.CqnSelectListItem;
import com.sap.cds.ql.cqn.CqnSortSpecification;
import com.sap.cds.ql.cqn.CqnSource;
import com.sap.cds.ql.cqn.CqnStructuredTypeRef;
import com.sap.cds.ql.cqn.CqnValue;
import com.sap.cds.services.ServiceException;

/**
 * Understands the elements of a {@link CqnSelect}
 */
public interface CqnParser {

    default public String distinct( final Boolean distinct ){

        this.assertNotUsed( distinct );
        return "";
        
    }

    default public String fields( final List<CqnSelectListItem> fields ){

        throw new ServiceException( NOT_IMPLEMENTED, "Unavailable feature" );
        
    }

    default public String filterWhere( final CqnSource source ){

        this.assertNotUsed( (( CqnStructuredTypeRef )source).rootSegment( ).filter( ).isEmpty( ) );
        return "";

    }

    default public String where( final Optional<CqnPredicate> where ){

        this.assertNotUsed( where.isEmpty( ) );
        return "";

    }

    default public String searchWhere( final Optional<CqnPredicate> search, final String whereClause ){

        this.assertNotUsed( search.isEmpty( ) );
        return "";

    }

    default public String groupBy( final List<CqnValue> groupBy ){

        this.assertNotUsed( groupBy.isEmpty( ) );
        return "";

    }

    default public String having( final Optional<CqnPredicate> having ){

        this.assertNotUsed( having.isEmpty( ) );
        return "";

    }

    default public String orderBy( final List<CqnSortSpecification> orderBy ){

        this.assertNotUsed( orderBy.isEmpty( ) );
        return "";

    }

    default public Integer offset( final long offset ){

        this.assertNotUsed( offset == 0);
        return 0;

    }

    default public Integer rowLimit( final long rowLimit ){

        this.assertNotUsed( rowLimit == 0 );
        return Integer.MAX_VALUE;

    }

    default public Map<String,Object> hints( final Map<String,Object> hints ){

        this.assertNotUsed( hints.isEmpty( ) );
        return Collections.unmodifiableMap( Collections.emptyMap( ) );

    }

    private void assertNotUsed( final boolean condition ){

        if( !condition ){
            throw new ServiceException( NOT_IMPLEMENTED, "Unavailable feature" );
        }

    }

}

package customer.sap_cap_unsupported_db_read.externalcatalogservice.mysql;

import com.sap.cds.ql.cqn.CqnSearchPredicate;

import customer.sap_cap_unsupported_db_read.externalcatalogservice.jpql.CqnVisitor;

public class Visitor extends CqnVisitor {

    @Override
    public void visit( final CqnSearchPredicate search ){

        this.stack.poll( );
        this.stack.push( "name LIKE '%" + search.searchTerm( ) + "%' OR ID LIKE '%" + search.searchTerm( ) + "%'" );

    }
    
}

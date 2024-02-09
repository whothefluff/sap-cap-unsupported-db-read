package customer.sap_cap_unsupported_db_read.externalcatalogservice;

import org.springframework.stereotype.Component;

import com.sap.cds.Result;
import com.sap.cds.ResultBuilder;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.util.CqnStatementUtils;

import cds.gen.db.external.Sites_;
import cds.gen.externalcatalogservice.ExternalCatalogService_;
import cds.gen.externalcatalogservice.PointOfInterests_;
import customer.sap_cap_unsupported_db_read.externalcatalogservice.mysql.Parser;
import customer.sap_cap_unsupported_db_read.externalcatalogservice.mysql.QueryProcessor;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.XSlf4j;

@XSlf4j
@AllArgsConstructor
@Component
@ServiceName( ExternalCatalogService_.CDS_NAME )
public class PointOfInterestsHandler implements EventHandler {

    Parser cqnParser;
    QueryProcessor queryProcessor;
    
    @On( entity = PointOfInterests_.CDS_NAME )
    Result readFromExternalDatasource( final CdsReadEventContext context ){

        log.entry( context );
        val cqnSelect = context.getCqn( );
        log.info( "Processing request {}", cqnSelect.toJson( ) );
        val currentEntity = Sites_.CDS_NAME.substring( Sites_.CDS_NAME.lastIndexOf( '.' ) + 1 );
        val jpqlQuery = this.cqnParser.buildFrom( cqnSelect, currentEntity );
        val queryResult = this.queryProcessor.execute( jpqlQuery );
        val resultType = CqnStatementUtils.rowType( context.getModel( ), cqnSelect );
        val mappedResults = new PointOfInterestODataRows( resultType ).mapRawResults( queryResult );
        val capResult = ResultBuilder.selectedRows( mappedResults );
        capResult.entity( context.getTarget( ) );
        capResult.rowType( resultType ); 
        if( cqnSelect.hasInlineCount( ) ){
            capResult.inlineCount( mappedResults.size( ) );
        }
        return log.exit( capResult.result( ) );

    }

}
package customer.sap_cap_unsupported_db_read.externalcatalogservice;

import static lombok.AccessLevel.PROTECTED;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sap.cds.reflect.CdsElement;
import com.sap.cds.reflect.CdsStructuredType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.XSlf4j;;

@XSlf4j
@RequiredArgsConstructor
@Accessors( chain = true,
            fluent = true )
@FieldDefaults( level = PROTECTED,
                makeFinal = true )
public class PointOfInterestODataRows {

    CdsStructuredType type;
    @Getter( lazy = true )
    private List<String> fieldNames = this.initializeFieldNames( );

    protected List<String> initializeFieldNames( ){

        val fields = this.type.elements( ).collect( Collectors.toList( ) );
        val fieldNames = fields.stream( ).map( CdsElement::getName ).collect( Collectors.toList( ) );
        return fieldNames;

    }

    public List<Map<String, Object>> mapRawResults( final List<Object[]> results ){

        log.entry( results );
        val dummyMergeFunction = ( BinaryOperator<Object> )( u, v ) -> u;
        val numberOfFields = this.fieldNames( ).size( );
        val toRow = ( Function<Object[],Map<String, Object>> )row -> {
            Collector<Integer, ?, LinkedHashMap<String, Object>> mappedRow;
            mappedRow = Collectors.toMap( i -> this.fieldNames( ).get( i ), 
                                          i -> row[i] != null ? row[i] : "",
                                          dummyMergeFunction,
                                          ( ) -> new LinkedHashMap<String, Object>( numberOfFields ) );
            return IntStream.range( 0, row.length ).boxed( ).collect( mappedRow );
        };
        val mappedResults = results.stream( ).map( toRow ).collect( Collectors.toList( ) );
        return mappedResults;

    }

}
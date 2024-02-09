package customer.sap_cap_unsupported_db_read.externalcatalogservice.mysql;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
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
public class QueryProcessor {

    public List<Object[]> execute( final TypedQuery<Object[]> jpql ){

        log.entry( jpql );
        return log.exit( jpql.getResultList( ) );

    }
    
}

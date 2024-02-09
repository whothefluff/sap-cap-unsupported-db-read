package customer.sap_cap_unsupported_db_read.externalcatalogservice;

import java.math.BigDecimal;

import cds.gen.db.external.Sites;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity( name = "sites" )
public class PointOfInterestsEntity {

    @Id
    @Column( name = Sites.SITE )
    String ID;
    @Column( name = Sites.DS )
    String name;
    @Column( name = Sites.ID_COUNTRY )
    String country;
    @Column( name = Sites.CAT1 )
    String category;
    @Column( name = Sites.CAT2 )
    String subcategory;
    @Column( name = Sites.LONGITUDE_WGS84 )
    BigDecimal longitude;
    @Column( name = Sites.LATITUDE_WGS84 )
    BigDecimal latitude;

}
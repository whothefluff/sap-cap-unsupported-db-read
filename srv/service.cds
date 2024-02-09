using { db.external as db } from '../db/model';

service ExternalCatalogService { 

    @readonly
    entity PointOfInterests as projection on db.sites { 
        SITE as ID, 
        DS as name,
        CAT1 as category,
        CAT2 as subcategory,
        ID_COUNTRY as country,
        LONGITUDE_WGS84 as longitude,
        LATITUDE_WGS84 as latitude
    };

}
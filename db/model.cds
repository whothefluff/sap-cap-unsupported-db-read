namespace db.external;

@cds.persistence.skip
entity sites {

    key SITE :String(100);
    DS :String(100);
    CAT1 :String(100);
    CAT2 :String(100);
    ID_COUNTRY :String(2);
    LONGITUDE_WGS84 :Decimal(38,8);
    LATITUDE_WGS84 :Decimal(38,8);

}
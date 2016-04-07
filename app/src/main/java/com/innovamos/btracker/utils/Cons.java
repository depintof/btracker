package com.innovamos.btracker.utils;

/**
 * Cons File
 */
public class Cons {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;

    /**
     * Puerto para la conexión.
     */
    private static final String PUERTO_HOST = "80";

    /**
     * Dirección IP de Webservices
     */
    private static final String IP = "http://btrackerws.exeamedia.com:";

    /**
     * URLs del Web Service: Obtener Lista de Beacons
     */
    public static final String GET_ALL_BEACONS = IP + PUERTO_HOST + "/get_beacons.php";

    /**
     * URLs del Web Service: Obtener Dispositivo
     */
    public static final String GET_CUSTOMER = IP + PUERTO_HOST + "/get_customer.php";

    /**
     * URLs del Web Service: Obtener la Zona Asociada a un Beacon
     */
    public static final String GET_ZONE = IP + PUERTO_HOST + "/get_zones.php";

    /**
     * URLs del Web Service: Obtener Lista de Productos Asociados a una Zona
     */
    public static final String GET_PRODUCTS_ZONE_LIST = IP + PUERTO_HOST + "/get_products_zone_list.php";

    /**
     * Signo de consulta
     */
    public static final String QUESTION_MARK = "?";

    /**
     * Signo de igual
     */
    public static final String EQUAL_MARK = "=";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

    /*
        Tag 'mac' para respuestas de web-services
     */
    public static final String MAC = "mac";

    /*
        Tag 'beacon_id' para respuestas de web-services
     */
    public static final String BEACON_ID = "beacon_id";

    /*
        Tag 'zone_id' para respuestas de web-services
     */
    public static final String ZONE_ID = "zone_id";

    /*
        Tag 'estado' para respuestas de web-services
     */
    public static final String STATUS = "estado";

    /*
        Tag 'beacons' para respuestas de web-services
     */
    public static final String BEACONS = "beacons";

    /*
        Tag 'estado' para respuestas de web-services
     */
    public static final String CUSTOMERS = "customers";

    /*
        Tag 'products' para respuestas de web-services
     */
    public static final String ZONES = "zones";

    /*
        Tag 'products' para respuestas de web-services
     */
    public static final String PRODUCTS = "products";

    /*
        Valor exitoso de la variable estado para respuestas de web-services
     */
    public static final String STATUS_SUCCESS = "1";

    /*
        Valor fallido de la variable estado para respuestas de web-services
     */
    public static final String STATUS_FAIL  = "2";
}

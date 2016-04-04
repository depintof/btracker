package com.btracker.test9.utils;

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
     * URLs del Web Service
     */
    public static final String GET_ALL_BEACONS = IP + PUERTO_HOST + "/get_beacons.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

    /*
        Tag 'estado' para respuestas de web-services
     */
    public static final String STATUS = "estado";

    /*
        Tag 'estado' para respuestas de web-services
     */
    public static final String BEACONS = "beacons";

    /*
        Valor exitoso de la variable estado para respuestas de web-services
     */
    public static final String STATUS_SUCCESS = "1";

    /*
        Valor fallido de la variable estado para respuestas de web-services
     */
    public static final String STATUS_FAIL  = "2";
}

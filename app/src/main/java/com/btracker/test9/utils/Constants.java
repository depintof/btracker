package com.btracker.test9.utils;

/**
 * Constants File
 */
public class Constants {
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
    private static final String IP = "http://192.168.0.24:";

    /**
     * URLs del Web Service
     */
    public static final String GET = IP + PUERTO_HOST + "/btracker/get_beacons.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";
}

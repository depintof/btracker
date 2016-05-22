package com.innovamos.btracker.utils;

/**
 * Cons File
 */
public class Cons {

    /***** GENERALES *****/
    /**
     * Puerto para la conexión.
     */
    private static final String PUERTO_HOST = "80";

    /**
     * Dirección IP de Webservices
     */
    private static final String IP = "http://btrackerws.exeamedia.com:";

    /***** URL´s *****/
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
    public static final String GET_PRODUCTS_ZONE_LIST = IP + PUERTO_HOST + "/get_products.php";

    /**
     * URLs del Web Service: Insertar Campo con Compra de Producto
     */
    public static final String INSERT_PRODUCT_PURCHASE = IP + PUERTO_HOST + "/set_product_purchase.php";

    /**
     * URLs del Web Service: Obtener Lista de Productos Comprados
     */
    public static final String GET_PURCHASED_PRODUCTS = IP + PUERTO_HOST + "/get_purchased_products.php";

    /**
     * URLs del Web Service: Eliminar Producto Comprado
     */
    public static final String DELETE_PRODUCT_PURCHASE = IP + PUERTO_HOST + "/del_product_purchase.php";

    /**
     * URLs del Web Service: Insertar Campo con Like de Producto
     */
    public static final String INSERT_PRODUCT_LIKE = IP + PUERTO_HOST + "/set_customers_products.php";

    /**
     * URLs del Web Service: Obtener Lista de Productos con Like
     */
    public static final String GET_PRODUCTS_LIKE = IP + PUERTO_HOST + "/get_customers_products.php";

    /**
     * URLs del Web Service: Eliminar Producto con Like
     */
    public static final String DELETE_PRODUCT_LIKE = IP + PUERTO_HOST + "/del_customers_products.php";

    /***** SIMBOLOS DE CONSULTA *****/
    /**
     * Signo de consulta
     */
    public static final String QUESTION_MARK = "?";

    /**
     * Signo de igual
     */
    public static final String EQUAL_MARK = "=";

    /**
     * Signo para agregar parámetros
     */
    public static final String AND = "&&";

    /***** TAGS DE CONSULTAS *****/
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
        Tag 'customers' para respuestas de web-services
     */
    public static final String CUSTOMERS = "customers";

    /*
        Tag 'zones' para respuestas de web-services
     */
    public static final String ZONES = "zones";

    /*
        Tag 'products' para respuestas de web-services
     */
    public static final String PRODUCTS = "products";

    /*
        Tag 'customer_id' para respuestas de web-services
     */
    public static final String CUSTOMER_ID = "customer_id";

    /*
        Tag 'product_id' para respuestas de web-services
     */
    public static final String PRODUCT_ID = "product_id";

    /*
        Tag 'customersProducts' para respuestas de web-services
     */
    public static final String PRODUCTS_LIKES = "customersProducts";

    /*
        Tag 'purchases' para respuestas de web-services
     */
    public static final String PURCHASES = "purchases";

    /*
        Tag 'price' para respuestas de web-services
     */
    public static final String PRICE = "price";

    /***** ESTADOS RESPUESTAS *****/
    /*
        Valor exitoso de la variable estado para respuestas de web-services
     */
    public static final String STATUS_SUCCESS = "1";

    /*
        Valor fallido de la variable estado para respuestas de web-services
     */
    public static final String STATUS_FAIL  = "2";

}

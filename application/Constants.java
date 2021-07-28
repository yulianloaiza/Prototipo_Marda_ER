package application;

/**
 * Clase donde se almacenan las constantes que se utilizan en el marda.
 */
public final class Constants {
    private Constants(){}
    //NAME APP
    public static final String NAME_APP = "CAT Diagrammer";

    //DIAGRAMAS
    public static final String DIAGRAM_ER = "ER";
    public static final String DIAGRAM_REDES = "Redes";

    //NODOS ER
    public static final String NODE_ENTITY = "Entity";
    public static final String NODE_VIEW = "View";
    public static final String NODE_TRIGGER = "Trigger";
    public static final String NODE_SP = "StoredProcedure";
    public static final String NODE_SPRS = "Result Set";
    public static final String NODE_NOTE = "Nota";

    //NODOS REDES
    public static final String NODE_LAPTOP = "Laptop";
    public static final String NODE_ROUTER = "Router";
    public static final String NODE_PC = "PC";
    public static final String NODE_TELEPHONE = "Telephone";
    public static final String NODE_SERVER = "Server";
    public static final String NODE_INTERNET = "Internet";
    public static final String NODE_SWITCH = "Switch";
    public static final String NODE_WLAN = "Wlan Controller";
    public static final String NODE_FIREWALL = "Firewall";

    //ENLACES ER
    public static final String LINK_ONETOONE = "Uno a Uno";
    public static final String LINK_ONETOMANY = "Uno a Muchos";
    public static final String LINK_MANYTOMANY = "Muchos a Muchos";
    public static final String LINK_GENERIC = "Generico";
    public static final String LINK_ANCHOR = "Anchor";

    //ENLACES REDES
    public static final String LINK_WIRELESS = "Wireless";
    public static final String LINK_WIRED = "Wired";

    //Flechas
    public static final String ARROW_ONE = "M 0 2 V 3 H 2 V 10 H 2 V 3 H 5 V 2 H 3 V 0 H 2 V 2 Z";
    public static final String ARROW_MANY = "M 13 13 C 10 10 13 13 10 10 H 16 C 13 13 16 10 13 13 Z C 11 13 11 16 13 16 C 15 16 15 13 13 13";
    public static final String ARROW_GENERIC = "M 1 4 H 5 C 3 1 5 4 3 1 M 6 1 V 1 M 6 7 H 0 Z";
    public static final String ARROW_ANCHOR = "M 0 0 H 9 M 4 7";

}

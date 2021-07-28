package application.utils.factories;

import application.Constants;
import application.models.Enlace.Anchor;
import application.models.Enlace.EnlaceAbs;
import application.models.Enlace.Redes.Wired;
import application.models.Enlace.Redes.Wireless;
import application.models.Nodo.Redes.*;
import application.models.Nodo.NodoAbs;
import application.models.Nodo.Nota;

/**
 * Clase FactoryRedes, representa la fábrica encargada de construir los componentes del diagrama
 * de tipo Redes (Network).
 */
public class FactoryRedes extends FactoryAbs{

    public FactoryRedes(){
        this.setTypeFactory(Constants.DIAGRAM_REDES);
        setupNodesStr();
        setupEdgesStr();
        createMetaClasses();
    }

    @Override
    public NodoAbs buildNodo(String type) {
        NodoAbs mkNode = null;

        switch (type) {
            case Constants.NODE_PC:
                mkNode = new PC();
                break;
            case Constants.NODE_LAPTOP:
                mkNode = new Laptop();
                break;
            case Constants.NODE_TELEPHONE:
                mkNode = new Telephone();
                break;
            case Constants.NODE_INTERNET:
                mkNode = new Internet();
                break;
            case Constants.NODE_SERVER:
                mkNode = new Server();
                break;
            case Constants.NODE_FIREWALL:
                mkNode = new Firewall();
                break;
            case Constants.NODE_ROUTER:
                mkNode = new Router();
                break;
            case Constants.NODE_SWITCH:
                mkNode = new Switch();
                break;
            case Constants.NODE_WLAN:
                mkNode = new WLAN();
                break;
            default:
                mkNode = new Nota();
                break;
        }
        return  mkNode;
    }

    @Override
    public EnlaceAbs buildEnlace(String type) {
        EnlaceAbs mkEnlace = null;
        switch(type){
            case Constants.LINK_WIRED:
                mkEnlace = new Wired();
                break;
            case Constants.LINK_WIRELESS:
                mkEnlace = new Wireless();
                break;
            default:
                mkEnlace = new Anchor();
                break;
        }
        return mkEnlace;
    }

    @Override
    protected void setupNodesStr() {
        nodes_str.add(Constants.NODE_PC);
        nodes_str.add(Constants.NODE_LAPTOP);
        nodes_str.add(Constants.NODE_TELEPHONE);
        nodes_str.add(Constants.NODE_INTERNET);
        nodes_str.add(Constants.NODE_SERVER);
        nodes_str.add(Constants.NODE_FIREWALL);
        nodes_str.add(Constants.NODE_ROUTER);
        nodes_str.add(Constants.NODE_SWITCH);
        nodes_str.add(Constants.NODE_WLAN);
        nodes_str.add(Constants.NODE_NOTE);
    }

    @Override
    protected void setupEdgesStr() {
        edges_str.add(Constants.LINK_WIRED);
        edges_str.add(Constants.LINK_WIRELESS);
        edges_str.add(Constants.LINK_ANCHOR);
    }

    @Override
    protected void createMetaClasses() {
        //Crear nodos
        for(String nodo : nodes_str){
            types_nodes.add(buildNodo(nodo));
        }

        //Crear enlaces
        for(String enlace : edges_str){
            types_edges.add(buildEnlace(enlace));
        }
    }
}

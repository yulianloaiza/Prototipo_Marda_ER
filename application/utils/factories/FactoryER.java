package application.utils.factories;

import application.Constants;
import application.models.Enlace.*;
import application.models.Enlace.ER.Generica;
import application.models.Enlace.ER.MuchosAMuchos;
import application.models.Enlace.ER.UnoAMuchos;
import application.models.Enlace.ER.UnoAUno;
import application.models.Nodo.*;
import application.models.Nodo.ER.*;


/**
 * Clase FactoryER, representa la fábrica encargada de construir los componentes del diagrama
 * de tipo Entity Relationship.
 */
public class FactoryER extends FactoryAbs{

    public FactoryER(){
        this.setTypeFactory(Constants.DIAGRAM_ER);
        setupNodesStr();
        setupEdgesStr();
        createMetaClasses();
    }

    @Override
    public NodoAbs buildNodo(String type) {
        NodoAbs mkNode = null;

        switch (type) {
            case Constants.NODE_ENTITY:
                mkNode = new Entity();
                break;
            case Constants.NODE_VIEW:
                mkNode = new View();
                break;
            case Constants.NODE_TRIGGER:
                mkNode = new Trigger();
                break;
            case Constants.NODE_SP:
                mkNode = new StoredProcedure();
                break;
            case Constants.NODE_SPRS:
                mkNode = new StoredProcedureRS();
                break;
            default:
                mkNode = new Nota();
                break;
        }
        return mkNode;
    }

    @Override
    public EnlaceAbs buildEnlace(String type) {
        EnlaceAbs mkEnlace = null;
        switch(type){
            case Constants.LINK_ANCHOR:
                mkEnlace = new Anchor();
                break;
            case Constants.LINK_GENERIC:
                mkEnlace = new Generica();
                break;
            case Constants.LINK_MANYTOMANY:
                mkEnlace = new MuchosAMuchos();
                break;
            case Constants.LINK_ONETOMANY:
                mkEnlace = new UnoAMuchos();
                break;
            case Constants.LINK_ONETOONE:
                mkEnlace = new UnoAUno();
                break;
        }
        return mkEnlace;
    }

    @Override
    protected void setupNodesStr() {
        nodes_str.add(Constants.NODE_ENTITY);
        nodes_str.add(Constants.NODE_VIEW);
        nodes_str.add(Constants.NODE_TRIGGER);
        nodes_str.add(Constants.NODE_SP);
        nodes_str.add(Constants.NODE_SPRS);
        nodes_str.add(Constants.NODE_NOTE);
    }

    @Override
    protected void setupEdgesStr() {
        edges_str.add(Constants.LINK_ONETOONE);
        edges_str.add(Constants.LINK_ONETOMANY);
        edges_str.add(Constants.LINK_MANYTOMANY);
        edges_str.add(Constants.LINK_GENERIC);
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

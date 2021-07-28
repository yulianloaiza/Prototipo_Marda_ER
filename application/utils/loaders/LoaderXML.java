package application.utils.loaders;

import application.models.Nodo.*;
import application.utils.factories.FactoryAbs;
import application.utils.factories.FactoryFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Vector;

/**
 * Clase de loaders concreta que crea objetos a partir de archivos .xml
 */
public class LoaderXML extends LoaderAbs{

    public LoaderXML(){}

    @Override
    public FactoryAbs cargar(String path) {
        DocumentBuilderFactory factoryDoc = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factoryDoc.newDocumentBuilder();

            File file = new File(path);
            Document doc = builder.parse(file);
            Element element = doc.getDocumentElement();

            String tipoD = element.getElementsByTagName("TIPODIAGRAMA").item(0).getTextContent();
            System.out.println(tipoD);
            FactoryFactory factoryFac = new FactoryFactory();
            this.factory = factoryFac.buildFactory(tipoD);

            NodeList nodes = element.getElementsByTagName("NODO");
            nodosAbs = new Vector<>();
            enlacesNuevos = new Vector<>();

            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println(nodes.item(i));
                nodosAbs.add(construirNodo(nodes.item(i)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.factory;
    }

    /**
     * Método encargado de construir los objetos nodo en base al archivo .xml.
     * @param nodoXML Node de tipo xml el cual contiene la información del nodo del diagrama
     * que se va a crear.
     * @return NodoAbs que corresponde a alguno de los diagramas.
     */
    public NodoAbs construirNodo(Node nodoXML){
        NodoAbs nodoDiagrama = null;
        if(nodoXML.getNodeType() == Node.ELEMENT_NODE){
            Element elementoXML = (Element) nodoXML;
            String tipo = elementoXML.getElementsByTagName("TIPO").item(0).getTextContent();
            nodoDiagrama = this.factory.buildNodo(tipo); //Nos devuelve el tipo apropiado usando factory

            nodoDiagrama.setId(elementoXML.getElementsByTagName("ID").item(0).getTextContent());
            nodoDiagrama.setTitulo(elementoXML.getElementsByTagName("TITULO").item(0).getTextContent());
            nodoDiagrama.setCoordX(Double.parseDouble(elementoXML.getElementsByTagName("COORDX").item(0).getTextContent()));
            nodoDiagrama.setCoordY(Double.parseDouble(elementoXML.getElementsByTagName("COORDY").item(0).getTextContent()));
            nodoDiagrama.setTieneTitulo(Boolean.valueOf(elementoXML.getElementsByTagName("TIENETITULO").item(0).getTextContent()));
            nodoDiagrama.setTieneTexto(Boolean.valueOf(elementoXML.getElementsByTagName("TIENETEXTO").item(0).getTextContent()));
            nodoDiagrama.setTieneTexto2(Boolean.valueOf(elementoXML.getElementsByTagName("TIENETEXTO2").item(0).getTextContent()));
            nodoDiagrama.setTieneDivisionCampos(Boolean.valueOf(elementoXML.getElementsByTagName("TIENEDIVISIONCAMPOS").item(0).getTextContent()));
            nodoDiagrama.setCamposHabilitados(camposInteger(elementoXML.getElementsByTagName("CAMPOSHABILITADOS").item(0).getTextContent()));
            nodoDiagrama.setCamposOcupados(camposInteger(elementoXML.getElementsByTagName("CAMPOSOCUPADOS").item(0).getTextContent()));
            nodoDiagrama.setContenido(insertarContenido(elementoXML,"CONTENIDO"));
            nodoDiagrama.setContenido2(insertarContenido(elementoXML,"CONTENIDO2"));
            getVectorEnlace((Element) elementoXML.getElementsByTagName("ENLACES").item(0));
        }
        return nodoDiagrama;
    }

    /**
     * Método encargado de convertir el string de campos habilitados/ocupados en un vector
     * de Integer.
     * @param campos hilera de campos a parsear a un vector.
     * @return Vector de Integer que corresponde a los campos habilitados/ocupados del nodo.
     */
    public Vector<Integer> camposInteger(String campos){
        Vector <Integer> camposInt = new Vector<>();
        String[] camposStrArray = campos.split("@");
        if(campos.length() > 2) {
            for (int i = 0; i < camposStrArray.length; i++) {
                camposInt.add(Integer.valueOf(camposStrArray[i]));
            }
        }
        return camposInt;
    }

    /**
     * Método encargado de insertar el contenido de texto a un nodo.
     * @param elemento de tipo xml de donde se va a extraer el contenido.
     * @param nombreContenido Nombre del contenido
     * @return Vector de Strings donde se almacenó el contenido.
     */
    public Vector<String> insertarContenido(Element elemento, String nombreContenido){
        Vector<String> contenidos = new Vector<>();
        Node contenidosXML = elemento.getElementsByTagName(nombreContenido).item(0);
        Element elem = (Element) contenidosXML;
        String nombre = "";
        int cantidadLineas = Integer.valueOf(elem.getAttribute("cantidad"));
        for(int i = 0; i < cantidadLineas; i++){
            nombre = "LINEA_" + String.valueOf(i);
            contenidos.add(elem.getElementsByTagName(nombre).item(0).getTextContent());
        }
        return contenidos;
    }

    /**
     * Método encargado de formar un enlace en un vector de Strings a partir de su información.
     * @param nodoXML de tipo xml de donde se va a extraer la información del enlace.
     * @return Vector de strings donde se almacenó la información del enlace.
     */
    public Vector<String> construirEnlace(Node nodoXML){
        Vector<String> enlaceNuevo = new Vector<>();
        if(nodoXML.getNodeType() == Node.ELEMENT_NODE){
            Element enlaceXML = (Element) nodoXML;
            enlaceNuevo.add(enlaceXML.getElementsByTagName("ID").item(0).getTextContent());
            enlaceNuevo.add(enlaceXML.getElementsByTagName("TIPO").item(0).getTextContent());
            enlaceNuevo.add(enlaceXML.getElementsByTagName("FUENTE").item(0).getTextContent());
            enlaceNuevo.add(enlaceXML.getElementsByTagName("DESTINO").item(0).getTextContent());
            enlaceNuevo.add(enlaceXML.getElementsByTagName("PUERTOFUENTE").item(0).getTextContent());
            enlaceNuevo.add(enlaceXML.getElementsByTagName("PUERTODESTINO").item(0).getTextContent());
        }
        return enlaceNuevo;
    }

    /**
     * Método encargado de crear un Vector con todos los enlaces para un determinado nodo.
     * @param elemento de tipo xml que representa el nodo en específico que contiene los enlaces.
     */
    public void getVectorEnlace(Element elemento){
        NodeList enlacesXML = elemento.getElementsByTagName("ENLACE");
        for(int i = 0; i < enlacesXML.getLength(); i++) {
            enlacesNuevos.add(construirEnlace(enlacesXML.item(i)));
        }
    }
}

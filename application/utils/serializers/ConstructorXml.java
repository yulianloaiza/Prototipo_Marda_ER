package application.utils.serializers;

import application.models.Nodo.NodoAbs;
import application.models.Enlace.EnlaceAbs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;
import java.util.Vector;

/**
 * Clase concreta para serializar el diagrama en formato JSON
 */
public class ConstructorXml extends ConstructorSerializerAbs {

    Element rootElement;
    Document document;
    String deafult = " ";

    @Override
    public void serialize(NodoAbs nodo){
        Element unNodo = this.document.createElement("NODO");
        this.rootElement.appendChild(unNodo);


        Element IdNodo = this.document.createElement("ID");
        IdNodo.appendChild(this.document.createTextNode(nodo.getId()));
        unNodo.appendChild(IdNodo);

        Element tipo = this.document.createElement("TIPO");
        tipo.appendChild(this.document.createTextNode(nodo.getTipo()));
        unNodo.appendChild(tipo);

        Element titulo = this.document.createElement("TITULO");
        titulo.appendChild(this.document.createTextNode(nodo.getTitulo()));
        unNodo.appendChild(titulo);

        Element coordX = this.document.createElement("COORDX");
        coordX.appendChild(this.document.createTextNode(String.valueOf(nodo.getCoordX())));
        unNodo.appendChild(coordX);

        Element coordY = this.document.createElement("COORDY");
        coordY.appendChild(this.document.createTextNode(String.valueOf(nodo.getCoordY())));
        unNodo.appendChild(coordY);

        Element tieneTitulo = this.document.createElement("TIENETITULO");
        tieneTitulo.appendChild(this.document.createTextNode(String.valueOf(nodo.getTieneTitulo())));
        unNodo.appendChild(tieneTitulo);

        Element tieneTexto = this.document.createElement("TIENETEXTO");
        tieneTexto.appendChild(this.document.createTextNode(String.valueOf(nodo.getTieneTexto())));
        unNodo.appendChild(tieneTexto);

        Element tieneTexto2 = this.document.createElement("TIENETEXTO2");
        tieneTexto2.appendChild(this.document.createTextNode(String.valueOf(nodo.getTieneTexto2())));
        unNodo.appendChild(tieneTexto2);

        Element tieneDivisionCampos = this.document.createElement("TIENEDIVISIONCAMPOS");
        tieneDivisionCampos.appendChild((this.document.createTextNode(String.valueOf(nodo.getTieneDivisionCampos()))));
        unNodo.appendChild(tieneDivisionCampos);

        Element camposHabilitados = this.document.createElement("CAMPOSHABILITADOS");
        camposHabilitados.appendChild(this.document.createTextNode(camposString(nodo.getCamposHabilitados())));
        unNodo.appendChild(camposHabilitados);

        Element camposOcupados = this.document.createElement("CAMPOSOCUPADOS");
        camposOcupados.appendChild(this.document.createTextNode(camposString(nodo.getCamposOcupados())));
        unNodo.appendChild(camposOcupados);

        Element enlaces = this.document.createElement("ENLACES");

        Map<String, EnlaceAbs> mapaEnlaces = nodo.getEnlacesMap();

        for (Map.Entry<String, EnlaceAbs> entry : mapaEnlaces.entrySet()) {
            if (nodo.getId().equals(entry.getValue().getFuente().getId())) {
                Element enlace = this.document.createElement("ENLACE");
                EnlaceAbs unEnlace = entry.getValue();

                Element enlaceId = this.document.createElement("ID");
                enlaceId.appendChild(this.document.createTextNode(unEnlace.getId()));
                enlace.appendChild(enlaceId);

                Element tipoEnlace = this.document.createElement("TIPO");
                tipoEnlace.appendChild(this.document.createTextNode(unEnlace.getTipo()));
                enlace.appendChild(tipoEnlace);

                Element fuente = this.document.createElement("FUENTE");
                fuente.appendChild(this.document.createTextNode(unEnlace.getFuente().getId()));
                enlace.appendChild(fuente);

                Element destino = this.document.createElement("DESTINO");
                destino.appendChild(this.document.createTextNode(unEnlace.getDestino().getId()));
                enlace.appendChild(destino);

                Element puertoF = this.document.createElement("PUERTOFUENTE");
                puertoF.appendChild(this.document.createTextNode(String.valueOf(unEnlace.getPuertoFuente())));
                enlace.appendChild(puertoF);

                Element puertoD = this.document.createElement("PUERTODESTINO");
                puertoD.appendChild(this.document.createTextNode(String.valueOf(unEnlace.getPuertoDestino())));
                enlace.appendChild(puertoD);

                enlaces.appendChild(enlace);
            }
        }
        enlaces.appendChild(this.document.createTextNode(deafult));
        unNodo.appendChild(enlaces);

        Vector<String> contenidoVector = nodo.getContenido();
        unNodo.appendChild(agregarContenido(contenidoVector, "CONTENIDO"));
        Vector<String> contenidoVector2 = nodo.getContenido2();
        unNodo.appendChild(agregarContenido(contenidoVector2, "CONTENIDO2"));
    }

    /**
     * Método encargado de agregar el contenido del nodo al archivo xml.
     * @param contenidoVector contenido del nodo en el vector.
     * @param nombreContenido tag con el cual se va a identificar el contenido.
     * @return elemento xml que se va a agregar al nodo en el archivo xml.
     */
    public Element agregarContenido(Vector<String> contenidoVector, String nombreContenido){
        Element contenido = this.document.createElement(nombreContenido);
        contenido.setAttribute("cantidad", String.valueOf(contenidoVector.size()));
        if(contenidoVector.size() > 0) {
            for (int i = 0; i < contenidoVector.size(); i++) {
                Element linea = this.document.createElement("LINEA_" + String.valueOf(i));
                linea.appendChild(this.document.createTextNode(contenidoVector.get(i)));
                contenido.appendChild(linea);
            }
        }else{
            contenido.appendChild(this.document.createTextNode(" "));
        }
        return contenido;
    }

    /**
     * Método encargado de convertir el Vector de campos habilitados/ocupados en un String.
     * @param vec Vector de campos habilitados/ocupados.
     * @return String que corresponde a los campos habilitados/ocupados del nodo.
     */
    public String camposString(Vector<Integer> vec){
        String campos = "";
        if(vec.size() > 0) {
            for (int i = 0; i < vec.size(); i++) {
                campos += String.valueOf(vec.get(i)) + "@";
            }
        }else{
            campos = " ";
        }
        return campos;
    }

    @Override
    public void iniciar(String path, String typeDiagram){
        this.path = path;
        this.tipoDiagrama = typeDiagram;
        System.out.println("Exportando en XML...");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("CANVAS");
            document.appendChild(rootElement);
            this.document = document;
            this.rootElement = rootElement;
            Element tipoD = this.document.createElement("TIPODIAGRAMA");
            tipoD.appendChild(this.document.createTextNode(this.tipoDiagrama));
            this.rootElement.appendChild(tipoD);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }

    }

    @Override
    public void finalizar() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(this.document);
            StreamResult streamResult = new StreamResult(new File(this.path));
            transformer.transform(domSource, streamResult);
            System.out.println("Done creating XML File");
        }catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}

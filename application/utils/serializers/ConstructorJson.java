package application.utils.serializers;

import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Clase concreta para serializar el diagrama en formato JSON
 */
public class ConstructorJson extends ConstructorSerializerAbs {

    Gson gson;
    Writer writer; //Para appendable writer de Gson
    boolean Notfirst = false;

    @Override
    public void serialize(NodoAbs nodo){
        if(Notfirst){
            try {
                writer.write(", ");
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Notfirst=true;
        Type typeOfSrcMethod = new TypeToken<NodoAbs>(){}.getType();
        gson.toJson(nodo, typeOfSrcMethod, writer);

        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finalizar() {
        try {
            writer.write(" ]");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void iniciar(String path, String typeDiagram) {
        this.path = path;
        this.tipoDiagrama = typeDiagram;
        System.out.println("Exportando en Json...");
        {
            try {
                writer = new FileWriter(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gson = new GsonBuilder().registerTypeAdapter(NodoAbs.class, new ConstructorJson.NodoAbsSerializer())
                .setPrettyPrinting().create();
        try {
            writer.write("[ ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Override del metodo para serializar 'NodoAbs' que utiliza Gson. En vez de hacerlo automaticamente,
     * Gson sigue las intrucciones dadas en este metodo para saber como serializar Nodos.
     * Dado un nodo, podemos guardar tanto sus atributos, como los enlaces que el origina, si es que tiene.
     */
    class NodoAbsSerializer implements JsonSerializer<NodoAbs> {
        @Override
        public JsonElement serialize(NodoAbs src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject nodoBody = new JsonObject();
            //Lleno con todos sus atributos. Aqui llega el nodo padre.
            nodoBody.add("TIPODIAGRAMA", context.serialize(tipoDiagrama));
            nodoBody.add("ID", context.serialize(src.getId()));
            nodoBody.add("TIPO", context.serialize(src.getTipo()));
            nodoBody.add("TITULO", context.serialize(src.getTitulo()));
            nodoBody.add("COORDX", context.serialize(src.getCoordX()));
            nodoBody.add("COORDY", context.serialize(src.getCoordY()));
            nodoBody.add("TIENETITULO", context.serialize(src.getTieneTitulo()));
            nodoBody.add("TIENETEXTO", context.serialize(src.getTieneTexto()));
            nodoBody.add("TIENETEXTO2", context.serialize(src.getTieneTexto2()));
            nodoBody.add("TIENEDIVISIONCAMPOS", context.serialize(src.getTieneDivisionCampos()));
            nodoBody.add("CAMPOSHABILITADOS", context.serialize(src.getCamposHabilitados()));
            nodoBody.add("CAMPOSOCUPADOS", context.serialize(src.getCamposOcupados()));
            //Enlaces. Solo ponemos los enlaces de los cual este nodo es origen
            Map<String, EnlaceAbs> mapaEnlaces = src.getEnlacesMap();
            JsonArray jsonarray = new JsonArray();
            for (Map.Entry<String, EnlaceAbs> entry : mapaEnlaces.entrySet()) {
                if(src.getId().equals(entry.getValue().getFuente().getId()) ){
                    JsonObject enlaceBody = new JsonObject();
                    enlaceBody.add("ID", context.serialize(entry.getValue().getId()));
                    enlaceBody.add("TIPO", context.serialize(entry.getValue().getTipo()));
                    enlaceBody.add("FUENTE", context.serialize(entry.getValue().getFuente().getId()));
                    enlaceBody.add("DESTINO", context.serialize(entry.getValue().getDestino().getId()));
                    enlaceBody.add("PUERTOFUENTE", context.serialize(entry.getValue().getPuertoFuente()));
                    enlaceBody.add("PUERTODESTINO", context.serialize(entry.getValue().getPuertoDestino()));
                    //Añadimos este objeto al array de objetos
                    jsonarray.add(enlaceBody);
                }
            }
            nodoBody.add("ENLACES", new Gson().toJsonTree(jsonarray));
            nodoBody.add("CONTENIDO", context.serialize(src.getContenido()));
            nodoBody.add("CONTENIDO2", context.serialize(src.getContenido2()));
            return nodoBody;
        }
    }

}

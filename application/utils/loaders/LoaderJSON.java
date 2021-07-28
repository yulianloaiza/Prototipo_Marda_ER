package application.utils.loaders;

import application.models.Nodo.NodoAbs;
import application.utils.factories.FactoryAbs;
import application.utils.factories.FactoryER;
import application.utils.factories.FactoryFactory;
import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * Instancia de loaders concreta que crea objetos a partir de archivos .json
 */
public class LoaderJSON extends LoaderAbs{

    public LoaderJSON(){}

    @Override
    public FactoryAbs cargar(String path) {
        nodosAbs = new Vector<>();
        enlacesNuevos = new Vector<>();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(NodoAbs.class, new NodoAbsDeSerializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(path))); //Leemos el archivo del path
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Agarramos la string que indica el tipo de diagrama a deserializar
        JsonArray jsonA = new Gson().fromJson(inFile, JsonArray.class);
        JsonElement jsonE = jsonA.get(0);
        jsonE = ((JsonObject) jsonE).get("TIPODIAGRAMA");
        String tipoD = jsonE.getAsString();
        //Creamos fabrica correspondiente. Debemos ver si la fabrica que se nos ha pasado por parametro debe cambiar de tipo
        //Por ejemplo, en vez de ser FactoryER, sera FactoryBPNM por ejemplo
        FactoryFactory tempFac = new FactoryFactory();
        this.factory = tempFac.buildFactory(tipoD); //Actualizamos la factory
        //Ahora deserializamos los nodos en si
        NodoAbs[] objs = gson.fromJson(inFile, NodoAbs[].class); //Deserializes the specified Json into an object of the specified class.
        //Ahora los objetos estan creados, ya han sido deserializados.
        for (int i=0; i < objs.length; i++) {
            nodosAbs.add(objs[i] );
        }

        return this.factory;
    }

    /**
     * Permite extraer del archivo los vectores que representan divisiones del nodo en casos como diagramas
     * flow o compuertas logicas donde es necesario un manejo peculiar de donde se pueden conectar enlaces
     * @param nodoJSON El array de JSON que contiene todos los indices  guardados
     * @return Un vector de enteros que representa camposHabilitados o bien, cambosOcupados
     */
    public Vector<Integer> insertarCampos(JsonArray nodoJSON){
        Vector<Integer> contenidos = new Vector<>();
        for (JsonElement e : nodoJSON) { //Por cada uno de mis contenidos
            contenidos.add(e.getAsInt());
        }
        return contenidos;
    }

    /**
     * Dado un JSON Array, nos permite extraer el contenido que tenia guardado un nodo
     * @param nodoJSON Array de JSON que contiene todos los contenidos guardados
     * @return EL vector de strings que representa el contenido que tenia dado nodo
     */
    public Vector<String> insertarContenido(JsonArray nodoJSON){
        Vector<String> contenidos = new Vector<>();
        for (JsonElement e : nodoJSON) { //Por cada uno de mis contenidos
            contenidos.add(e.getAsString());
        }
        return contenidos;
    }

    /**
     * Dado un JsonObject, construimos el enlace con todos los atributos necesarios para el mismo
     * @param nodoJSON Objeto Json que adentro contiene todos los atributos de enlaces
     * @return el vector de String que representa un enlace, que todavia ha de implementarse en el canvas
     */
    public Vector<String> construirEnlace(JsonObject nodoJSON){
        Vector<String> enlaceNuevo = new Vector<>();
        enlaceNuevo.add(nodoJSON.get("ID").getAsString());
        enlaceNuevo.add(nodoJSON.get("TIPO").getAsString());
        enlaceNuevo.add(nodoJSON.get("FUENTE").getAsString());
        enlaceNuevo.add(nodoJSON.get("DESTINO").getAsString());
        //Estos de abajo deberan convertirse a ints.
        enlaceNuevo.add(nodoJSON.get("PUERTOFUENTE").getAsString());
        enlaceNuevo.add(nodoJSON.get("PUERTODESTINO").getAsString());
        return enlaceNuevo;
    }

    /**
     * Override del metodo para deserializar 'NodoAbs' que utiliza Gson. En vez de hacerlo automaticamente,
     * Gson sigue las intrucciones dadas en este metodo para saber como deserializar Nodos.
     * Dado un nodo, podemos obtener tanto sus atributos, como los enlaces que el origina, si es que tiene.
     * Devuelve el nodo 'creado', que posteriormente se agregara al vector de nodos.
     */
    public class NodoAbsDeSerializer implements JsonDeserializer<NodoAbs> {
        @Override
        public NodoAbs deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject();
            String tipo = jsonObject.get("TIPO").getAsString(); //Para saber cual instancia de clase crear
            NodoAbs mkNode = factory.buildNodo(tipo); //Nos devuelve el tipo apropiado usando factory
            //No hace falta ponerle 'tipo' ya que se pone implicitamente en el constructor.
            mkNode.setId(jsonObject.get("ID").getAsString());
            mkNode.setTitulo(jsonObject.get("TITULO").getAsString());
            mkNode.setCoordX(jsonObject.get("COORDX").getAsDouble());
            mkNode.setCoordY(jsonObject.get("COORDY").getAsDouble());
            mkNode.setTieneTitulo(jsonObject.get("TIENETITULO").getAsBoolean());
            mkNode.setTieneTexto(jsonObject.get("TIENETEXTO").getAsBoolean());
            mkNode.setTieneTexto2(jsonObject.get("TIENETEXTO2").getAsBoolean());
            mkNode.setTieneDivisionCampos(jsonObject.get("TIENEDIVISIONCAMPOS").getAsBoolean());
            mkNode.setCamposHabilitados(insertarCampos( jsonObject.getAsJsonArray("CAMPOSHABILITADOS") ));
            mkNode.setCamposOcupados(insertarCampos( jsonObject.getAsJsonArray("CAMPOSOCUPADOS") ));

            JsonArray enlacesJSON = jsonObject.getAsJsonArray("ENLACES");
            for (JsonElement e : enlacesJSON) { //Por cada uno de mis enlaces
                enlacesNuevos.add(construirEnlace(e.getAsJsonObject()));
            }
            mkNode.setContenido( insertarContenido( jsonObject.getAsJsonArray("CONTENIDO") ));
            mkNode.setContenido2( insertarContenido( jsonObject.getAsJsonArray("CONTENIDO2") ));
            return mkNode;

        }
    }
}

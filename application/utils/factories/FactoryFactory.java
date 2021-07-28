package application.utils.factories;

import application.Constants;

/**
 * Clase FactoryFactory, encargada de construir las fábricas correspondientes
 * según el tipo de diagrama con el que se vaya a trabajar.
 */
public class FactoryFactory {
    public FactoryAbs buildFactory(String type){
        FactoryAbs factoryDiagram = null;
        switch (type){
            case Constants.DIAGRAM_ER:
                factoryDiagram = new FactoryER();
                break;
            case Constants.DIAGRAM_REDES:
                factoryDiagram = new FactoryRedes();
                break;
            default:
                factoryDiagram = new FactoryER();
                break;
        }
        return factoryDiagram;
    }
}

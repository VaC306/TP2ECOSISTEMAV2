package simulator.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {
	// TODO definir atributos necesarios
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] _header;
	
	private RegionData[][] _regions;
	private int _colMax;
	private int _rowMax;
	private int _length;
	
	// Estructura de datos para almacenar el recuento de animales por dieta para cada región
	private Map<RegionData, Map<Diet, Integer>> _animalCountByRegion = new HashMap<>();
    
	
	
	RegionsTableModel(Controller ctrl) {
		// TODO inicializar estructuras de datos correspondientes
		_regions = new RegionData[_rowMax][_colMax];
		
		_header = new String[Diet.values().length + 3];
		for(int i = 0 ;i <= _header.length - 1; i++)
		{
			if(i == 0)
				_header[i] = "Row";
			else if(i == 1)
				_header[i] = "Col";
			else if(i == 2)
				_header[i] = "Desc.";
			else
				_header[i] = Diet.values()[i-3].toString();
		}
			
		// TODO registrar this como observador
		ctrl.addObserver(this);
	}
	
	// TODO el resto de métodos van aquí…
	
	@Override
	public int getRowCount() {
		return _regions == null ? 0 : _length;
	}

	@Override
	public int getColumnCount() {
		return _header.length;
	}
	
	@Override
    public String getColumnName(int columnIndex) {
		return _header[columnIndex];
    }
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Verificar si la matriz de regiones no está inicializada o si el índice de columna está fuera de rango
	    if (_regions == null || columnIndex < 0 || columnIndex >= _header.length) {
	        return null;
	    }
	    
	 // Iterar sobre todas las filas y columnas de la matriz de regiones
	    int _row = rowIndex / _colMax;
        int _col = rowIndex % _colMax;
        if (_row >= _rowMax || _col >= _colMax) {
            return null;
        }
	    
	    switch(columnIndex)
	    {
	    	// Si columnIndex es 0, 1 o 2, devolver el número de fila, número de columna o descripción de la región respectivamente
	    	case 0:
	    		return _row;
	    	case 1:
	    		
	    		return _col;
	    	case 2:
	    		
	    		return _regions[_row][_col].r().toString();
	    	default:
	    		 // Obtener la región correspondiente a la fila y columna especificadas
	            RegionData region = _regions[rowIndex / _colMax][rowIndex % _colMax];
	            // Obtener el recuento de animales de la dieta correspondiente en la región
	            return _animalCountByRegion.get(region).get(Diet.values()[columnIndex - 3]);
	    }
	}
	
	// Método para inicializar la estructura de datos
    private void initializeAnimalCountByRegion(MapInfo map) {
        _animalCountByRegion.clear();
        for (RegionData[] row : _regions) {
            for (RegionData region : row) {
                Map<Diet, Integer> animalCount = new HashMap<>();
                for (Diet diet : Diet.values()) {
                    animalCount.put(diet, 0);
                }
                _animalCountByRegion.put(region, animalCount);
            }
        }
    }
	
    // Método para actualizar la estructura de datos cuando cambie la información de las regiones o de los animales
    private void updateAnimalCountByRegion(MapInfo map) {
        for (RegionData[] row : _regions) {
            for (RegionData region : row) {
                Map<Diet, Integer> animalCount = _animalCountByRegion.get(region);
                // Reiniciar el recuento de animales en la región
                for (Diet diet : Diet.values()) {
                    animalCount.put(diet, 0);
                }
                // Actualizar el recuento de animales en la región según la información actual de los animales
                for (AnimalInfo animal : region.r().getAnimalsInfo()) {
                    Diet diet = animal.get_diet();
                    animalCount.put(diet, animalCount.get(diet) + 1);
                }
            }
        }
    }
    
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		_colMax = map.get_cols();
		_rowMax = map.get_rows();
		_length = _rowMax * _colMax;
		 _regions = new RegionData[_rowMax][_colMax];
	        Iterator<RegionData> it = map.iterator();
	        for (int i = 0; i < _rowMax; i++) {
	            for (int j = 0; j < _colMax && it.hasNext(); j++) {
	                _regions[i][j] = it.next();
	            }
	        }
	    initializeAnimalCountByRegion(map);
	    updateAnimalCountByRegion(map);
		fireTableStructureChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		_colMax = map.get_cols();
		_rowMax = map.get_rows();
		_length = _rowMax * _colMax;
		 _regions = new RegionData[_rowMax][_colMax];
	        Iterator<RegionData> it = map.iterator();
	        for (int i = 0; i < _rowMax; i++) {
	            for (int j = 0; j < _colMax && it.hasNext(); j++) {
	                _regions[i][j] = it.next();
	            }
	        }
	    initializeAnimalCountByRegion(map);
		updateAnimalCountByRegion(map);
		fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		updateAnimalCountByRegion(map);
		fireTableStructureChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		_regions[row][col] = new RegionData(row, col, r);
		initializeAnimalCountByRegion(map);
		updateAnimalCountByRegion(map);
		fireTableStructureChanged();
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		updateAnimalCountByRegion(map);
		fireTableStructureChanged();
	}
}

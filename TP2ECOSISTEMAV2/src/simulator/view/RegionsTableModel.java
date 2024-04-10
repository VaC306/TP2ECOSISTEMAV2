package simulator.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.DefaultRegion;
import simulator.model.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.Region;
import simulator.model.RegionInfo;
import simulator.model.State;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {
	// TODO definir atributos necesarios
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] _header;
	List<AnimalInfo> _animals;
	
	private RegionData[][] _regions;
	private int row;
	private int col;
	private int colMax;
	private int rowMax;
	private int _region_width;
	private int _region_height;
	
	
	RegionsTableModel(Controller ctrl) {
	// TODO inicializar estructuras de datos correspondientes
	row = 0;
	col = 0;
	_animals = new ArrayList<>();
		
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
		return _regions == null ? 0 : _regions.length;
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
		/*switch(columnIndex)
		{	
			case 0:
				return row;
			case 1:
				return col;
			case 2:
				return _regions[row][col].r().toString();
			default:
				//return the animals in the n states
				if (_animals == null) {
	                return null;
	            }
				// Obtener el estado correspondiente al índice de la columna
		        Diet diet = Diet.values()[columnIndex - 3];
		        int count = 0;
		        
		        
		        // Contar animales con el código genético y estado específicos
			    for (AnimalInfo animal : _animals) {
			    	if ((row == (animal.get_position().getY() / _region_height) && col == (animal.get_position().getX() / _region_width)) && (animal.get_diet() == diet)) {
			    		count++;
			        }
			    }
			    return count;
		        
		}*/
		// Verificar si la matriz de regiones no está inicializada o si el índice de columna está fuera de rango
	    if (_regions == null || columnIndex < 0 || columnIndex >= _header.length) {
	        return null;
	    }

	    // Si columnIndex es 0, 1 o 2, devolver el número de fila, número de columna o descripción de la región respectivamente
	    if (columnIndex == 0) {
	        return rowIndex; // Número de fila
	    } else if (columnIndex == 1) {
	        return columnIndex; // Número de columna
	    } else if (columnIndex == 2) {
	        return _regions[rowIndex][columnIndex].r().toString(); // Descripción de la región
	    } else {
	        // Índice de columna correspondiente a una dieta
	        // Calcular el índice de la dieta en base al índice de columna
	        int dietIndex = columnIndex - 3;
	        Diet diet = Diet.values()[dietIndex];

	        int count = 0;
	        // Iterar sobre todas las filas y columnas de la matriz de regiones
	        for (int i = 0; i < _regions.length; i++) {
	            for (int j = 0; j < _regions[i].length; j++) {
	                // Contar animales con la dieta específica en la región actual
	                RegionData regionData = _regions[i][j];
	                for (AnimalInfo animal : regionData.r().getAnimalsInfo()) {
	                    if (animal.get_diet() == diet) {
	                        count++;
	                    }
	                }
	            }
	        }
	        return count;
	    }
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		_regions = new RegionData[map.get_rows()][map.get_cols()];
		Iterator<RegionData> _it = map.iterator();
		int i = 0;
		int j = 0;
		
		while(_it.hasNext())
		{
			_regions[i][j] = _it.next();
			j++;
			if(j == map.get_cols())
			{
				j = 0;
				i++;
			}
		}
		colMax = map.get_cols();
		rowMax = map.get_rows();
		_region_height = map.get_region_height();
		_region_width = map.get_region_width();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		colMax = map.get_cols();
		rowMax = map.get_rows();
		_region_height = map.get_region_height();
		_region_width = map.get_region_width();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		colMax = map.get_cols();
		rowMax = map.get_rows();
		_region_height = map.get_region_height();
		_region_width = map.get_region_width();
	}
}

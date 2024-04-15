package simulator.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.TableModelListener;
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
	private int _colMax;
	private int _rowMax;
	private int _row;
	private int _col;
	private int _length;
	
	
	RegionsTableModel(Controller ctrl) {
		// TODO inicializar estructuras de datos correspondientes
		_animals = new ArrayList<>();
		_regions = new RegionData[_rowMax][_colMax];
		
		_row = 0;
		_col = 0;
		
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
	    		if(rowIndex < _length)
	    		{
	    			// Índice de columna correspondiente a una dieta
			        // Calcular el índice de la dieta en base al índice de columna
			        int dietIndex = columnIndex - 3;
			        Diet diet = Diet.values()[dietIndex];
			      
			        int _count = 0;
			        
			        _count = 0;
			        // Contar animales con la dieta específica en la región actual
			        RegionData regionData = _regions[_row][_col];
			        for (AnimalInfo animal : regionData.r().getAnimalsInfo()) {
			        	if (animal.get_diet() == diet) {
			        		_count++;
			        	}
			        } 
			        return _count;
	    		}
	    		else
	    			return null;
	    }
	}
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		_rowMax = map.get_rows();
        _colMax = map.get_cols();
        _length = _rowMax * _colMax;
        _regions = new RegionData[_rowMax][_colMax];
        Iterator<RegionData> it = map.iterator();
        for (int i = 0; i < _rowMax; i++) {
            for (int j = 0; j < _colMax && it.hasNext(); j++) {
                _regions[i][j] = it.next();
            }
        }
		
		fireTableStructureChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		_colMax = map.get_cols();
		_rowMax = map.get_rows();
		_length = _rowMax * _colMax;
		fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		_animals.add(a);
		fireTableStructureChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		fireTableStructureChanged();
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		fireTableStructureChanged();
	}
}

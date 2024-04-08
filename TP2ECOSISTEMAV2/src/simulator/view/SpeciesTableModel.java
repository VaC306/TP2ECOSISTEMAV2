package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;
import simulator.model.State;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// TODO definir atributos necesarios
	Controller _ctrl;
	String[] _header;
	
	List<String> _genetic;
	List<String> _states;
	List<AnimalInfo> _animals;
	
	SpeciesTableModel(Controller ctrl) {
		// TODO inicializar estructuras de datos correspondientes
		_ctrl = ctrl;
		_animals = new ArrayList<>();
		_genetic = new ArrayList<>();
		_states = new ArrayList<>();
		
		//adding to the header the column Species and the different States
		_header = new String[State.values().length + 1];
		for(int i = 0 ;i <= State.values().length; i++)
		{
			if(i == 0)
				_header[i] = "Species";
			else
				_header[i] = State.values()[i-1].toString();
		}		
		
		// TODO registrar this como observador
		_ctrl.addObserver(this);
	}

	
	@Override
	public int getRowCount() {
		return _animals == null ? 0 : _animals.size();
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
		
		switch (columnIndex) {
		
			//return the different type of species
			case 0:
				if (_animals == null) {
	                return null;
	            }
				
				for(AnimalInfo a : _animals)
				{
					if(!_genetic.contains(a.get_genetic_code()))
						_genetic.add(a.get_genetic_code());
				}
				if(rowIndex < _genetic.size())
					return _genetic.get(rowIndex);
				else
					return null;
			
			default:
				//return the animals in the n states
				if (_animals == null) {
	                return null;
	            }
				// Obtener el estado correspondiente al índice de la columna
		        State state = State.values()[columnIndex - 1];
		        int count = 0;
		        if(rowIndex < _genetic.size())
		        {
		        	// Contar animales con el código genético y estado específicos
			        for (AnimalInfo animal : _animals) {
			            if (animal.get_genetic_code().equals(_genetic.get(rowIndex)) && animal.get_state() == state) {
			                count++;
			            }
			        }

			        return count;
		        }
		        else
		        	return null;
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		for(AnimalInfo a : animals)
		{
			_animals.add(a);
		}
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		_animals.clear();
		fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		_animals.add(a);
		fireTableStructureChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		fireTableStructureChanged();
		
	}

}

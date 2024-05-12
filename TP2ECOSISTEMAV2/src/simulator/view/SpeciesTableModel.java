package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
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
	
	private Map<String, Map<State, Integer>> _animalCountByGeneticAndState;
	
	SpeciesTableModel(Controller ctrl) {
		// TODO inicializar estructuras de datos correspondientes
		_ctrl = ctrl;
		_animals = new ArrayList<>();
		_genetic = new ArrayList<>();
		_states = new ArrayList<>();
		_animalCountByGeneticAndState = new HashMap<>();
		
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
		return _genetic == null ? 0 : _genetic.size();
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
				if(rowIndex < _genetic.size())
					return _genetic.get(rowIndex);
				else
					return null;
			
			default:
				//return the animals in the n states
				if (_animals == null) {
	                return null;
	            }
				else
				{
					String geneticCode = _genetic.get(rowIndex);
		            State state = State.values()[columnIndex - 1];
		            Map<State, Integer> animalCountByState = _animalCountByGeneticAndState.getOrDefault(geneticCode, new HashMap<>());
		            return animalCountByState.getOrDefault(state, 0);
				}
		}
	}
	
	 private void updateData(List<AnimalInfo> animals) {
	        _genetic.clear();
	        _animalCountByGeneticAndState.clear();

	        for (AnimalInfo animal : animals) {
	            String geneticCode = animal.get_genetic_code();
	            State state = animal.get_state();

	            if(!_genetic.contains(animal.get_genetic_code()))
					_genetic.add(animal.get_genetic_code());

	            Map<State, Integer> animalCountByState = _animalCountByGeneticAndState.computeIfAbsent(geneticCode, k -> new HashMap<>());
	            animalCountByState.put(state, animalCountByState.getOrDefault(state, 0) + 1);
	        }
	    }
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		for(AnimalInfo a : animals)
		{
			_animals.add(a);
		}
		updateData(animals);
		fireTableStructureChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		_animals.clear();
		for(AnimalInfo a : animals)
		{
			_animals.add(a);
		}
		updateData(animals);
		fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		if(!_animals.contains(a))
			_animals.add(a);
		updateData(animals);
		fireTableStructureChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		_animals.clear();
		for(AnimalInfo a : animals)
		{
			_animals.add(a);
		}
		updateData(animals);
		fireTableStructureChanged();
		
	}

}

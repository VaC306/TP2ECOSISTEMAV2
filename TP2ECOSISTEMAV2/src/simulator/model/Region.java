package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo{
	
	protected List<Animal> _animals;
	
	public Region()
	{
		this._animals = new LinkedList<>();
	}
	
	final void add_animal(Animal a)
	{
		_animals.add(a);
	}
	
	final void remove_animal(Animal a)
	{
		_animals.remove(a);
	}
	
	final List<Animal> getAnimals()
	{
		return Collections.unmodifiableList(_animals);
	}
	
	public List<AnimalInfo> getAnimalsInfo() {
		return new ArrayList<>(_animals); // se puede usar Collections.unmodifiableList(_animals);
	}
	
	public JSONObject as_JSON()
	{
		JSONObject animals = new JSONObject();
		JSONArray list = new JSONArray();
		
		for(Animal a : getAnimals())
		{
			list.put(a);
		}
		
		animals.put("animals", list);
		return animals;
	}
}

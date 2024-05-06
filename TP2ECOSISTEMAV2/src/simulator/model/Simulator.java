package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable, Observable<EcoSysObserver> {
	
	protected int _cols;
	protected int _rows;
	protected int _width;
	protected int _height;
	protected double _time;
	protected RegionManager _regmanager;
	protected List<Animal> l;
	ListIterator<Animal> it;
	protected Factory<Animal> _animals_factory;
	protected Factory<Region> _regions_factory;
	private List <EcoSysObserver> _observers;
	
	
	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory, Factory<Region> regions_factory)
	{
		
		_regmanager = new RegionManager(cols, rows, width, height);
		l = new LinkedList<>();
		it = l.listIterator();
		this._cols = cols;
		this._rows = rows;
		this._width = width;
		this._height = height;
		this._time = 0.0;
		this._animals_factory = animals_factory;
		this._regions_factory = regions_factory;
		this._observers = new ArrayList<EcoSysObserver>();
	}
	
	public double get_time()
	{
		return this._time;
	}
	
	private void add_animal(Animal a)
	{
		_regmanager.register_animal(a);
		l.add(a);
		
		List<AnimalInfo> animals = new ArrayList<>(l);
		for(EcoSysObserver o: _observers)
		{
			o.onAnimalAdded(_time, _regmanager, animals, a);
		}
	}
	
	public void add_animal(JSONObject a_json)
	{
		Animal nuevo_animal = _animals_factory.create_instance(a_json);
		add_animal(nuevo_animal);
		
	}
	
	private void set_region(int row, int col, Region r)
	{
		_regmanager.set_region(row, col, r);
		
		for(EcoSysObserver o: _observers)
		{
			o.onRegionSet(row, col, _regmanager, r);
		}
	}
	
	public void set_region(int row, int col, JSONObject r_json)
	{
		Region nueva_region = _regions_factory.create_instance(r_json);
		set_region(row, col, nueva_region);
	}
	
	public MapInfo get_map_info()
	{
		return _regmanager;
	}
	
	public List<? extends AnimalInfo> get_animals()
	{
		return Collections.unmodifiableList(l);
	}
	
	public void advance(double dt)
	{
		this._time += dt;
		
		
		it = l.listIterator();
		
		while(it.hasNext())
		{
			Animal a = it.next();
			
			//quitar los animales muertos
			if(a.get_state() == State.DEAD)
			{
				it.remove();
				_regmanager.unregister_animal(a);
			}
		}
		
		//siempre que haya un siguiente animal
		for(Animal a : l)
		{	
			a.update(dt); //actualizar cada animal
			_regmanager.update_animal_region(a); //actualizar la region del animal
			_regmanager.update_all_regions(dt);
		}
		
		//crear los nuevos animales
		it = l.listIterator();
		
		while(it.hasNext())
		{
			Animal a = it.next();
			
			if(a.is_pregnent())
			{
				Animal _baby = a.deliver_baby();
				//add_animal(_baby);
				it.add(_baby);
				_regmanager.register_animal(_baby);
			}
		}
		
		List<AnimalInfo> animals = new ArrayList<>(l);
		for(EcoSysObserver o: _observers)
		{
			o.onAvanced(_time, _regmanager, animals, dt);
		}
		
	}
	
	public void reset(int cols, int rows, int width, int height)
	{
		l.clear(); //vaciar lista de animales
		_regmanager = new RegionManager(cols, rows, width, height); //nuevo reg manager con este tamaño
		_time = 0.0; //time a 0.0
		
		List<AnimalInfo> animals = new ArrayList<>(l);
		for(EcoSysObserver o: _observers)
		{
			o.onReset(_time, _regmanager, animals);
		}
		
	}
	
	public JSONObject as_JSON()
	{
		JSONObject _obj = new JSONObject();
		
		_obj.put("time", _time);
		_obj.put("state", _regmanager.as_JSON());
		
		return _obj;
	}

	@Override
	public void addObserver(EcoSysObserver o) {
		if(!_observers.contains(o))
		{
			_observers.add(o);
			o.onRegister(_time, _regmanager, Collections.unmodifiableList(l));
		}
		
	}

	@Override
	public void removeObserver(EcoSysObserver o) {
		_observers.remove(o);
		
	}
	
}

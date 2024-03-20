package simulator.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegionManager implements AnimalMapView, Iterable<MapInfo.RegionData>{
	
	protected int _cols;
	protected int _rows;
	protected int _width;
	protected int _height;
	protected int _region_height;
	protected int _region_width;
	protected Region[][] _regions;
	protected Map<Animal, Region> _animal_region;
	
	public RegionManager(int cols, int rows, int width, int height)
	{
		this._cols = cols;
		this._rows = rows;
		this._width = width;
		this._height = height;
		
		if ( _width % _cols != 0 || _height % _rows != 0) 
			throw new IllegalArgumentException("la anchura y el grosor no son divisibles por las columnas y filas");
		
		_region_width = _width / _cols;
		_region_height = _height / _rows;
		
		this._regions = new Region[rows][cols];
		
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				this._regions[i][j] = new DefaultRegion();
		
		this._animal_region = new HashMap <Animal, Region>();
		
	}
	
	void set_region(int row, int col, Region r)
	{
		//actualizar lista de animales de la region
		r._animals = this._regions[row][col].getAnimals();
		this._regions[row][col] = r;
		for(Animal a : r._animals)
		{
			_animal_region.replace(a, r);
		}
	}
	
	void update_animal_region(Animal a)
	{
		Region _region = region_animal(a);
		Region _old_region = _animal_region.get(a);
		
		if((_old_region != null) && !_old_region.equals(_region))
		{
			_region.add_animal(a);
			_old_region.remove_animal(a);
			_animal_region.put(a, _region);
			_animal_region.remove(a, _old_region);
		}
	}
	
	void register_animal(Animal a)
	{
		a.init(this);
		
		Region _region = region_animal(a);
		
		_region.add_animal(a);
		_animal_region.put(a, _region);
	}
	
	void unregister_animal(Animal a)
	{
		Region _region = region_animal(a);
		_region.remove_animal(a);
		_animal_region.remove(a, _region);
		update_animal_region(a);
	}
	
	void update_all_regions(double dt)
	{
		for(int i = 0; i < _rows; ++i)
		{
			for(int j = 0; j < _cols; ++j)
			{
				_regions[i][j].update(dt);
			}
		}
	}
	
	private Region region_animal(Animal a)
	{
		return _regions[(int) (a.get_position().getY() / _region_height)][(int)(a.get_position().getX() / _region_width)];
	}
	
	public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
	    List<Animal> animalsInRange = new LinkedList<>();

	    // Obtener las coordenadas del animal a
	    double animalX = a.get_position().getX();
	    double animalY = a.get_position().getY();
	    double sightRange = a.get_sight_range();

	    // Calcular las coordenadas del campo visual
	    double minX = Math.max(0, animalX - sightRange);
	    double maxX = Math.min(_width - 1, animalX + sightRange);
	    double minY = Math.max(0, animalY - sightRange);
	    double maxY = Math.min(_height - 1, animalY + sightRange);

	    // Iterar sobre las regiones en el campo visual
	    int minRow = (int) (minY / _region_height);
	    int maxRow = (int) (maxY / _region_height);
	    int minCol = (int) (minX / _region_width);
	    int maxCol = (int) (maxX / _region_width);

	    for (int i = minRow; i <= maxRow; i++) {
	        for (int j = minCol; j <= maxCol; j++) {
	            Region region = _regions[i][j];
	            List<Animal> animalsInRegion = region.getAnimals();

	            // Filtrar animales dentro del campo visual y que cumplan con el filtro
	            for (Animal animal : animalsInRegion) {
	                double distance = animal.get_position().distanceTo(a.get_position());
	                if (distance <= sightRange && filter.test(animal)) {
	                    animalsInRange.add(animal);
	                }
	            }
	        }
	    }

	    return animalsInRange;
	}


	@Override
	public int get_cols() {
		return _cols;
	}

	@Override
	public int get_rows() {
		return _rows;
	}

	@Override
	public int get_width() {
		return _width;
	}

	@Override
	public int get_height() {
		return _height;
	}

	@Override
	public int get_region_width() {
		return _region_width;
	}

	@Override
	public int get_region_height() {
		return _region_height;
	}

	@Override
	public double get_food(Animal a, double dt) {
		Region _region = region_animal(a);
		return _region.get_food(a, dt);
	}
	
	public JSONObject as_JSON()
	{
		JSONObject ret = new JSONObject();
		JSONArray _regiones = new JSONArray();
		
		
		for(int i = 0; i < _rows; ++i)
		{
			for(int j = 0; j < _cols; ++j)
			{
				JSONObject _region = new JSONObject();
				
				_region.put("row", i);
				_region.put("col", j);
				_region.put("data", _regions[i][j].as_JSON()); //ver como hacerlo
				
				_regiones.put(_region);
			}
		}
		ret.put("regiones", _regiones);
		return ret;
	}
	
	@Override
	public Iterator<RegionData> iterator() {
		 return new Iterator<MapInfo.RegionData>() {
			 private int currentRow = 0;
	         private int currentCol = 0;

	         	@Override
	         	public boolean hasNext() {
	         		return currentRow < _rows && currentCol < _cols;
	         	}

	         	@Override
	         	public MapInfo.RegionData next() {
	         		MapInfo.RegionData regionData = MapInfo.RegionData.createRegionData(currentRow, currentCol, _regions[currentRow][currentCol]);
	         		currentCol++;
	         	if (currentCol >= _cols) {
	         		currentCol = 0;
	         		currentRow++;
	         	}
	         	return regionData;
	         	}
		 };
	}
}

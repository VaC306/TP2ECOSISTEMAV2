package simulator.model;

import java.util.function.Predicate;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Sheep extends Animal{
	
	protected double width = 800.0;
	protected double height = 600.0;
	protected Animal _danger_source;
	protected SelectionStrategy _danger_strategy_;
	
	
	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos)
	{
		super("SHEEP", Diet.HERBIVORE, 40.0, 35.0, mate_strategy, pos);
		this._pos = pos;
		this._danger_strategy_= danger_strategy;
		this._mate_strategy = mate_strategy;
		
	}
	
	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		this._danger_strategy_ = p1._danger_strategy_;
		p1._danger_source= null;
	}
	
	@Override
	public void update(double dt) {
		
		//si le estado es DEAD no hace nada
		if(this.get_state().equals(State.DEAD)) {
			
		}
		else
		{
			//actualiza el objeto segun el estado del animal
			actualizar(dt);
			
			//mantener al SHEEP en la zona deseada
			if(this.get_position().getX() > width || this.get_position().getY() > height || this.get_position().getX() < 0 || this.get_position().getY() < 0)
			{
				ajustar_pos(width, height);
				
				this._state = State.NORMAL;
			}
			//si su energia es 0 o age >8 cambia a DEAD
			if(this.get_energy() == 0.0 ||this.get_age() > 8.0)
				this._state = State.DEAD;
			
			//pide comida y la añade a su energy(entre 0 y 100)
			if(!this.get_state().equals(State.DEAD))
			{
				_energy = Math.max(0.0, Math.min(_energy + _region_mngr.get_food(this, dt), 100.0));//energy + get_food(this, dt) (0-100)
			}
		}
	}
	
	
	
	private void actualizar(double dt)
	{
		switch(this.get_state())
		{
			case NORMAL:
				avanzapaso1(dt);
				//buscar un nuevo animal si danger es null
				if(this._danger_source == null) 
				{
					//buscar animal que se considere peligroso
					Predicate<Animal> DangersAnimals = (a)-> a.get_diet()== Diet.CARNIVORE;
					
					_region_mngr.get_animals_in_range(this, DangersAnimals);
					
					//mantenemos una referencia del animal peligroso
					_danger_source =_danger_strategy_.select(this, _region_mngr.get_animals_in_range(this, DangersAnimals));
					
					
					//si tras buscar sigue siendo null
					if(_danger_source != null){
						this._state = State.DANGER;
						_mate_target = null;
					}	
				}
				if(this._desire > 65.0 && _danger_source == null) 
				{
					this._state = State.MATE;
					_danger_source = null;
				}
				break;
				
			case DANGER:
				//si el animal peligroso ya ha muerto por alguna otra razon
				if(this._danger_source != null && this.get_state() ==State.DEAD) {
					this._danger_source =null;
				}
				if(this._danger_source.equals(null)) {
					avanzapaso1(dt);
				}
				
				if(this._danger_source != null) {
					this._dest =_pos.plus(_pos.minus(_danger_source.get_position()).direction());
					
					avanzapaso2(dt);
				}
				
				//cambio de estado
				if(this._danger_source == null || _danger_source.get_position().distanceTo(this.get_position()) > this.get_sight_range()) {
					//buscar animal que se considere peligroso
					Predicate<Animal> DangersAnimals = (a)-> a.get_diet()== Diet.CARNIVORE;
					
					_region_mngr.get_animals_in_range(this, DangersAnimals);
					
					//mantenemos una referencia del animal peligroso
					_danger_source =_danger_strategy_.select(this, _region_mngr.get_animals_in_range(this, DangersAnimals));
					
					if(this._danger_source==null)
					{
						if(this._desire < 65.0) 
						{
							this._state = State.NORMAL;
							_danger_source = null;
							_mate_target = null;
						}
						else
						{
							this._state = State.MATE;
							_danger_source = null;
						}
					}
					
				}
			
					break;
					
				case MATE:
					if(this._mate_target != null && this._state ==State.DEAD || (this._mate_target != null &&_mate_target.get_position().distanceTo(this.get_position())>this.get_sight_range())) {
						this._mate_target =null;
					}
					
					
					if(this._mate_target==null) {
						//buscar animal para emparejarse
						Predicate<Animal> MateAnimals = (a)-> a.get_genetic_code() == _genetic_code;
						
						_region_mngr.get_animals_in_range(this, MateAnimals);
						
						//mantenemos una referencia del animal mate
						_mate_target =_mate_strategy.select(this, _region_mngr.get_animals_in_range(this, MateAnimals));
						}
					
					//si sigue siendo null avanzo comopaso1
					if(_mate_target==null) {
					avanzapaso1(dt);
					}
					else
					{
						this._dest = _mate_target.get_position();
									
						avanzapaso2(dt);
						//si la distancia es menos q 8, se empareja y tienen un bebe
						if(this.get_position().distanceTo(_mate_target.get_position()) < 8.0) {
							this._desire=0.0;
							_mate_target._desire=0.0;
								
							if(!is_pregnent())
							{
								if(Utils._rand.nextDouble() < 0.9)
								{
									_baby = new Sheep(this, _mate_target);
								}
								_mate_target = null;
							}
						}
					
						
							
						//buscar nuevo animal como peligroso
						if(_danger_source == null) {
							//buscar animal que se considere peligroso
							Predicate<Animal> DangersAnimals = (a)-> a.get_diet()== Diet.CARNIVORE;
								
							_region_mngr.get_animals_in_range(this, DangersAnimals);
								
							//mantenemos una referencia del animal peligroso
							_danger_source =_danger_strategy_.select(this, _region_mngr.get_animals_in_range(this, DangersAnimals));
								
						}
							
						if(_danger_source != null) {
								this._state = State.DANGER;
								_mate_target = null;
						}
							
						if(_danger_source == null && this._desire < 65.0) {
								this._state = State.NORMAL;
								_danger_source = null;
								_mate_target = null;
						}
					}
					break;
					
					
				default:
					break;
			}
		
		
		
	}
	
	private void avanzapaso1(double dt) {
		//elije un nuevo destino
		if(this.get_position().distanceTo(this.get_destination()) < 8.0)
		{
			this._dest = elegir_pos_rand();
		}
		
		//avanza
		move(_speed*dt*Math.exp((_energy-100.0)*0.007)); 
		
		// a�ade dt a la edad
		this._age += dt; 
		
		//quita energia
		this._energy = Math.max(0.0, Math.min(100.0, this._energy - 20.0 * dt));

		
		//suma deseo
		this._desire = Math.max(0.0, Math.min(100.0, this._desire + 40.0 * dt));
		
	}
	
	private void avanzapaso2(double dt){
		//avanza
		move(2.0*_speed*dt*Math.exp((_energy-100.0)*0.007)); 
				
		// a�ade dt a la edad
		this._age = _age+dt; 
				
		//quita energia
		this._energy = Math.max(0.0, Math.min(100.0, this._energy - 20.0 * 1.2 * dt));
				
		//suma deseo
		this._desire = Math.max(0.0, Math.min(100.0, this._desire + 40.0 * dt));
				
	}
	
}

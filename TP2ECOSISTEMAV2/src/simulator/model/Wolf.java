package simulator.model;



import java.util.function.Predicate;

import simulator.misc.Utils;
import simulator.misc.Vector2D;



public class Wolf extends Animal{
	
	protected double width = 800.0;
	protected double height = 600.0;
	protected Animal _hunt_target;
	protected SelectionStrategy _hunting_strategy;
	
	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos)
	{
		super("WOLF", Diet.CARNIVORE, 50.0, 60.0, mate_strategy, pos);
		this._pos = pos;
		this._hunting_strategy = hunting_strategy;
		this._mate_strategy = mate_strategy;
	}
	
	protected Wolf(Wolf p1, Animal p2)
	{
		super(p1, p2);
		this._hunting_strategy = p1._hunting_strategy;
		p1._hunt_target = null; 
	}
	

	@Override

	public void update(double dt) {
		
		//si le estado es DEAD no hace nada
		if(this.get_state() == State.DEAD)
		{
			
		}
		else
		{
			//actualiza el objeto segun el estado del animal
			actualizar(dt);
			
			//mantener al WOLF en la zona deseada
			if(this.get_position().getX() > width || this.get_position().getY() > height || this.get_position().getX() < 0 || this.get_position().getY() < 0)
			{
				ajustar_pos(width, height);
			
				this._state = State.NORMAL;
			}
			
			//si su energia es 0 o age >14 cambia a DEAD
			if(this.get_energy() == 0.0 || this.get_age() > 14.0)
				this._state = State.DEAD;
			
			//pide comida y la a√±ade a su energy(entre 0 y 100)
			if(this.get_state() != State.DEAD)
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
				//avanzar
				avanzapaso1(dt);
				
				//cambio de estado
				if(this.get_energy() < 50.0)
				{
					this._state = State.HUNGER;
					this._mate_target = null;
				}
				else if(this.get_energy() >= 50.0 && this._desire > 65.0)
				{
					this._state = State.MATE;
					this._hunt_target = null;
				}
				break;
				
			case HUNGER:
				if(_hunt_target == null || (_hunt_target != null && _hunt_target.get_state() == State.DEAD) 
				|| (_hunt_target != null && this.get_position().distanceTo(_hunt_target.get_position()) > this.get_sight_range()))
				{
					//buscar animal que se considere peligroso
					Predicate<Animal> _cazar = (a)-> a.get_diet()== Diet.HERBIVORE;
			
					_region_mngr.get_animals_in_range(this, _cazar);
			
					//mantenemos una referencia del animal peligroso
					_hunt_target = _hunting_strategy.select(this, _region_mngr.get_animals_in_range(this, _cazar));
				}
				if(_hunt_target == null)
				{
					avanzapaso1(dt);
				}
				else
				{
					this._dest = _hunt_target.get_position();
			
					avanzapaso2(dt);
				
					if(_hunt_target.get_position().distanceTo(this.get_position()) < 8.0)
					{
						_hunt_target._state = State.DEAD;
						_hunt_target = null;
						_energy = Math.max(0.0, Math.min(_energy + 50.0, 100.0));//energy + 50.0 (0-100)
					}
				}
			//cambio de estado
			if(this.get_energy() > 50.0)
			{
				if(this._desire < 65.0)
				{
					this._state = State.NORMAL;
					_hunt_target = null;
					_mate_target = null;
				}
				else
				{
					this._state = State.MATE;
					_hunt_target = null;
				}		
			}
			
					break;
					
				case MATE:
					
					if(_mate_target != null && _mate_target._state == State.DEAD || _mate_target != null && _mate_target.get_position().minus(_pos).magnitude() > this.get_sight_range())
					{
						_mate_target = null;
					}
					if(_mate_target == null)
					{
						//buscar animal que se considere peligroso
						Predicate<Animal> MateAnimals = (a)-> a.get_genetic_code() == this._genetic_code;
						
						_region_mngr.get_animals_in_range(this, MateAnimals);
						
						//mantenemos una referencia del animal mate
						_mate_target =_mate_strategy.select(this, _region_mngr.get_animals_in_range(this, MateAnimals)); 
					}
					if(_mate_target == null)
					{
						avanzapaso1(dt);
					}
					else
					{
						this._dest = _mate_target.get_position();
						
						avanzapaso2(dt);
						
						if(_mate_target.get_position().distanceTo(this._pos) < 8.0)
						{
							this._desire = 0.0;
							_mate_target._desire = 0.0;
								
							if(!is_pregnent())
							{
								if(Utils._rand.nextDouble() < 0.9)
								{
									_baby = new Wolf(this, _mate_target);
								}
									
								_energy = Math.max(0.0, Math.min(_energy - 10.0, 100.0));//energy - 10 (0-100)
									
								_mate_target = null;
							}
						}
					}
					
					//cambio de estado
					if(this.get_energy() < 50.0)
					{
						this._state = State.HUNGER;
						this._mate_target = null;
					}
					else
					{
						if(this._desire < 65.0)
						{
							this._state = State.NORMAL;
							_hunt_target = null;
							_mate_target = null;
						}
					}
					break;
					
					
				default:
					break;
			}
		
		
		
	}
	
	private void avanzapaso1(double dt)
	{
		//avanzar
		if(_dest.distanceTo(_pos) < 8.0)
		{
			this._dest = elegir_pos_rand();
		}
				
		move(this._speed*dt*Math.exp((_energy-100.0)*0.007));//avanza
						
		this._age += dt; // aÒade dt a la edad
		
		_energy = Math.max(0.0, Math.min(_energy - 18.0*dt, 100.0));//energy -18.0*dt (0-100)
						
		_desire = Math.max(0.0, Math.min(_desire + 30.0*dt, 100.0));//desire + 30.0*dt (0-100)
	}
	
	private void avanzapaso2(double dt)
	{
		move(3.0*_speed*dt*Math.exp((_energy-100.0)*0.007));//avanza
		
		this._age += dt;
			
		_energy = Math.max(0.0, Math.min(_energy - 18.0*1.2*dt, 100.0));//energy - 18*1.2*dt (0-100)
			
		_desire = Math.max(0.0, Math.min(_desire + 30.0*dt, 100.0));//desire + 30.0*dt (0-100)
	}
}




package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;

class ControlPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final double _dtDEFAULT = 0.03;
	private static final int _nDEFAULT = 60;
	private Controller _ctrl;
	private ChangeRegionsDialog _changeRegionsDialog;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;
	private JButton _fileButton;
	private JButton _viewerButton;
	private JButton _stopButton;
	private JButton _runButton;
	private JButton _regionButton;
	private JTextField dtField;
	
	private int _n;
	private double _dt;
	
	// TODO añade más atributos aquí …
	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_dt = _dtDEFAULT;
		_n = _nDEFAULT;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);
		
		// TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para añadir la línea de separación vertical
		// entre las componentes que lo necesiten.
		_toolaBar.addSeparator();
		
		
		//TODO Inicializar _fc con una instancia de JFileChooser. Para que siempre
		// abre en la carpeta de ejemplos puedes usar:
		//
		// _fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		// "/resources/examples"));
		
		//the file chooser
		_fileButton = new JButton();
		_fc = new JFileChooser();
		_fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		_fileButton.setIcon(new ImageIcon("resources/icons/open.png"));
		//JSONObject _in = ;
		_fileButton.addActionListener((e) ->
		{
			_fc.showOpenDialog(ViewUtils.getWindow(this));
			
			if(_fc.getSelectedFile() != null)
			{
				//_ctrl.reset(); //ver como conseguir la info del mapa
				//_ctrl.loadData();
			}
			else
			{
				ViewUtils.showErrorMsg(this, "INPUT FILE IS NULL");
			}
		});
		
		_fileButton.setToolTipText("Load an input file into the simulator");
		_toolaBar.add(_fileButton);
		
		_toolaBar.addSeparator();
		
		// TODO Inicializar _changeRegionsDialog con instancias del diálogo de cambio
		// de regiones	
				
		
		//viewer button
		_viewerButton = new JButton();
		JFrame _viewerFrame = new JFrame();
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_toolaBar.add(_viewerButton);
		_viewerButton.setToolTipText("Open viewer window");
		_viewerButton.addActionListener((e) -> 
		{
			MapWindow viewer = new MapWindow(_viewerFrame, _ctrl);
			viewer.setVisible(true);
			viewer.setLocationRelativeTo(null);
		});
		
		//regions button
		_regionButton = new JButton();
		_regionButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		_toolaBar.add(_regionButton);
		_regionButton.setToolTipText("Select force laws for groups");
		_regionButton.addActionListener((e) ->
		{
			_changeRegionsDialog.open(ViewUtils.getWindow(this));
		});
		
		
		
		_toolaBar.addSeparator();
		
		//run Button
		_runButton = new JButton();
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_toolaBar.add(_runButton);
		_runButton.setToolTipText("Run the simulator");
		_runButton.addActionListener((e) ->  
		{		
			_viewerButton.setEnabled(false);
			_regionButton.setEnabled(false);
			_quitButton.setEnabled(false);
			_fileButton.setEnabled(false);
					
			_stopped = false;
			run_sim(_n, _dt); 
		});
				
				
		//stop button
		_stopButton = new JButton();
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_toolaBar.add(_stopButton);
		_stopButton.setToolTipText("Stop the simulator");
		_stopButton.addActionListener((e) -> _stopped = true);
		
				
		//steps spinner
		_toolaBar.add(new JLabel("Steps:"));
		SpinnerModel model = new SpinnerNumberModel(_n, 0, 10000, 1);
		JSpinner steps = new JSpinner(model);
		
		
		model.addChangeListener(new ChangeListener() 
		{
			@Override
	        public void stateChanged(ChangeEvent e) 
			{
				_n = (int) steps.getValue();
			}
			});
			
		steps.setValue(_n);
		steps.setToolTipText("Simulation steps to run: 1-10000");
		Dimension d = steps.getPreferredSize();  
	    d.width = 50;  
	    steps.setPreferredSize(d); //size of steps spinner
		_toolaBar.add(steps);
		
		_toolaBar.add(new JLabel("Delta-Time:"));
		dtField = new JTextField(Double.toString(_dt));
		dtField.setToolTipText("Real time(seconds) corresponding to a step");
		_toolaBar.add(dtField);
		
		_toolaBar.addSeparator();
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		
		// Quit Button
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> ViewUtils.quit(this));
		_toolaBar.add(_quitButton);
	
		
	}
	
	
	private void run_sim(int n, double dt) {
		if (n > 0 && !_stopped) {
		try {
		_ctrl.advance(dt);
		SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
		} catch (Exception e) {
			// TODO llamar a ViewUtils.showErrorMsg con el mensaje de error
			// que corresponda
			ViewUtils.showErrorMsg(TOOL_TIP_TEXT_KEY);
			// TODO activar todos los botones
			_viewerButton.setEnabled(true);
			_regionButton.setEnabled(true);
			_quitButton.setEnabled(true);
			_fileButton.setEnabled(true);
			
			_stopped = true;
			}
		} 
		else {
			// TODO activar todos los botones
			_viewerButton.setEnabled(true);
			_regionButton.setEnabled(true);
			_quitButton.setEnabled(true);
			_fileButton.setEnabled(true);
			
			_stopped = true;
		}
	}
}


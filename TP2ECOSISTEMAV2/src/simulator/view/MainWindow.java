package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private Border _blackBorder = BorderFactory.createLineBorder(Color.black, 2);

	public MainWindow(Controller ctrl) {
		super("[ECOSYSTEM SIMULATOR]");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		// TODO crear ControlPanel y añadirlo en PAGE_START de mainPanel
		ControlPanel controlPanel = new ControlPanel(_ctrl);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		
		// TODO crear StatusBar y añadirlo en PAGE_END de mainPanel
		StatusBar statusBar = new StatusBar(_ctrl);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
		
		// Definición del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		// TODO crear la tabla de especies y añadirla a contentPanel.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
		InfoTable _species = new InfoTable("Species", new SpeciesTableModel(_ctrl));
		// add border
		_species.setBorder(BorderFactory.createTitledBorder(_blackBorder, "Species", TitledBorder.LEFT, TitledBorder.TOP));
		setPreferredSize(new Dimension(800, 600));
		contentPanel.add(_species);
		
		
		// TODO crear la tabla de regiones.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamañ
		InfoTable _regiones = new InfoTable("Regions", new SpeciesTableModel(_ctrl));
		// add border
		_regiones.setBorder(BorderFactory.createTitledBorder(_blackBorder, "Regions", TitledBorder.LEFT, TitledBorder.TOP));
		setPreferredSize(new Dimension(800, 600));
		contentPanel.add(_regiones);
		
		// TODO llama a ViewUtils.quit(MainWindow.this) en el método windowClosing
		addWindowListener(new WindowListener()
		{

			@Override
			public void windowOpened(WindowEvent e) {
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				ViewUtils.quit(MainWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
		});
		
		setPreferredSize (new Dimension(700, 300));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}


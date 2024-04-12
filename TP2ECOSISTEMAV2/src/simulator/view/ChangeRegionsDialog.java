package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> _regionsModel;
	private DefaultComboBoxModel<Integer> _fromRowModel;
	private DefaultComboBoxModel<Integer> _toRowModel;

	// TODO en caso de ser necesario, añadir los atributos aquí…
	private int _status = 1;
	private DefaultComboBoxModel<Integer> _fromColModel;
	private DefaultComboBoxModel<Integer> _toColModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _regionsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	private JButton _ok;
	private JButton _cancel;
	
	private	int _rows;
	private	int _cols;
	
	
	ChangeRegionsDialog(Controller ctrl) {
		super((Frame)null, true);
		_ctrl = ctrl;
		initGUI();
		_rows = 0;
		_cols = 0;
		// TODO registrar this como observer;
		_ctrl.addObserver(this);
	}
	
	
	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		// TODO crea varios paneles para organizar los componentes visuales en el
		// dialogo, y añadelos al mainpanel. P.ej., uno para el texto de ayuda,
		// uno para la tabla, uno para los combobox, y uno para los botones.
		JPanel _label_panel = new JPanel();
		mainPanel.add(_label_panel);
		
		JPanel _table_panel = new JPanel();
		_table_panel.setLayout(new BoxLayout(_table_panel, BoxLayout.Y_AXIS));
		mainPanel.add(_table_panel);
		
		JPanel _combo_panel = new JPanel();
		mainPanel.add(_combo_panel);
		
		JPanel _buttons_panel = new JPanel();
		mainPanel.add(_buttons_panel);
		
		// TODO crear el texto de ayuda que aparece en la parte superior del diálogo y
		// añadirlo al panel correspondiente diálogo (Ver el apartado Figuras)
		// _regionsInfo se usará para establecer la información en la tabla
		
		//texto de ayuda
		JLabel _info1 = new JLabel("Select a region type, the rows/cols interval, and provide values for the parameters in the Value Column (default values\n");
		JLabel _info2 = new JLabel("are used for parameters with no value)\n");
		_label_panel.add(_info1);
		_label_panel.add(_info2);
		
		
		//info de la tabla
		_regionsInfo = Main._regions_factory.get_info();
		
		// _dataTableModel es un modelo de tabla que incluye todos los parámetros de
		// la region
		_dataTableModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO hacer editable solo la columna 1
				return column == 1;
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		// TODO crear un JTable que use _dataTableModel, y añadirlo al diálogo
		// _regionsModel es un modelo de combobox que incluye los tipos de regiones		
		JTable _table = new JTable(_dataTableModel);
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table_panel.add(_table);
		
		JScrollPane _dataTableScroll = new JScrollPane(_table);
		_dataTableScroll.setVisible(true); // scroll panel
		
		_table_panel.add(_dataTableScroll);
		
		_regionsModel = new DefaultComboBoxModel<>();
		// TODO añadir la descripción de todas las regiones a _regionsModel, para eso
		// usa la clave “desc” o “type” de los JSONObject en _regionsInfo,
		for (JSONObject info : _regionsInfo)
        {
            _regionsModel.addElement(info.getString("type"));     
        }
		
		// ya que estos nos dan información sobre lo que puede crear la factoría.
		// TODO crear un combobox que use _regionsModel y añadirlo al diálogo.
		JComboBox<String> _lawsCombo = new JComboBox<>(_regionsModel);
		JLabel _regionsLabel = new JLabel("Region type: ");
		_regionsLabel.setAlignmentX(CENTER_ALIGNMENT);
		_combo_panel.add(_regionsLabel);
		_combo_panel.add(_lawsCombo);
		
		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel,
		// _fromColModel y _toColModel.
		_fromRowModel = new DefaultComboBoxModel<>();
		_toRowModel = new DefaultComboBoxModel<>();
		_fromColModel = new DefaultComboBoxModel<>();
		_toColModel = new DefaultComboBoxModel<>();
		
		// TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.
		JComboBox<Integer> _fromRowCombo = new JComboBox<>(_fromRowModel);
		JComboBox<Integer> _toRowCombo = new JComboBox<>(_toRowModel);
		JComboBox<Integer> _fromColCombo = new JComboBox<>(_fromColModel);
		JComboBox<Integer> _toColCombo = new JComboBox<>(_toColModel);
		
		JLabel _rowLabel = new JLabel("Row from/to: ");
		_rowLabel.setAlignmentX(CENTER_ALIGNMENT);
		_combo_panel.add(_rowLabel);
		_combo_panel.add(_fromRowCombo);
		_combo_panel.add(_toRowCombo);
		
		JLabel _colLabel = new JLabel("Column from/to: ");
		_colLabel.setAlignmentX(CENTER_ALIGNMENT);
		_combo_panel.add(_colLabel);
		_combo_panel.add(_fromColCombo);
		_combo_panel.add(_toColCombo);
		
		// TODO crear los botones OK y Cancel y añadirlos al diálogo.
		//boton cancel
		_cancel = new JButton("CANCEL");
		_cancel.addActionListener((e) -> {
			_status = 0;
			setVisible(false);
		});
		
		//boton ok
		_ok = new JButton("OK");
				
		
		
		_buttons_panel.add(_ok);
		_buttons_panel.add(_cancel);
		
		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);
	}
	public void open(Frame parent) {
		setLocation(//
		parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2, //
		parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}
	// TODO el resto de métodos van aquí…

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		_regionsModel.removeAllElements();
		_fromRowModel.removeAllElements();
		_toRowModel.removeAllElements();
		_fromColModel.removeAllElements();
		_toRowModel.removeAllElements();
		
		for (int i = 0; i < map.get_rows(); i++)
        {
            _fromRowModel.addElement(i);
            _toRowModel.addElement(i);
        }
		
		for (int i = 0; i < map.get_cols(); i++)
        {
            _fromColModel.addElement(i);
            _toColModel.addElement(i);
        }
		
		for (JSONObject info : _regionsInfo)
        {
            _regionsModel.addElement(info.getString("type"));
            JSONObject data = info.getJSONObject("data");
            for(String key : data.keySet())
            {
            	String value = data.getString(key);
                _dataTableModel.addRow(new Object[] { key, "", value });
            }
        }
	}


	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		_regionsModel.removeAllElements();
		_fromRowModel.removeAllElements();
		_toRowModel.removeAllElements();
		_fromColModel.removeAllElements();
		_toRowModel.removeAllElements();
		
		for (int i = 0; i < map.get_rows(); i++)
        {
            _fromRowModel.addElement(i);
            _toRowModel.addElement(i);
        }
		
		for (int i = 0; i < map.get_cols(); i++)
        {
            _fromColModel.addElement(i);
            _toColModel.addElement(i);
        }
		
		for (JSONObject info : _regionsInfo)
        {
            _regionsModel.addElement(info.getString("type"));  
            JSONObject data = info.getJSONObject("data");
        }
	}


	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub
		_rows = map.get_rows();
	}
	
}


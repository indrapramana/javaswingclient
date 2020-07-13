package demo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import api.APIClient;
import api.ProductAPI;
import entities.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JFrameMain extends JFrame {

	private JPanel contentPane;
	private JTable tableProduct;
	private JTextField textFieldId;
	private JTextField textFieldName;
	private JTextField textFieldPrice;
	private JTextArea textAreaDescription;
	private JButton btnCreate;
	private JButton buttonUpdate;
	private JButton buttonDelete;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameMain frame = new JFrameMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JFrameMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 622, 442);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 16, 456, 128);
		contentPane.add(scrollPane);

		tableProduct = new JTable();
		tableProduct.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				do_tableProduct_mouseClicked(e);
			}
		});
		scrollPane.setRowHeaderView(tableProduct);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Product Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(32, 156, 434, 241);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblId = new JLabel("Id");
		lblId.setBounds(24, 27, 55, 16);
		panel.add(lblId);

		textFieldId = new JTextField();
		textFieldId.setBounds(97, 27, 277, 28);
		panel.add(textFieldId);
		textFieldId.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(24, 61, 55, 16);
		panel.add(lblName);

		textFieldName = new JTextField();
		textFieldName.setColumns(10);
		textFieldName.setBounds(97, 61, 277, 28);
		panel.add(textFieldName);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(24, 95, 55, 16);
		panel.add(lblPrice);

		textFieldPrice = new JTextField();
		textFieldPrice.setColumns(10);
		textFieldPrice.setBounds(97, 95, 277, 28);
		panel.add(textFieldPrice);

		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(24, 129, 55, 16);
		panel.add(lblDescription);

		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnCreate_actionPerformed(e);
			}
		});
		btnCreate.setBounds(93, 195, 90, 28);
		panel.add(btnCreate);

		buttonUpdate = new JButton("Update");
		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_buttonUpdate_actionPerformed(e);
			}
		});
		buttonUpdate.setBounds(187, 195, 90, 28);
		panel.add(buttonUpdate);

		buttonDelete = new JButton("Delete");
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_buttonDelete_actionPerformed(e);
			}
		});
		buttonDelete.setBounds(284, 195, 90, 28);
		panel.add(buttonDelete);

		textAreaDescription = new JTextArea();
		textAreaDescription.setBounds(97, 129, 277, 61);
		panel.add(textAreaDescription);
		loadData();
	}

	private void loadData() {
		try {
			ProductAPI productAPI = APIClient.getClient().create(ProductAPI.class);
			productAPI.findAll().enqueue(new Callback<List<Product>>() {

				@Override
				public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
					if (response.isSuccessful()) {
						DefaultTableModel defaultTableModel = new DefaultTableModel();
						defaultTableModel.addColumn("Id");
						defaultTableModel.addColumn("Name");
						defaultTableModel.addColumn("Price");
						defaultTableModel.addColumn("Description");
						for (Product product : response.body()) {
							defaultTableModel.addRow(new Object[] { product.getId(), product.getName(),
									product.getPrice(), product.getDescription() });
						}
						tableProduct.setModel(defaultTableModel);
					}
				}

				@Override
				public void onFailure(Call<List<Product>> arg0, Throwable t) {
					JOptionPane.showConfirmDialog(null, t.getMessage());
				}
			});
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, e.getMessage());
		}
	}

	protected void do_btnCreate_actionPerformed(ActionEvent e) {
		try {
			Product product = new Product();
			product.setId(textFieldId.getText());
			product.setName(textFieldName.getText());
			product.setPrice(Double.parseDouble(textFieldPrice.getText()));
			product.setDescription(textAreaDescription.getText());
			ProductAPI productAPI = APIClient.getClient().create(ProductAPI.class);
			productAPI.create(product).enqueue(new Callback<Void>() {

				@Override
				public void onResponse(Call<Void> arg0, Response<Void> response) {
					if (response.isSuccessful()) {
						loadData();
					}
				}

				@Override
				public void onFailure(Call<Void> arg0, Throwable arg1) {
					JOptionPane.showMessageDialog(null, arg1.getMessage());
				}
			});
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage());
		}

	}

	protected void do_tableProduct_mouseClicked(MouseEvent e) {
		int selectedRow = tableProduct.getSelectedRow();
		String id = tableProduct.getValueAt(selectedRow, 0).toString();
		ProductAPI productAPI = APIClient.getClient().create(ProductAPI.class);
		productAPI.find(id).enqueue(new Callback<Product>() {

			@Override
			public void onResponse(Call<Product> arg0, Response<Product> response) {
				if (response.isSuccessful()) {
					Product product = response.body();
					textFieldId.setText(product.getId());
					textFieldName.setText(product.getName());
					textFieldPrice.setText(String.valueOf(product.getPrice()));
					textAreaDescription.setText(product.getDescription());
				}
			}

			@Override
			public void onFailure(Call<Product> arg0, Throwable t) {
				JOptionPane.showMessageDialog(null, t.getMessage());
			}
		});
	}

	protected void do_buttonDelete_actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(null, "Confirm", "Are you sure", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			ProductAPI productAPI = APIClient.getClient().create(ProductAPI.class);
			productAPI.delete(textFieldId.getText()).enqueue(new Callback<Void>() {

				@Override
				public void onResponse(Call<Void> arg0, Response<Void> response) {
					if (response.isSuccessful()) {
						loadData();
					}
				}

				@Override
				public void onFailure(Call<Void> arg0, Throwable t) {
					JOptionPane.showMessageDialog(null, t.getMessage());
				}
			});
		}
	}

	protected void do_buttonUpdate_actionPerformed(ActionEvent e) {
		Product product = new Product();
		product.setId(textFieldId.getText());
		product.setName(textFieldName.getText());
		product.setPrice(Double.parseDouble(textFieldPrice.getText()));
		product.setDescription(textAreaDescription.getText());
		ProductAPI productAPI = APIClient.getClient().create(ProductAPI.class);
		productAPI.update(product).enqueue(new Callback<Void>() {

			@Override
			public void onResponse(Call<Void> arg0, Response<Void> response) {
				if (response.isSuccessful()) {
					loadData();
				}
			}

			@Override
			public void onFailure(Call<Void> arg0, Throwable t) {
				JOptionPane.showMessageDialog(null, t.getMessage());
			}
		});
	}
}

package md.cemirtan.magazinhardware.server;
import md.cemirtan.magazinhardware.Helper;
import md.cemirtan.magazinhardware.Firma;
import md.cemirtan.magazinhardware.Util;
import md.cemirtan.magazinhardware.Core;
import md.cemirtan.magazinhardware.RAM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.function.Consumer;

@SpringBootApplication
public class Server
{
	public static void main(String[] args) throws HibernateException, SQLException
	{
		SpringApplication.run(Server.class, args);
	}
}

@PageTitle("Pagina principala")
@Route
class MainView extends VerticalLayout
{
	private static final long serialVersionUID = 1;
	private String dbms, username, password;
	private boolean modeSwitch;
	private Session session;
	private Consumer<Boolean> refresh;
	
	public MainView()
	{
		var refreshButton = new Button("Autentificare", e -> refresh.accept(true));
		refreshButton.setEnabled(false);
		
		var dbmsRadioGroup = new RadioButtonGroup<String>();
		dbmsRadioGroup.setItems("MySQL", "H2");
		dbmsRadioGroup.setLabel("SGBD");
		dbmsRadioGroup.addValueChangeListener(e -> dbms = e.getValue().toLowerCase());
		dbmsRadioGroup.setValue("MySQL");
		
		var usernameField = new TextField("Username", e -> refreshButton.setEnabled(!e.getValue().isBlank()));
		var passwordField = new PasswordField("Password");
		
		var authLayout = new HorizontalLayout(dbmsRadioGroup, usernameField, passwordField, refreshButton);
		
		var dbLayout = new VerticalLayout();
		dbLayout.setAlignItems(Alignment.CENTER);

		var textAreaList = new TextArea();
		dbLayout.setAlignSelf(Alignment.STRETCH, textAreaList);
		textAreaList.setReadOnly(true);
		textAreaList.getStyle().set("font-family", "monospace");
		
		var grid = new Grid<RAM>(RAM.class, false);
		grid.addColumn(RAM::getId).setHeader("ID");
		grid.addColumn(RAM::getFirma).setHeader("Firma");
		grid.addColumn(RAM::getCoeficient).setHeader("Coeficient");
		grid.addColumn(RAM::getCl).setHeader("CL");
		grid.addColumn(RAM::getCapacitate).setHeader("Capacitate (GB)");
		
		var firmaSelect = new Select<Firma>();
		firmaSelect.setLabel("Firma");

		var radioGroup = new RadioButtonGroup<String>();
		radioGroup.setItems("Hibernate", "JDBC");
		radioGroup.setLabel("Operabilitate:");
		
		refresh = reserved ->
		{
			username = usernameField.getValue();
			password = passwordField.getValue();
			session = Util.getSession(dbms, username, password);
			
			refresh = b ->
			{
				if (modeSwitch)
					try
					{
						session.clear();
						grid.setItems(session.createNativeQuery("SELECT * FROM RAM", RAM.class).list());

						if (b)
							firmaSelect.setItems(session.createNativeQuery("SELECT * FROM Firma", Firma.class).list());
					}
					catch (Exception e)
					{
						Notification.show("Eroare la conexiune.");
					}
				else
					try (var c = Core.getConnection(dbms, username, password))
					{
						textAreaList.setValue(Helper.formatQuery(c, "SELECT * FROM RAM"));
						var rs = Core.executeQuery(c, "SELECT * FROM FIRMA");
						
						if (b)
						{
							var list = new ArrayList<Firma>();
							
							while (rs.next())
								list.add(new Firma(rs.getString("ID")));
							
							firmaSelect.setItems(list);
						}
					}
					catch (SQLException e)
					{
						textAreaList.setValue(e.getMessage());
					}
			};

			radioGroup.addValueChangeListener(e ->
			{
				modeSwitch ^= true;
				grid.setVisible(modeSwitch);
				textAreaList.setVisible(!modeSwitch);
				refresh.accept(true);
			});
			
			radioGroup.setValue("Hibernate");
			authLayout.remove(dbmsRadioGroup, usernameField, passwordField);

			authLayout.add(new Button("Logout", e1 ->
			{
				session.close();
				UI.getCurrent().getPage().reload();
			}));
			
			add(dbLayout);
			addComponentAtIndex(1, new Label("Logat ca " + username));
			refreshButton.setText("Refresh");
		};

		var idField = new IntegerField("ID");
		
		var coeficientField = new IntegerField("Coeficient");
		coeficientField.setMin(1);
		coeficientField.setHasControls(true);
		
		var clField = new IntegerField("CL");
		clField.setMin(1);
		
		var capacitateField = new IntegerField("Capacitate", e ->
		{
			var val = e.getValue();
			
			if (val != null)
				e.getSource().setInvalid((val & (val - 1)) != 0);
		});
		capacitateField.setMin(1);
		
		var formLayout = new HorizontalLayout(idField, firmaSelect, coeficientField, clField, capacitateField);

		formLayout.add(new VerticalLayout(
			new Button("Adaugă/Actualizează", e ->
			{
				if (capacitateField.isInvalid())
				{
					Notification.show("Coeficientul nu este o putere a lui doi.");
					return;
				}
				
				if (formLayout.getChildren().filter(x -> x instanceof AbstractField).anyMatch(x -> ((AbstractField<?, ?>)x).isEmpty()))
				{
					Notification.show("Trebuie de completat formularul.");
					return;
				}
				
				var indicator = "Actualizat";
				
				if (modeSwitch)
				{
					session.beginTransaction();
					
					session.merge(new RAM(
						idField.getValue(),
						firmaSelect.getValue(),
						coeficientField.getValue(),
						clField.getValue(),
						capacitateField.getValue()));
					
					session.getTransaction().commit();
				}
				else
					try (var c = Core.getConnection(dbms, username, password))
					{
						var firma = firmaSelect.getValue().getId();
						
						if (
							Helper.executeBatches(c, "UPDATE RAM SET FirmaID = ?, Coeficient = ?, CL = ?, Capacitate = ? WHERE ID = ?",
							new Object[][]
							{
								{
									firma,
									coeficientField.getValue(), clField.getValue(),
									capacitateField.getValue(), idField.getValue()
								}
							},
							new Integer[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER }
						)[0] == 0)
						{
							Helper.executeBatches(c, "INSERT INTO RAM VALUES (?, ?, ?, ?, ?)",
								new Object[][]
								{
									{
										idField.getValue(), firma,
										coeficientField.getValue(), clField.getValue(),
										capacitateField.getValue()
									}
								},
								new Integer[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER }
							);
							
							indicator = "Adăugat";
						}
					}
					catch (SQLException e1)
					{
						textAreaList.setValue(e1.getMessage());
						return;
					}
				
				refresh.accept(false);
				Notification.show(indicator + " cu succes.");
			}), 
			new Button("Șterge după ID", e ->
			{
				if (idField.isEmpty())
				{
					Notification.show("ID-ul trebuie de completat.");
					return;
				}

				if (modeSwitch)
				{
					session.beginTransaction();	
					session.remove(session.find(RAM.class, idField.getValue()));
					session.getTransaction().commit();
				}
				else
					try (var c = Core.getConnection(dbms, username, password))
					{
						Helper.executeBatches(c, "DELETE FROM RAM WHERE ID = ?",
							new Object[][] { { idField.getValue() } },
							new Integer[] { Types.INTEGER }
						);
					}
					catch (SQLException e1)
					{
						textAreaList.setValue(e1.getMessage());
						return;
					}
				
				refresh.accept(false);
				Notification.show("Șters cu succes.");
			})
		));
		
		grid.addSelectionListener(e ->
		{
			var select = e.getFirstSelectedItem();
			
			if (select.isPresent())
			{
				var item = select.get();
				
				idField.setValue(item.getId());
				firmaSelect.setValue(item.getFirma());
				coeficientField.setValue(item.getCoeficient());
				clField.setValue(item.getCl());
				capacitateField.setValue(item.getCapacitate());
			}
		});
		
		dbLayout.add(
			radioGroup,
			formLayout,
			new H5("Tabela RAM"),
			textAreaList,
			grid
		);
		
		setAlignItems(Alignment.CENTER);
		
		add(
			new H1("Magazin Hardware"),
			authLayout,
			new Hr()
		);
	}
}

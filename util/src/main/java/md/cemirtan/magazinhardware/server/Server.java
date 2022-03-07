package md.cemirtan.magazinhardware.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.dom.Style;

@SpringBootApplication
public class Server
{
	public static void main(String[] args)
	{
		SpringApplication.run(Server.class, args);
	}
}

@PageTitle("Pagina principala")
@Route
class MainView extends VerticalLayout
{
	private static final long serialVersionUID = 1;
	private HorizontalLayout hLayout, hLayoutSpans;	
	private String color = "red";
	
	private Style setShadowColor(Style span)
	{
		return span.set("text-shadow", "0 0 2px black").set("color", color);
	}
	
	private void hLayoutAddSpan(Span span)
	{
		hLayoutSpans.add(span);
		
		if (hLayoutSpans.getComponentCount() > 0)
			hLayout.setVisible(true);
	}
	
	private void hLayoutRemoveSpan(Span span)
	{
		hLayoutSpans.remove(span);
		
		if (hLayoutSpans.getComponentCount() == 0)
			hLayout.setVisible(false);
	}
	
	private void hLayoutUpdateColors()
	{
		hLayoutSpans.getChildren().forEach(x -> setShadowColor(((Span)x).getStyle()));
	}
	
	public MainView()
	{
		hLayout = new HorizontalLayout(
			hLayoutSpans = new HorizontalLayout(),
			new Button("Curăță", e -> 
			{
				hLayoutSpans.removeAll();
				hLayout.setVisible(false);
				Notification.show("Șters complet.");
			})
		);
		
		hLayoutSpans.getStyle()
			.set("border", "2px inset silver")
			.set("padding", "5px");
		
		hLayout.setVisible(false);

		var progressBar = new ProgressBar(0, 100);
		progressBar.setHeight("25px");
		
		var iField = new IntegerField(e -> progressBar.setValue(e.getValue()));
		iField.setMax(100);
		iField.setMin(0);
		iField.setStep(5);
		iField.setValue(50);
		iField.setHasControls(true);
		
		var vLayout = new VerticalLayout(new H5("Test cu procente"), iField, progressBar);
		vLayout.setAlignItems(Alignment.CENTER);
		vLayout.getStyle().set("border", "2px dotted silver").set("width", "initial");
		
		var select = new Select<String>("red", "green", "blue");
		select.setLabel("Culoarea obiectelor");
		select.setValue("red");
		
		var checkbox = new Checkbox("Actualizare automată", e -> 
		{
			if (e.getValue())
				hLayoutUpdateColors();
		});

		select.addValueChangeListener(e ->
		{
			color = e.getValue();
			
			if (checkbox.getValue())
				hLayoutUpdateColors();
		});
		
		var addDialog = new Dialog();
		var dialogTextField = new TextField("Denumire object");

		addDialog.add(
			dialogTextField, 
			new Button("Adaugă", e ->
			{
				if (dialogTextField.getValue().isBlank())
					Notification.show("Se necesită să fie completat.");
				else
				{
					var span = new Span(dialogTextField.getValue());
					span.addClickListener(e1 -> 
					{
						hLayoutRemoveSpan(e1.getSource());
						Notification.show("Șters.");
					});
					setShadowColor(span.getStyle()).set("cursor", "pointer");
		
					hLayoutAddSpan(span);
					addDialog.close();
					Notification.show("Adăugat.");
				}
			})
		);
		
		setAlignItems(Alignment.CENTER);
		add(
			new H1("Prima mea pagină Vaadin"), 
			vLayout, 
			select, 
			checkbox, 
			new Button("Adaugă un obiect", e -> 
			{
				dialogTextField.clear();
				addDialog.open();
				dialogTextField.focus();
			}), 
			hLayout,
			new Hr(),
			new Footer(new Span("Copyright 2022 Cemîrtan Cristian"))
		);
	}
}

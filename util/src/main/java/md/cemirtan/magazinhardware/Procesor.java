package md.cemirtan.magazinhardware;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Procesor
{
	@Id
	private int id;

	@Column(nullable=false)
	private String firmaId;

	@Column(nullable=false)
	private String model;

	@Column(nullable=false)
	private double viteza;

	@Column(nullable=false)
	private double pret;

	public Procesor() {}

	public Procesor(int id, String firmaId, String model, double viteza, double pret)
	{
		this.id = id;
		this.firmaId = firmaId;
		this.model = model;
		this.viteza = viteza;
		this.pret = pret;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getFirmaId()
	{
		return firmaId;
	}

	public void setFirmaId(String firmaId)
	{
		this.firmaId = firmaId;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;
	}

	public double getViteza()
	{
		return viteza;
	}

	public void setViteza(double viteza)
	{
		this.viteza = viteza;
	}

	public double getPret()
	{
		return pret;
	}

	public void setPret(double pret)
	{
		this.pret = pret;
	}

	@Override
	public String toString()
	{
		return String.format("(%s, %s, %s, %s MHz, %s MDL)", getId(), getFirmaId(), getModel(), getViteza(), getPret());
	}
}

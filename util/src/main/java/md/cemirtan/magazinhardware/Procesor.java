package md.cemirtan.magazinhardware;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.util.Objects;

@Entity
public class Procesor
{
	@Id
	private int id;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "FirmaID", nullable = false)
	private Firma firma;

	@Column(nullable = false)
	private String model;

	@Column(nullable = false)
	private double viteza, pret;
	
	@OneToOne(mappedBy = "procesor", cascade = { CascadeType.ALL })
	private CPU cpu;
	
	@OneToOne(mappedBy = "procesor", cascade = { CascadeType.ALL })
	private GPU gpu;

	public Procesor() {}

	public Procesor(int id, Firma firma, String model, double viteza, double pret)
	{
		this.id = id;
		this.firma = firma;
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

	public Firma getFirma()
	{
		return firma;
	}
	
	public void setFirma(Firma firma)
	{
		this.firma = firma;
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
	
	public CPU getCpu()
	{
		return cpu;
	}
	
	public void setCpu(CPU cpu)
	{
		this.cpu = cpu;
	}
	
	public GPU getGpu()
	{
		return gpu;
	}
	
	public void setGpu(GPU gpu)
	{
		this.gpu = gpu;
	}

	@Override
	public String toString()
	{
		return String.format("(%s, %s, %s, %s GHz, %s MDL)", getId(), getFirma(), getModel(), getViteza(), getPret());
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
	
		if (!(obj instanceof Procesor))
			return false;
		
		var o = (Procesor)obj;

		return 
			id == o.id && Objects.equals(firma, o.firma) && 
			Objects.equals(model, o.model) && Double.compare(pret, o.pret) == 0 &&
			Double.compare(viteza, o.viteza) == 0;
	}
}

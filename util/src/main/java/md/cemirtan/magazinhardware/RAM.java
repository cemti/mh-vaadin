package md.cemirtan.magazinhardware;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.util.Objects;

@Entity
public class RAM
{
	@Id
	private int id;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "firmaid", nullable = false)
	private Firma firma;
	
	@Column(nullable = false)
	private int coeficient, cl, capacitate;
	
	public RAM() {}
	
	public RAM(int id, Firma firma, int coeficient, int cl, int capacitate)
	{
		this.id = id;
		this.firma = firma;
		this.coeficient = coeficient;
		this.cl = cl;
		this.capacitate = capacitate;
	}

	public int getId()
	{
		return id;
	}

	public Firma getFirma()
	{
		return firma;
	}

	public void setFirma(Firma firma)
	{
		this.firma = firma;
	}

	public int getCoeficient()
	{
		return coeficient;
	}

	public void setCoeficient(int coeficient)
	{
		this.coeficient = coeficient;
	}

	public int getCl()
	{
		return cl;
	}

	public void setCl(int cl)
	{
		this.cl = cl;
	}

	public int getCapacitate()
	{
		return capacitate;
	}

	@Override
	public String toString()
	{
		return String.format("(%s, %s, %sx, CL %s, %s GB)", getId(), getFirma(), getCoeficient(), getCl(), getCapacitate());
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
		
		if (!(obj instanceof RAM))
			return false;
		
		var o = (RAM)obj;
		
		return
			capacitate == o.capacitate && cl == o.cl && 
			coeficient == o.coeficient && Objects.equals(firma, o.firma) && id == o.id;
	}
}

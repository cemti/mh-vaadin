package md.cemirtan.magazinhardware;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.util.Objects;

@Entity
public class Disk
{
	@Id
	private int id;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "FirmaID", nullable = false)
	private Firma firma;
	
	private Integer viteza;
	
	@Column(nullable = false)
	private int capacitate;
	
	public Disk() {}
	
	public Disk(int id, Firma firma, Integer viteza, int capacitate)
	{
		this.id = id;
		this.firma = firma;
		this.viteza = viteza;
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

	public int getCapacitate()
	{
		return capacitate;
	}

	public Integer getViteza()
	{
		return viteza;
	}
	
	@Override
	public String toString()
	{
		var rpm = getViteza();
		return String.format("(%s, %s, %s, %s GB)", getId(), getFirma(), rpm == null ? "SSD" : rpm + " RPM", getCapacitate());
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
		
		if (!(obj instanceof Disk))
			return false;
		
		var o = (Disk)obj;

		return 
			capacitate == o.capacitate && Objects.equals(firma, o.firma) &&
			id == o.id && Objects.equals(viteza, o.viteza);
	}
}

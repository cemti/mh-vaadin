package md.cemirtan.magazinhardware;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.util.Objects;

@Entity
public class CPU
{
	@Id
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "ID")
	private Procesor procesor;

	@Column(nullable = false)
	private int nuclee;
	
	public CPU() {}
	
	public CPU(Procesor procesor, int nuclee)
	{
		this.procesor = procesor;
		this.nuclee = nuclee;
	}

	public Procesor getProcesor()
	{
		return procesor;
	}

	public int getNuclee()
	{
		return nuclee;
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s, %s nuclee)", getProcesor(), getNuclee());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(nuclee, procesor);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		
		if (!(obj instanceof CPU))
			return false;

		var o = (CPU)obj;
		return nuclee == o.nuclee && Objects.equals(procesor, o.procesor);
	}
}

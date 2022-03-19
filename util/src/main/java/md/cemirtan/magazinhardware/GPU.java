package md.cemirtan.magazinhardware;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class GPU implements Serializable
{
	private static final long serialVersionUID = 1;

	@Id
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "ID")
	private Procesor procesor;
	
	@Column(nullable = false)
	private int vram;
	
	public GPU() {}
	
	public GPU(Procesor procesor, int vram)
	{
		this.procesor = procesor;
		this.vram = vram;
	}

	public Procesor getProcesor()
	{
		return procesor;
	}

	public void setProcesor(Procesor procesor)
	{
		this.procesor = procesor;
	}

	public int getVram()
	{
		return vram;
	}
	
	public void setVram(int vram)
	{
		this.vram = vram;
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s, %s GB)", getProcesor(), getVram());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(procesor, vram);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		
		if (!(obj instanceof GPU))
			return false;
		
		var o = (GPU)obj;
		return Objects.equals(procesor, o.procesor) && vram == o.vram;
	}
}

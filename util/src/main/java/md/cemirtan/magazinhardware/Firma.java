package md.cemirtan.magazinhardware;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
public class Firma
{
	@Id
	private String id;
	
	@OneToMany(mappedBy = "firma", cascade = { CascadeType.ALL })
	private Set<Procesor> procesorSet;
	
	@OneToMany(mappedBy = "firma", cascade = { CascadeType.ALL })
	private Set<RAM> ramSet;
	
	@OneToMany(mappedBy = "firma", cascade = { CascadeType.ALL })
	private Set<Disk> diskSet;
	
	public Firma() {}
	
	public Firma(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return toString();
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public Set<Procesor> getProcesorSet()
	{
		return procesorSet;
	}
	
	public Set<RAM> getRamSet()
	{
		return ramSet;
	}

	public Set<Disk> getDiskSet()
	{
		return diskSet;
	}

	@Override
	public String toString()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof Firma && id == ((Firma)obj).id);
	}
}

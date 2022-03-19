package md.cemirtan.magazinhardware;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Calculator implements Serializable
{
	private static final long serialVersionUID = 1;

	@Id
	private int id;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name="CPUID", nullable = false)
	private CPU cpu;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name="GPUID", nullable = false)
	private GPU gpu;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name="RAM_A_ID", nullable = false)
	private RAM ramA;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name="RAM_B_ID")
	private RAM ramB;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name="DiskID", nullable = false)
	private Disk disk;
	
	public Calculator() {}
	
	public Calculator(int id, CPU cpu, GPU gpu, RAM ramA, RAM ramB, Disk disk)
	{
		this.id = id;
		this.cpu = cpu;
		this.gpu = gpu;
		this.ramA = ramA;
		this.ramB = ramB;
		this.disk = disk;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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

	public RAM getRamA()
	{
		return ramA;
	}

	public void setRamA(RAM ram)
	{
		this.ramA = ram;
	}

	public RAM getRamB()
	{
		return ramB;
	}

	public void setRamB(RAM ram)
	{
		this.ramB = ram;
	}

	public Disk getDisk()
	{
		return disk;
	}

	public void setDisk(Disk disk)
	{
		this.disk = disk;
	}
	
	@Override
	public String toString()
	{
		return String.format("(\n\t%s,\n\t%s,\n\t%s,\n\t%s,\n\t%s,\n\t%s\n)", 
			getId(), getCpu(), getGpu(), getRamA(), getRamB(), getDisk());
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof Calculator))
			return false;
		
		var o = (Calculator)obj;
		
		return 
			id == o.id && Objects.equals(cpu, o.cpu) &&
			Objects.equals(disk, o.disk) && Objects.equals(gpu, o.gpu) && 
			Objects.equals(ramA, o.ramA) && Objects.equals(ramB, o.ramB);
	}
}

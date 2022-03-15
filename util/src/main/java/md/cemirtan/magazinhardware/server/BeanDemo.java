package md.cemirtan.magazinhardware.server;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import md.cemirtan.magazinhardware.Calculator;
import md.cemirtan.magazinhardware.RAM;

public class BeanDemo
{
	public static void main(String[] args)
	{
		try (var context = new ClassPathXmlApplicationContext("beans.xml"))
		{
			var calculator = (Calculator)context.getBean("calculator1");
			System.out.println(calculator.toString());
			
			var ram = (RAM)context.getBean("ram1");

			System.out.printf("Testare: %b %b\n", 
				calculator.getRamA() == ram,
				calculator.getCpu().getProcesor() == context.getBean("procesor1_1")
			);
		}
	}
}

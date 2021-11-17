package centroVacunacion;

public class Persona {
	private Integer dni;
	private Fecha nacimiento;
	private boolean empleadoSalud;
	private boolean persDeRiesgo;

	public Persona(int dni, Fecha nacimiento, boolean salud, boolean riesgo) {
		if (Fecha.diferenciaAnios(Fecha.hoy(), nacimiento) >= 18) {
			this.dni = dni;
			this.nacimiento = nacimiento;
			empleadoSalud = salud;
			persDeRiesgo = riesgo;
		} else {
			throw new RuntimeException("la persona ha ingresar debe ser mayor de edad");
		}
	}

	public int consultarDni() {
		return dni;
	}

	public int consultarEdad() {
		return Fecha.diferenciaAnios(Fecha.hoy(), nacimiento);
	}

	public boolean esPersonalSalud() {
		return empleadoSalud;
	}

	public boolean esPesonaRiesgo() {
		return persDeRiesgo;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("DNI: ").append(dni);
		return sBuilder.toString();
	}
}

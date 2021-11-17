package centroVacunacion;

public class Turno {
	Fecha fecha;
	private Vacuna vacuna;
	private Persona persona;

	public Turno(Fecha fecha, Vacuna vacuna, Persona persona) {
		this.fecha = fecha;
		this.vacuna = vacuna;
		this.persona = persona;
	}

	public Fecha consultarFechaTurno() {
		return fecha;
	}

	public Vacuna consultarVacuna() {
		return vacuna;
	}

	public Persona consultarPersona() {
		return persona;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(fecha).append(", ").append(persona).append(", ").append(vacuna);
		return sBuilder.toString();
	}
}

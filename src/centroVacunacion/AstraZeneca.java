package centroVacunacion;

public class AstraZeneca extends VacunaTodoPublico {

	public AstraZeneca(Fecha fecha, int cant) {
		super(fecha, cant);
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return "AstraZeneca";
	}

	@Override
	public int tempDeAlmacenamiento() {
		return 3;
	}

	@Override
	public boolean equals(Object vac) {
		if (vac instanceof AstraZeneca) {
			return ((Vacuna) vac).consultarFechaIngreso().equals(super.consultarFechaIngreso());
		}
		return false;
	}
}

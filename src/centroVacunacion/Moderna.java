package centroVacunacion;

public class Moderna extends VacunaTodoPublico {

	public Moderna(Fecha fecha, int cant) {
		super(fecha, cant);
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return "Moderna";
	}

	@Override
	public int tempDeAlmacenamiento() {
		return -18;
	}

	@Override
	public boolean equals(Object vac) {
		if (vac instanceof Moderna) {
			return ((Vacuna) vac).consultarFechaIngreso().equals(super.consultarFechaIngreso());
		}
		return false;
	}
}

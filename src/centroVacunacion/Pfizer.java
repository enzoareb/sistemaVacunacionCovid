package centroVacunacion;

public class Pfizer extends VacunaMayores60 {
	public Pfizer(Fecha fecha, int cant) {
		super(fecha, cant);
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return "Pfizer";
	}

	@Override
	public int tempDeAlmacenamiento() {
		return -18;
	}

	@Override
	public boolean equals(Object vac) {
		if (vac instanceof Pfizer) {
			return ((Vacuna) vac).consultarFechaIngreso().equals(super.consultarFechaIngreso());
		}
		return false;
	}
}

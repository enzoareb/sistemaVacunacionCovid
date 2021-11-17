package centroVacunacion;

public class Sputnik extends VacunaMayores60 {
	public Sputnik(Fecha fecha, int cant) {
		super(fecha, cant);
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return "Sputnik";
	}

	@Override
	public int tempDeAlmacenamiento() {
		return 3;
	}

	@Override
	public boolean equals(Object vac) {
		if (vac instanceof Sputnik) {
			return ((Vacuna) vac).consultarFechaIngreso().equals(super.consultarFechaIngreso());
		}
		return false;
	}
}

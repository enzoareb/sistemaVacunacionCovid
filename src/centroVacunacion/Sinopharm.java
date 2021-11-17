package centroVacunacion;

public class Sinopharm extends VacunaTodoPublico {
	public Sinopharm(Fecha fecha, int cant) {
		super(fecha, cant);
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return "Sinopharm";
	}

	@Override
	public int tempDeAlmacenamiento() {
		return 3;
	}

	@Override
	public boolean equals(Object vac) {
		if (vac instanceof Sinopharm) {
			return ((Vacuna) vac).consultarFechaIngreso().equals(super.consultarFechaIngreso());
		}
		return false;
	}
}

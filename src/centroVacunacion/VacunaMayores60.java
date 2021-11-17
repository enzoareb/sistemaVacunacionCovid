package centroVacunacion;

public abstract class VacunaMayores60 extends Vacuna {
	public VacunaMayores60(Fecha fecha, int cant) {
		super(fecha, cant);
	}

	@Override
	public abstract String getTipo();

	@Override
	public boolean soloParaMayores60() {
		return true;
	}

	@Override
	public abstract int tempDeAlmacenamiento();

	@Override
	public String toString() {
		return super.toString() + "Apta solo para mayores de 60";
	}
}

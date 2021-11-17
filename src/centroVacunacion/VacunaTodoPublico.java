package centroVacunacion;

public abstract class VacunaTodoPublico extends Vacuna {
	public VacunaTodoPublico(Fecha fecha, int cant) {
		super(fecha, cant);
	}

	@Override
	public abstract String getTipo();

	@Override
	public boolean soloParaMayores60() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public abstract int tempDeAlmacenamiento();
}

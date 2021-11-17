package centroVacunacion;

public abstract class Vacuna {
	private String tipo;
	private Float tempAlmacenamiento;
	private int cantidad;
	private Fecha fechaIngreso;

	public Vacuna(Fecha f, int cant) {
		this.cantidad = cant;
		fechaIngreso = f;
	}

	public abstract String getTipo();

	public abstract boolean soloParaMayores60();

	public void agregarFechaIngreso(Fecha fecha) {
		fechaIngreso = fecha;
	}

	public Fecha consultarFechaIngreso() {
		return fechaIngreso;
	}

	public int cantVacunasDisponibles() {
		return cantidad;
	}

	public void agregarVacunas(int cant) {
		if (cantidad >= 0) {
			cantidad += cant;
		} else {
			System.out.println("la cantidad ingresada es incorrecta");
		}
	}

	public void descontarVacuna() {
		cantidad--;
	}

	public abstract int tempDeAlmacenamiento();

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("vacuna: ").append(tipo).append("\ntemperatura de almacenamiento: ").append(tempAlmacenamiento);
		return sBuilder.toString();
	}
}

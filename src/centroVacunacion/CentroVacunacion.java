package centroVacunacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class CentroVacunacion {
	private HashSet<Vacuna> vacunasHashSet;
	private HashMap<Integer, Persona> listaEsperaHashMap;
	private HashSet<Turno> turnosHashSet;
	private HashMap<Integer, String> vacunadosHashMap;
	private HashMap<String, Integer> vacunasVencidashHashMap;
	private Integer capacidadVacunatorio;
	private String nombreCentro;

	public CentroVacunacion(String nombreCentro, int capacidad) {
		if (capacidad > 0) {
			vacunasHashSet = new HashSet<>();
			listaEsperaHashMap = new HashMap<>();
			turnosHashSet = new HashSet<>();
			vacunadosHashMap = new HashMap<>();
			vacunasVencidashHashMap = new HashMap<>();
			capacidadVacunatorio = capacidad;
			this.nombreCentro = nombreCentro;
		} else {
			throw new RuntimeException("la cacidad del vacunatorio debe ser mayora a 0");
		}
	}
	
	public void ingresarVacunas(String tipo, int cant, Fecha fechaIngreso) {
		boolean estaEnElInventario = false;
		Vacuna vac= null;
		if (cant > 0) {
			if (tipo.equals("Pfizer")) {
				vac = new Pfizer(fechaIngreso, cant);
			}	
			if (tipo.equals("Sputnik")) {
				vac = new Sputnik(fechaIngreso, cant);
			}
			if (tipo.equals("AstraZeneca")) {
				vac = new AstraZeneca(fechaIngreso, cant);
			}
			if (tipo.equals("Sinopharm")) {
				vac = new Sinopharm(fechaIngreso, cant);
			}
			if (tipo.equals("Moderna")) {
				vac = new Moderna(fechaIngreso, cant);
			}
			if (vac!=null) {
				for (Vacuna vacuna : vacunasHashSet) {
					if (vac.equals(vacuna)) {
						vacuna.agregarVacunas(cant);
						estaEnElInventario = true;
					}
				}
				if (!estaEnElInventario) {
					vacunasHashSet.add(vac);
				}
			}
		} else {
			throw new RuntimeException("la cantidad ingresada no es valida");
		}
	}

	public int vacunasDisponibles() {
		int contador = 0;
		for (Vacuna vacuna : vacunasHashSet) {
			contador = contador + vacuna.cantVacunasDisponibles();
		}
		return contador;
	}

	public int vacunasDisponibles(String tipo) {
		int contador = 0;
		for (Vacuna vacuna : vacunasHashSet) {
			if (vacuna.getTipo().equals(tipo)) {
				contador = contador + vacuna.cantVacunasDisponibles();
			}
		}
		return contador;
	}

	public void inscribirPersona(int dni, Fecha nacimiento, boolean salud, boolean riesgo) {
		if (!listaEsperaHashMap.containsKey(dni)) {
			Persona persona = new Persona(dni, nacimiento, salud, riesgo);
			listaEsperaHashMap.put(dni, persona);
		} else {
			throw new RuntimeException("la persona ingresada ya se encuentra inscripta");
		}
	}

	public void generarTurnos(Fecha fecha) {
		if (fecha.posterior(Fecha.hoy())) {
			// repongo las vacunas de las personas que tenian turno y no acudieron a
			// vacunarse
			reponerVacunas();
			descartarVacunasVencidas();
			Fecha f = new Fecha(fecha);
			int backUpCapacidad = capacidadVacunatorio;
			// si la lista de espera no esta vacia
			while (listaEsperaHashMap.size() != 0) {
				agregarPersonalSalud(f);
				agregarMayores60(f);
				agregarGrupoRiesgo(f);
				agregarRestoPoblacion(f);
				// fecha siguiente
				f.avanzarUnDia();
				f = new Fecha(f);
				capacidadVacunatorio = backUpCapacidad;
			}
		} else {
			throw new RuntimeException();
		}
	}

	private void descartarVacunasVencidas() {
		Iterator<Vacuna> it = vacunasHashSet.iterator();
		// recorro la coleccion de vacunas
		while (it.hasNext()) {
			Vacuna vacuna = it.next();
			// si es Pfizer
			if (vacuna instanceof Pfizer) {
				// si la fecha comparada al dia de hoy es mayor a 30, entonces esta vencida
				if (vacuna.consultarFechaIngreso().diasDeDiferencia(Fecha.hoy()) > 30) {
					// agrego esas vacunas a la coleccion de vacunas vencidas
					vacunasVencidashHashMap.put(vacuna.getTipo(), vacuna.cantVacunasDisponibles());
					// elimino la vacuna vencida
					it.remove();
				}
			}
			// si es Moderna
			if (vacuna instanceof Moderna) {
				// si la fecha comparada al dia de hoy es mayor a 60 ,entonces esta vencida
				if (vacuna.consultarFechaIngreso().diasDeDiferencia(Fecha.hoy()) > 60) {
					vacunasVencidashHashMap.put(vacuna.getTipo(), vacuna.cantVacunasDisponibles());
					// elimino la vacuna vencida
					it.remove();
				}
			}
		}
	}

	private void reponerVacunas() {
		// recorro los turnos
		Iterator<Turno> it = turnosHashSet.iterator();
		while (it.hasNext()) {
			Turno turno = (Turno) it.next();
			// si la fecha del turno es anterior al dia de hoy
			if (turno.consultarFechaTurno().anterior(Fecha.hoy())) {
				// devuelvo la vacuna al stock
				ingresarVacunas(turno.consultarVacuna().getTipo(), 1, turno.consultarVacuna().consultarFechaIngreso());
				// elimino el turno
				it.remove();
			}
		}
	}

	private void agregarPersonalSalud(Fecha fecha) {
		// recorro las vacunas
		for (Vacuna vacuna : vacunasHashSet) {
			// recorro lista de espera
			Iterator<Persona> it = listaEsperaHashMap.values().iterator();
			while (it.hasNext()) {
				Persona persona = (Persona) it.next();
				// (si la vacuna no es para mayores de 60) o (vacuna es para mayores de 60 &
				// persona edad mayor 60)
				if (!vacuna.soloParaMayores60() || (vacuna.soloParaMayores60() && persona.consultarEdad() > 60)) {
					// si (persona es personal de salud y vacuna.cantidad>0 y capacidad>0)
					if (persona.esPersonalSalud() && vacuna.cantVacunasDisponibles() > 0 && capacidadVacunatorio > 0) {
						// agrego el turno a la lista de turnos
						turnosHashSet.add(new Turno(new Fecha(fecha.dia(), fecha.mes(), fecha.anio()), vacuna, persona));
						// capacidad -1
						capacidadVacunatorio--;
						// vacuna.cantidad -1--
						vacuna.descontarVacuna();
						// elimino a la persona de la lista de espera
						it.remove();
					}
				}
			}
		}
	}

	private void agregarMayores60(Fecha fecha) {
		for (Vacuna vacuna : vacunasHashSet) {
			if (vacuna.soloParaMayores60()) {
				Iterator<Persona> it = listaEsperaHashMap.values().iterator();
				while (it.hasNext()) {
					Persona persona = (Persona) it.next();
					// si (persona mayor 60 y vacuna.cantidad>0 y capacidad>0)
					if (persona.consultarEdad() > 60 && vacuna.cantVacunasDisponibles() > 0
							&& capacidadVacunatorio > 0) {
						turnosHashSet.add(new Turno(new Fecha(fecha.dia(), fecha.mes(), fecha.anio()), vacuna, persona));
						capacidadVacunatorio--;
						vacuna.descontarVacuna();
						it.remove();
					}
				}
			}

		}
	}

	private void agregarGrupoRiesgo(Fecha fecha) {
		for (Vacuna vacuna : vacunasHashSet) {
			// si no es para mayores de 60
			if (!vacuna.soloParaMayores60()) {
				Iterator<Persona> it = listaEsperaHashMap.values().iterator();
				while (it.hasNext()) {
					Persona persona = (Persona) it.next();
					// si (persona de riesgo y vacuna.cantidad>0 y capacidad>0)
					if (persona.esPesonaRiesgo() && vacuna.cantVacunasDisponibles() > 0 && capacidadVacunatorio > 0) {
						turnosHashSet.add(new Turno(new Fecha(fecha.dia(), fecha.mes(), fecha.anio()), vacuna, persona));
						capacidadVacunatorio--;
						vacuna.descontarVacuna();
						it.remove();
					}
				}
			}
		}
	}

	private void agregarRestoPoblacion(Fecha fecha) {
		for (Vacuna vacuna : vacunasHashSet) {
			if (!vacuna.soloParaMayores60()) {
				Iterator<Persona> it = listaEsperaHashMap.values().iterator();
				while (it.hasNext()) {
					Persona persona = (Persona) it.next();
					if (vacuna.cantVacunasDisponibles() > 0 && capacidadVacunatorio > 0) {
						turnosHashSet.add(new Turno(new Fecha(fecha.dia(), fecha.mes(), fecha.anio()), vacuna, persona));
						capacidadVacunatorio--;
						vacuna.descontarVacuna();
						it.remove();
					}
				}
			}
		}

	}

	public ArrayList<Integer> listaDeEspera() {
		ArrayList<Integer> nuevaList = new ArrayList<>();
		for (int dni : listaEsperaHashMap.keySet()) {
			nuevaList.add(dni);
		}
		return nuevaList;
	}

	public ArrayList<Integer> turnosConFecha(Fecha fecha) {
		ArrayList<Integer> nArrayList = new ArrayList<>();
		for (Turno turno : turnosHashSet) {
			if (turno.consultarFechaTurno().equals(fecha)) {
				nArrayList.add(turno.consultarPersona().consultarDni());
			}
		}
		return nArrayList;
	}

	public void vacunarInscripto(int dni, Fecha fechaVacunacion) {
		boolean estaInscripto = false;
		boolean fechaNoValida = true;
		// recorro turnos
		Iterator<Turno> it = turnosHashSet.iterator();
		while (it.hasNext()) {
			Turno turno = (Turno) it.next();
			// si las fechas de los turnos coinciden
			if (turno.consultarFechaTurno().equals(fechaVacunacion)) {
				fechaNoValida = false;
				// si los dni coinciden
				if (turno.consultarPersona().consultarDni() == dni) {
					// agrego a la persona a la lista de vacunados
					vacunadosHashMap.put(turno.consultarPersona().consultarDni(), turno.consultarVacuna().getTipo());
					it.remove();
					estaInscripto = true;
				}
			}
		}
		if (!estaInscripto || fechaNoValida) {
			throw new RuntimeException();
		}
	}

	public HashMap<Integer, String> reporteVacunacion() {
		return vacunadosHashMap;
	}

	public HashMap<String, Integer> reporteVacunasVencidas() {
		return vacunasVencidashHashMap;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("centro de vacunacion: ").append(nombreCentro).append("\ncapacidad: ")
				.append(capacidadVacunatorio).append("\nvacunas disponibles: ").append(vacunasDisponibles())
				.append("\ncantidad de personas en lista de espera: ").append(listaDeEspera().size())
				.append("\ncantidad de turnos asignados: ").append(turnosHashSet.size()).append("\nvacunas aplicadas: ")
				.append(vacunadosHashMap.size());
		return sBuilder.toString();
	}

}

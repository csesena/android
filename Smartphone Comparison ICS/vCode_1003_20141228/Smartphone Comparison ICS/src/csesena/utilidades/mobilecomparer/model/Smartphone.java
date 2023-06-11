package csesena.utilidades.mobilecomparer.model;

public class Smartphone {

	int id;
	String nombre;
	String procesador;
	String gpu;
	String ram;
	String memoria;
	String card_slot;
	String so;
	String bateria;
	String pantalla;
	String tamano_pantalla;
	String res_pantalla;
	String proteccion;
	String camara;
	String conectividad;
	String nfc;
	String gps;
	String radio;
	String medidas;
	String peso;
	String precio;
	String puntuacion_total;
	String comentario;
	String imagen;

	public Smartphone() {

	}

	public Smartphone(int idP, String n, String p, String g, String r,
			String m, String cs, String soP, String b, String pant,
			String tpant, String rpant, String ppant, String cam, String conec,
			String nfcP, String gpsP, String rad, String med, String pes,
			String prec, String punt, String com, String im) {
		id = idP;
		nombre = n;
		procesador = p;
		gpu = g;
		ram = r;
		memoria = m;
		card_slot = cs;
		so = soP;
		bateria = b;
		pantalla = pant;
		tamano_pantalla = tpant;
		res_pantalla = rpant;
		proteccion = ppant;
		camara = cam;
		conectividad = conec;
		nfc = nfcP;
		gps = gpsP;
		radio = rad;
		medidas = med;
		peso = pes;
		precio = prec;
		puntuacion_total = punt;
		comentario = com;
		imagen = im;
	}

	public int getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public String getCPU() {
		return procesador;
	}

	public String getGPU() {
		return gpu;
	}

	public String getRAM() {
		return ram;
	}

	public String getMemoria() {
		return memoria;
	}

	public String getCardSlot() {
		return card_slot;
	}

	public String getSO() {
		return so;
	}

	public String getBateria() {
		return bateria;
	}

	public String getPantalla() {
		return pantalla;
	}

	public String getTamanoPantalla() {
		return tamano_pantalla;
	}

	public String getResolucionPantalla() {
		return res_pantalla;
	}

	public String getProteccion() {
		return proteccion;
	}

	public String getCamara() {
		return camara;
	}

	public String getConectividad() {
		return conectividad;
	}

	public String getNFC() {
		return nfc;
	}

	public String getGPS() {
		return gps;
	}

	public String getRadio() {
		return radio;
	}

	public String getMedidas() {
		return medidas;
	}

	public String getPeso() {
		return peso;
	}

	public String getPrecio() {
		return precio;
	}

	public String getPuntuacion() {
		return puntuacion_total;
	}
	
	public String getComentario() {
		return comentario;
	}

	public String getImagenURL() {
		return imagen;
	}
}

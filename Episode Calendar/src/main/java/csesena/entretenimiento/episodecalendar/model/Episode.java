package csesena.entretenimiento.episodecalendar.model;

/**
 * Created by casanchezs on 02/03/2015.
 */
public class Episode {

    int m_id = -1;
    String m_nombre = "";
    int m_nEpisodio = -1;
    int m_nTemporada = -1;
    int m_idserie;
    String m_serie = "";
    String m_fecha = "2015-01-01 00:00:00";
    String m_resumen = "";
    String m_imagen = "";

    public Episode(){}

    public Episode(int idP, String nombreP, int nEpisodioP, int nTemporadaP, int idserieP, String serieP, String fechaP, String resumenP, String imagenP) {
        m_id = idP;
        m_nombre = nombreP;
        m_nEpisodio = nEpisodioP;
        m_nTemporada = nTemporadaP;
        m_idserie = idserieP;
        m_serie = serieP;
        m_fecha = fechaP;
        m_resumen = resumenP;
        m_imagen = imagenP;
    }

    public Episode(int idP, String nombreP, String imagenP) {
        m_id = idP;
        m_nombre = nombreP;
        m_imagen = imagenP;
    }

    public Episode(TVShow tvShow) {
        m_id = tvShow.getID();
        m_nombre = tvShow.getNombre();
        m_resumen = tvShow.getResumen();
        m_imagen = tvShow.getImagen();
    }

    public Episode(String nombreP) {
        m_nombre = nombreP;
    }

    public int getID()
    {
        return m_id;
    }

    public String getNombre()
    {
        return m_nombre;
    }

    public String getNSNE()
    {
        return "(S:" + m_nTemporada + " E:" + m_nEpisodio +  ")";
    }

    public int getIDSerie()
    {
        return m_idserie;
    }

    public String getSerie()
    {
        return m_serie;
    }

    public String getFecha()
    {
        return m_fecha;
    }

    public String getResumen()
    {
        return m_resumen;
    }

    public String getImagen()
    {
        return m_imagen;
    }
}

package csesena.entretenimiento.episodecalendar.model;

/**
 * Created by Csesena on 02/03/2015.
 */
public class TVShow {

    int m_id = -1;
    String m_nombre = "";
    String m_imagen = "";
    String m_resumen = "";

    public TVShow(){}

    public TVShow(int idP, String nombreP, String resumenP, String imagenP) {
        m_id = idP;
        m_nombre = nombreP;
        m_resumen = resumenP;
        m_imagen = imagenP;
    }

    public TVShow(String nombreP) {
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

    public String getResumen()
    {
        return m_resumen;
    }

    public String getImagen()
    {
        return m_imagen;
    }
}

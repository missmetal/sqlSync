package utils;

/**
 * Created by mjmartinez on 15/02/2017.
 */

public class Antena {
    private int id;
    private String sector;
    private String estado;
    private String fechaEstado;
    private String tipoInstalacion;
    private String azimut;
    private String modeloAntena;
    private String altura;
    private String tiltMecanico;
    private String tilElectrico;
    private String tiltElectricoRemoto;
    private String observaciones;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(String fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public String getTipoInstalacion() {
        return tipoInstalacion;
    }

    public void setTipoInstalacion(String tipoInstalacion) {
        this.tipoInstalacion = tipoInstalacion;
    }

    public String getAzimut() {
        return azimut;
    }

    public void setAzimut(String azimut) {
        this.azimut = azimut;
    }

    public String getModeloAntena() {
        return modeloAntena;
    }

    public void setModeloAntena(String modeloAntena) {
        this.modeloAntena = modeloAntena;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getTiltMecanico() {
        return tiltMecanico;
    }

    public void setTiltMecanico(String tiltMecanico) {
        this.tiltMecanico = tiltMecanico;
    }

    public String getTilElectrico() {
        return tilElectrico;
    }

    public void setTilElectrico(String tilElectrico) {
        this.tilElectrico = tilElectrico;
    }

    public String getTiltElectricoRemoto() {
        return tiltElectricoRemoto;
    }

    public void setTiltElectricoRemoto(String tiltElectricoRemoto) {
        this.tiltElectricoRemoto = tiltElectricoRemoto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

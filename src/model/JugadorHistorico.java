package model;

public class JugadorHistorico {
    public final String nombre;
    public final int dias;
    public final int dinero;
    public final int envios;
    public final int satisfaccion;
    public final String fechaInicio;
    public final String fechaFin;
    public final String dificultad;
    public final String ciudad;

    public JugadorHistorico(String nombre, int dias, int dinero, int envios, int satisfaccion, 
                          String fechaInicio, String fechaFin, String dificultad, String ciudad) {
        this.nombre = nombre;
        this.dias = dias;
        this.dinero = dinero;
        this.envios = envios;
        this.satisfaccion = satisfaccion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.dificultad = dificultad;
        this.ciudad = ciudad;
    }
} 
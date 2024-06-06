/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FERNANDO
 */
public class Token {

    public TipoToken tipo;
    public String valor; //almacena el valor del Token
    public int linea; //numero de linea
    public int indice; //posición del Token en una linea

    public Token(TipoToken tipo, String valor, int linea, int indice) { //constructor de Token
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
        this.indice = indice;
    }
}

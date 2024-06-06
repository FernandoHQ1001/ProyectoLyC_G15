/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FERNANDO
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Scanner {
    private String codigo;
    private int linea;
    private TipoToken TokenActual;
    private Iterator<Token> Iterador; //interfaz para iterar sobre una colección de objetos Token
    private Token ultimoToken;

    /* Listado de palabras reservadas del lenguaje.
       Set --> almacena las palabras reservadas (colección de elementos únicos).
       HashSet --> Almcaena en una tabla Hash
       Array.asList --> Lista
    */
    private static final Set<String> palabrasReservadas = new HashSet<>(Arrays.asList(
       "verdadero", "si", "finSi", "entero", "sino", "mientras", "finMientras", "real", "escribir", "falso"
    ));

    public Scanner(String codigo) { //Constructor de Scanner
        this.codigo = codigo;
        this.linea = 1; //Inicializa la linea en 1
        this.Iterador = getTokens().iterator(); //Obtiene una colección de Tokens y luego itera sobre esta
        this.ultimoToken = null; //Inicializa el ultimo Token como null
    }
    
    // Obtener el sgte Token
    public Token getToken() {
        while (Iterador.hasNext()) { //Itera sobre los Tokens
            Token actualToken = Iterador.next(); //Proximo Token 
            if (!(actualToken.valor.equals("\n") && (ultimoToken == null || ultimoToken.valor.equals("\n")))) {
                ultimoToken = actualToken;
                return actualToken;
            }
        }
        return new Token(TipoToken.EOF, "EOF", linea, codigo.length());
        //codigo.length() --> indice de la última posición
    }
 
    // Devuelve tokens de a uno.
    private List<Token> getTokens() {
        List<Token> tokens = new ArrayList<>(); //Lista que almacenará los Tokens
        String lexema = "";
        for (int i = 0; i < codigo.length(); i++) { //Recorre el código
            char caractActual = codigo.charAt(i);

            if (caractActual == '\n') {
                if (!lexema.isEmpty()) {
                    tokens.add(Lexema(lexema, i));
                    lexema = "";
                }
                tokens.add(Lexema("\n", i));
                linea++;
            } else if (Character.isWhitespace(caractActual) || ",()=+-*/^<>|&".indexOf(caractActual) != -1) {
                if (!lexema.isEmpty()) {
                    tokens.add(Lexema(lexema, i));
                    lexema = "";
                }
                if (!Character.isWhitespace(caractActual)) {
                    tokens.add(Lexema(String.valueOf(caractActual), i + 1));
                }
            } else if (Character.isLetterOrDigit(caractActual) || caractActual == '.') {
                lexema += caractActual;
            } else {
                tokens.add(Lexema(String.valueOf(caractActual), i + 1));
            }
        }
        if (!lexema.isEmpty()) {
            tokens.add(Lexema(lexema, codigo.length()));
        }
        return tokens;
    }

    // Tipos
    private Token Lexema(String lexema, int indice) {
        if (palabrasReservadas.contains(lexema)) { //Verifica si el lexema es una palabra reservada
            return new Token(TipoToken.PALABRA_RESERVADA, lexema, linea, indice);
        }
        if (lexema.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) { //Verifica si el lexema es un ID
            return new Token(TipoToken.ID, "ID", linea, indice);
        }
        if (lexema.matches("[=+\\-*/^<>|&]")) { //Verifica si el lexema es un operador
            return new Token(TipoToken.OPERADOR, lexema, linea, indice);
        }
        if (lexema.matches("^[0-9]+(\\.[0-9]+)?$")) { //Verifica si el lexema es un numero
            return new Token(TipoToken.NUM, "NUM", linea, indice);
        }
        if (lexema.matches("[\n,()]")) { //Verifica si el lexema es un simbolo
            return new Token(TipoToken.SIMBOLO, lexema, linea, indice);
        }
        Token tokenDesconocido = new Token(TipoToken.DESCONOCIDO, lexema, linea, indice); //Si no coincide con ninguno es desconocidos
        error("Error ", tokenDesconocido);
        return tokenDesconocido;
    }

    // Muestra errores
    private void error(String ErrorTipo, Token token) {
        System.out.println(ErrorTipo + " en linea " + token.linea + ", columna " + token.indice + ": " + token.valor);
    }
    
    //Lee archivo
    private static String leerArchivo(String archivo) {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contenido.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contenido.toString();
    }

    // Método principal 
    public static void main(String[] args) {
        String codigo = leerArchivo("archivo.txt");
        Scanner scanner = new Scanner(codigo);

        Token token;
        while ((token = scanner.getToken()).tipo != TipoToken.EOF) {
            System.out.println("Token: " + token.tipo + ", Valor: " + token.valor);
        }
    }

}

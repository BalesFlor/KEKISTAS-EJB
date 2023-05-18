package com.grsc.logica.validador;

public class validador {
    
    public static boolean validarCi(String ci) {
        //Validamos formato de cedula 1.111.111-1 11

        boolean valido = false;

        if (ci.length() == 11) {
            char resultado = ci.charAt(1);
            char punto = '.';

            if (resultado == punto) {

                char resultado2 = ci.charAt(5);
                char punto2 = '.';
                if (resultado2 == punto2) {

                    char resultado3 = ci.charAt(9);
                    char guion = '-';
                    if (resultado3 == guion) {
                        valido = true;

                    }
                }
            }
        }
        //documento.length() < 11 && (!documento.contains(".") || !documento.contains("-")
        return valido;
    }
    //String mail

    public static boolean validarLargoCampos(String nombre1, String apellido1,
            String apellido2, String contrasenia) {
        //Validamos formato de largo de campos

        boolean valido = false;

        if (nombre1.length() > 3 && nombre1.length() < 30) {
            if (apellido1.length() > 3 && apellido1.length() < 30) {
                if (apellido2.length() > 3 && apellido2.length() < 30) {
                    if (contrasenia.length() > 3 && contrasenia.length() < 50) {
                        valido = true;
                    }
                }
            }
        }
        //documento.length() < 11 && (!documento.contains(".") || !documento.contains("-")
        return valido;
    }

    public static boolean validarTelefono(String telefono) {
        //ValidamosString telefono +598 99 977 583
        boolean valido = false;

        int suma = 0;
        char[] numeros = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        telefono = telefono.replaceAll("\\s+", "");

        if (telefono.length() == 12) {
            char posCero = telefono.charAt(0);
            char posUno = telefono.charAt(1);
            char posDos = telefono.charAt(2);
            char posTres = telefono.charAt(3);

            char mas = '+';
            char cinco = '5';
            char nueve = '9';
            char ocho = '8';

            if (posCero == mas && posUno == cinco && posDos == nueve && posTres == ocho) {

                for (int i = 4; i < telefono.length(); i++) {
                    char num = telefono.charAt(i);
                    for (int j = 0; j < numeros.length; j++) {
                        if (numeros[j] == num) {
                            suma = suma + 1;
                        }
                    }
                }
                if (suma == 8) {
                    valido = true;
                }
            }
        }
        return valido;
    }

    public static boolean validarMailConsti(String mail) {
        boolean validar = false;
        mail= mail.toLowerCase();
        
        if (mail.contains("@estudiantes.utec.edu.uy")) {
            validar = true;
        } else if (mail.contains("@utec.edu.uy")) {
            validar = true;
        }

        return validar;
    }
    
    public static boolean validarMailPers(String mail) {
        boolean validar = false;
        mail= mail.toLowerCase();
        
        if (mail.contains("@gmail.com")) {
            validar = true;
        }else if (mail.contains("@hotmail.com")) {
            validar = true;
        }else if (mail.contains("@outlook.com")) {
            validar = true;
        }
        return validar;
    }

    public static boolean validarContrasenia(String contrasenia1, String contrasenia2){
        boolean validar=false;
        
        if(contrasenia1.equals(contrasenia2)){
            validar=true;
        }
        
        return validar;
    }
}

package br.com.controlechuvas.util;

import java.util.regex.Pattern;

public final class ValidadorCpf {

    private static final Pattern NAO_DIGITO = Pattern.compile("\\D");
    private static final int TAMANHO_CPF = 11;
    private static final int MODULO = 11;
    private static final int LIMITE_RESTO = 2;

    private ValidadorCpf() {
    }

    public static boolean ehValido(String cpf) {
        if (cpf == null) {
            return false;
        }
        String digitos = NAO_DIGITO.matcher(cpf).replaceAll("");
        if (digitos.length() != TAMANHO_CPF || possuiTodosOsDigitosIguais(digitos)) {
            return false;
        }
        return possuiDigitosVerificadoresValidos(digitos);
    }

    private static boolean possuiTodosOsDigitosIguais(String digitos) {
        return digitos.chars().distinct().count() == 1;
    }

    private static boolean possuiDigitosVerificadoresValidos(String digitos) {
        int primeiroDigito = calcularDigitoVerificador(digitos, TAMANHO_CPF - 2);
        int segundoDigito = calcularDigitoVerificador(digitos, TAMANHO_CPF - 1);
        return primeiroDigito == valorNumerico(digitos, TAMANHO_CPF - 2)
                && segundoDigito == valorNumerico(digitos, TAMANHO_CPF - 1);
    }

    private static int calcularDigitoVerificador(String digitos, int quantidade) {
        int soma = 0;
        for (int posicao = 0; posicao < quantidade; posicao++) {
            soma += valorNumerico(digitos, posicao) * (quantidade + 1 - posicao);
        }
        int resto = soma % MODULO;
        return resto < LIMITE_RESTO ? 0 : MODULO - resto;
    }

    private static int valorNumerico(String digitos, int posicao) {
        return Character.getNumericValue(digitos.charAt(posicao));
    }
}

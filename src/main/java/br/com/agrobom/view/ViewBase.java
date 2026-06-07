package br.com.agrobom.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ViewBase {
	// Scanner compartilhado entre todas as Views via herança
    protected static final Scanner scanner = new Scanner(System.in);
    protected static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

    protected double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Digite um valor válido: ");
            }
        }
    }

    protected LocalDate lerData() {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine().trim(), fmt);
            } catch (DateTimeParseException e) {
                System.out.print("Data inválida. Use dd/MM/yyyy: ");
            }
        }
    }
}

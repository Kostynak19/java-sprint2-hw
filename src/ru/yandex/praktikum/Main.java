package ru.yandex.praktikum;

import ru.yandex.praktikum.month.Month;
import ru.yandex.praktikum.report.MonthlyReport;
import ru.yandex.praktikum.report.YearlyReport;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MonthlyReport monthlyReport = new MonthlyReport();
        YearlyReport yearlyReport = new YearlyReport();

        boolean exitIsProgram = false;
        while (true) {
            displayHello();

            while (!scanner.hasNextInt()) {
                System.err.println("Введите доступную команду!");
                scanner.nextLine();
            }

            if (exitIsProgram)
                break;

            int command = scanner.nextInt();
            switch (command) {

                case 0:
                    System.out.println("Вы вышли из приложения!");
                    exitIsProgram = true;
                    break;
                case 1:
                    for (int i = 0; i < Month.values().length; i++) {
                        if (monthlyReport.getCount() < Month.values().length) {
                            monthlyReport.readReport("m.20210" + (i + 1) + ".csv");
                            monthlyReport.clearListsMonthlyReport();
                        } else {
                            System.err.println("Данные считаны! Пожалуйста введите другую команду!");
                            break;
                        }
                    }
                    break;
                case 2:
                    if (yearlyReport.getCount() == 0) {
                        yearlyReport.readReport("y.2021.csv");
                    } else {
                        System.err.println("Данные считаны! Пожалуйста введите другую команду!");
                    }
                    break;
                case 3:
                    yearlyReport.dataReconciliation(monthlyReport);
                    break;
                case 4:
                    monthlyReport.printReport();
                    break;
                case 5:
                    yearlyReport.printReport();
                    break;
            }
        }
    }

    public static void displayHello() {
        System.out.println("Введите команду:");
        System.out.println("[0] - Выход");
        System.out.println("[1] - Считать все месячные отчеты");
        System.out.println("[2] - Считать годовой отчет");
        System.out.println("[3] - Сверить отчеты");
        System.out.println("[4] - Вывести на экран все месячные отчеты");
        System.out.println("[5] - Вывести на экран годовой отчет");
    }
}

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

        while (true) {
            displayHello();
            while (!scanner.hasNextInt()) {
                System.err.println("Введите доступную команду!");
                scanner.nextLine();
            }

            int command = scanner.nextInt();
            if (command == 0) {
                System.out.println("Вы вышли из приложения!");
                break;
            } else if (command == 1) {
                for (int i = 0; i < Month.values().length; i++) {
                    if (monthlyReport.getCount() < Month.values().length) {
                        monthlyReport.readReport("m.20210" + (i + 1) + ".csv");
                         monthlyReport.clearListsMonthlyReport();
                    } else {
                        System.err.println("Данные считаны! Пожалуйста введите другую команду!");
                        break;
                    }
                }
            } else if (command == 2) {
                if (yearlyReport.getCount() == 0) {
                    yearlyReport.readReport("y.2021.csv");
                } else {
                    System.err.println("Данные считаны! Пожалуйста введите другую команду!");
                }
            } else if (command == 3) {
                Map<Month, List<Integer>> map = monthlyReport.getMap();
                int index = 0;
                if (monthlyReport.getCount() != 0 && yearlyReport.getCount() != 0) {
                    for (Map.Entry<Month, List<Integer>> pair : map.entrySet()) {
                        List<String> listMonth = yearlyReport.getMonth();
                        for (int i = 0; i < listMonth.size(); i++) {
                            if (i < 2) {
                                List<Integer> list = map.get(pair.getKey());
                                int resultY, resultM;
                                boolean isFlag = yearlyReport.isExpenses(index);
                                if (listMonth.size() % 2 != 0) {
                                    if (index < listMonth.size() - 1) {
                                        boolean isEqualMonth = listMonth.get(index).equals(listMonth.get(index + 1));
                                        resultY = !isFlag ? yearlyReport.expensesProfit(index)
                                                : yearlyReport.expensesLoss(index);
                                        resultM = !isFlag ? list.get(0) : list.get(1);
                                        if (isEqualMonth) {
                                            assertData(resultY, resultM, pair);
                                            index++;
                                        } else {
                                            index++;
                                            assertData(resultY, resultM, pair);
                                            if (i % 2 == 0) {
                                                resultY = 0;
                                                resultM = isFlag ? list.get(0) : list.get(1);
                                                i++;
                                                assertData(resultY, resultM, pair);
                                            }
                                        }
                                    } else {
                                        resultY = !isFlag ? yearlyReport.expensesProfit(index)
                                                : yearlyReport.expensesLoss(index);
                                        resultM = !isFlag ? list.get(0) : list.get(1);
                                        assertData(resultY, resultM, pair);
                                        if (i % 2 == 0) {
                                            resultY = 0;
                                            resultM = isFlag ? list.get(0) : list.get(1);
                                            i++;
                                            assertData(resultY, resultM, pair);
                                        }
                                    }
                                } else {
                                    resultY = !isFlag ? yearlyReport.expensesProfit(index)
                                            : yearlyReport.expensesLoss(index);
                                    resultM = !isFlag ? list.get(0) : list.get(1);
                                    assertData(resultY, resultM, pair);
                                    index++;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                } else {
                    System.err.printf("Сначала вызовите команды:%n[1] - Считать все месячные отчеты%n"
                            + "[2] - Считать годовой отчет%n");
                }

            } else if (command == 4) {
                monthlyReport.printReport();
            } else if (command == 5) {
                yearlyReport.printReport();
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

    public static void assertData(int resultY, int resultM, Map.Entry<Month, List<Integer>> pair) {
        if (resultY == resultM) {
            System.out.printf("%s: Сверка данных выполнена!%n", pair.getKey());
        } else {
            System.err.printf("%s: %d != %d%n", pair.getKey(), resultY, resultM);
        }
    }
}

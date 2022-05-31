package ru.yandex.praktikum.report;

import lombok.Getter;
import ru.yandex.praktikum.month.Month;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Getter
public class MonthlyReport implements IReport {
    public static final String PATH_TO_REPORT = "resources/";
    public final Map<Month, List<Integer>> map;
    private final List<String> listReport;
    private final List<String> itemName;
    private final List<String> isExpense;
    private final List<String> quantity;
    private final List<String> sumOfOne;
    private String month;
    private String nameExpense;
    private int count;

    public MonthlyReport() {
        this.map = new LinkedHashMap<>();
        this.listReport = new ArrayList<>();
        this.itemName = new ArrayList<>();
        this.isExpense = new ArrayList<>();
        this.quantity = new ArrayList<>();
        this.sumOfOne = new ArrayList<>();
    }

    public void readReport(String name) {
        try {
            Path path = Paths.get(PATH_TO_REPORT + name);
            findMonth();
            String report = Files.readString(path);
            separatedReport(report);
            count++;
        } catch (IOException ex) {
            System.err.printf("Class exception: %s - Message exception: %s%n", ex.getClass(), ex.getMessage());
        }
    }

    private void separatedReport(String report) {
        String[] lines = report.split("\\n");
        for (int i = 1; i < lines.length; i++) {
            String[] lineContent = lines[i].split(",");
            itemName.add(i - 1, lineContent[0]);
            isExpense.add(i - 1, lineContent[1]);
            quantity.add(i - 1, lineContent[2]);
            sumOfOne.add(i - 1, lineContent[3]);
        }
        saveReport();
        addSumExpensesToMap(Month.valueOf(month));
        System.out.println("Данные за " + month + " считаны и успешно сохранены!");
    }

    public void clearListsMonthlyReport() {
        if (!itemName.isEmpty() && !isExpense.isEmpty() && !quantity.isEmpty() && !sumOfOne.isEmpty()) {
            itemName.clear();
            isExpense.clear();
            quantity.clear();
            sumOfOne.clear();
        }
    }

    public boolean isExpenses(int index) {
        return Boolean.parseBoolean(isExpense.get(index));
    }

    private int maxExpensesProfit() {
        int max = 0;
        for (int i = 0; i < isExpense.size(); i++) {
            int tmp = Integer.parseInt(quantity.get(i)) * Integer.parseInt(sumOfOne.get(i));
            if (!isExpenses(i)) {
                if (max < tmp) {
                    max = tmp;
                    this.nameExpense = itemName.get(i);
                }
            }
        }
        return max;
    }

    private int maxExpensesLoss() {
        int max = 0;
        for (int i = 0; i < isExpense.size(); i++) {
            int tmp = Integer.parseInt(quantity.get(i)) * Integer.parseInt(sumOfOne.get(i));
            if (isExpenses(i)) {
                if (max < tmp) {
                    max = tmp;
                    this.nameExpense = itemName.get(i);
                }
            }
        }
        return max;
    }

    private int sumExpensesLoss() {
        int sum = 0;
        for (int i = 0; i < isExpense.size(); i++) {
            int tmp = Integer.parseInt(quantity.get(i)) * Integer.parseInt(sumOfOne.get(i));
            if (isExpenses(i)) {
                sum += tmp;
            }
        }
        return sum;
    }

    private int sumExpensesProfit() {
        int sum = 0;
        for (int i = 0; i < isExpense.size(); i++) {
            int tmp = Integer.parseInt(quantity.get(i)) * Integer.parseInt(sumOfOne.get(i));
            if (!isExpenses(i)) {
                sum += tmp;
            }
        }
        return sum;
    }

    private void findMonth() {
        Month[] arrayMonth = Month.values();
        for (int i = 0; i < arrayMonth.length; i++) {
            if (i == getCount()) {
                this.month = arrayMonth[i].name();
            }
        }
    }

      @Override
    public void printReport() {
        if (!listReport.isEmpty()) {
            listReport.forEach(System.out::print);
            month = null;
            nameExpense = null;
            count = 0;
            listReport.clear();
        } else {
            System.err.println("Сначала вызовите команду [1] - Считать все месячные отчеты");
        }
    }

    private void saveReport() {
        int maxExpensesProfit = maxExpensesProfit();
        addListReport(maxExpensesProfit, 31, "Profit");

        int maxExpensesLoss = maxExpensesLoss();
        addListReport(maxExpensesLoss, 33, "Loss");
    }

    private void addListReport(int maxExpenses, int offsetFormattingString, String expenses) {
        String result = String.format("Month: %-8s | Name Expenses %s: %" + -offsetFormattingString + "s | Maximum Expenses: %d%n",
                month,
                expenses,
                nameExpense,
                maxExpenses);

        listReport.add(result);
    }

    private void addSumExpensesToMap(Month month) {
        map.put(month, List.of(sumExpensesProfit(), sumExpensesLoss()));
    }
}

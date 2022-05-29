package ru.yandex.praktikum.report;


import lombok.Getter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
public class YearlyReport implements IReport {
    private final List<String> listReport;
    private final List<String> month;
    private final List<String> amount;
    private final List<String> isExpense;
    private String year;
    private String nameMonth;
    private int count;

    public YearlyReport() {
        this.month = new ArrayList<>();
        this.amount = new ArrayList<>();
        this.isExpense = new ArrayList<>();
        this.listReport = new ArrayList<>();
    }

    public void readReport(String name) {
        try {
            Path path = Paths.get(MonthlyReport.PATH_TO_REPORT + name);
            setYear(name);
            String report = Files.readString(path);
            separatedReport(report);
            count++;
        } catch (IOException ex) {
            System.err.printf("Class exception: %s - Message exception: %s%n", ex.getClass(), ex.getMessage());
        }
    }

    private void separatedReport(String report) {
        String[] lines = report.split("\r\\n");
        for (int i = 1; i < lines.length; i++) {
            String[] lineContent = lines[i].split(",");
            month.add(i - 1, lineContent[0]);
            amount.add(i - 1, lineContent[1]);
            isExpense.add(i - 1, lineContent[2]);
        }
        saveReport();
        System.out.println("Данные за " + year + " год считаны и успешно сохранены!");
    }

    public void clearListsYearlyReport() {
        if (!month.isEmpty() && !amount.isEmpty() && !isExpense.isEmpty()) {
            month.clear();
            amount.clear();
            isExpense.clear();
        }
    }

    public boolean isExpenses(int index) {
        return Boolean.parseBoolean(isExpense.get(index));
    }

    public int expensesLoss(int index) {
        int result = 0;
        if (isExpenses(index)) {
            result = Integer.parseInt(amount.get(index));
            this.nameMonth = month.get(index);
        }
        return result;
    }

    private double averageExpensesLoss() {
        int count = 0;
        double sum = 0;
        for (int i = 0; i < isExpense.size(); i++) {
            int tmp = Integer.parseInt(amount.get(i));
            if (isExpenses(i)) {
                sum += tmp;
                count++;
            }
        }
        return (sum / count);
    }

    public int expensesProfit(int index) {
        int result = 0;
        if (!isExpenses(index)) {
            result = Integer.parseInt(amount.get(index));
            this.nameMonth = month.get(index);
        }
        return result;
    }

    private double averageExpensesProfit() {
        int count = 0;
        double sum = 0;
        for (int i = 0; i < isExpense.size(); i++) {
            int tmp = Integer.parseInt(amount.get(i));
            if (!isExpenses(i)) {
                sum += tmp;
                count++;
            }
        }
        return (sum / count);
    }

    private void setYear(String name) {
        this.year = name.substring(2, 6);
    }

    @Override
    public void printReport() {
        if (!listReport.isEmpty()) {
            listReport.forEach(System.out::print);
            year = null;
            nameMonth = null;
            count = 0;
            listReport.clear();
            clearListsYearlyReport();
        } else {
            System.err.println("Сначала вызовите команду [2] - Считать годовой отчет");
        }
    }

    private void saveReport() {
        for (int i = 0; i < month.size(); i++) {
            boolean isFlag = isExpenses(i);
            if (i < month.size() - 1) {
                boolean isEqualMonth = month.get(i).equals(month.get(i + 1));
                if (isEqualMonth) {
                    int profit = !isFlag ? expensesProfit(i) - expensesLoss(i + 1) : expensesProfit(i + 1) - expensesLoss(i);
                    addListReport(profit);
                    i++;
                } else {
                    int profit = !isFlag ? expensesProfit(i) : -expensesLoss(i);
                    addListReport(profit);
                }
            } else {
                int profit = !isFlag ? expensesProfit(i) : -expensesLoss(i);
                addListReport(profit);
            }
        }
    }

    private void addListReport(int profit) {
        String result = String.format("Year: %s | Month: %s | Profit: %-8d | Average Profit Year: %f | Average Loss Year: %f%n",
                year,
                nameMonth,
                profit,
                averageExpensesProfit(),
                averageExpensesLoss());

        listReport.add(result);
    }
}

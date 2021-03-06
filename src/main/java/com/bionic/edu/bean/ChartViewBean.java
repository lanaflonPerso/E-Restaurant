package com.bionic.edu.bean;

import com.bionic.edu.util.ReportCategory;
import com.bionic.edu.util.ReportDish;
import com.bionic.edu.util.ReportTotal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.chart.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Named
@Scope("session")
public class ChartViewBean implements Serializable {
    private static final long serialVersionUID = 7288379413976890050L;

    private PieChartModel pieModel;
    private LineChartModel lineModel;
    private HorizontalBarChartModel hBarModel;
    @Inject
    private ReportBean reportBean;
    @Inject
    private EmployeeBean employeeBean;

    private static final Logger logger = LogManager.getLogger(ChartViewBean.class);

    @PostConstruct
    public void init() {
        createModels();
    }

    private void createModels() {
        createPieModel();
        createLineModel();
        createHBarModel();
    }

    public PieChartModel getPieModel() {
        createPieModel();
        return pieModel;
    }

    public LineChartModel getLineModel() {
        createLineModel();
        return lineModel;
    }

    public HorizontalBarChartModel getHBarModel() {
        createHBarModel();
        return hBarModel;
    }

    private void createPieModel() {
        pieModel = new PieChartModel();
        reportBean.refreshCategoryReport();
        for (ReportCategory category : reportBean.getReportCategories()) {
            pieModel.set(category.getDishCategoryName(), category.getTotal());
        }

        pieModel.setTitle("Categories diagram");
        pieModel.setLegendPosition("e");
        pieModel.setFill(false);
        pieModel.setShowDataLabels(true);
        pieModel.setDiameter(150);
        logger.warn("Categories diagram was requested by employee " + employeeBean.getEmployee().getName() + " with ID:" + employeeBean.getEmployee().getId() + " and role as " + employeeBean.getEmployee().getRole().getName() + ".");
    }

    private void createLineModel() {
        lineModel = new LineChartModel();
        LineChartSeries series = new LineChartSeries();
        reportBean.refreshTotalReport();
        List<ReportTotal> reportTotals = reportBean.getReportTotals();
        for (ReportTotal reportTotal : reportTotals) {
            series.set(reportTotal.getDate().toString(), reportTotal.getTotal());
        }

        lineModel.addSeries(series);
        lineModel.setTitle("Daily Orders diagram");
        lineModel.setAnimate(true);
        lineModel.getAxis(AxisType.Y).setLabel("Income, USD");

        DateAxis axis = new DateAxis("Dates");
        axis.setTickFormat("%b %#d, %y");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        axis.setMax(df.format(reportBean.getEndDate().getTime() + TimeUnit.DAYS.toMillis(1)));
        axis.setMin(df.format(reportBean.getStartDate().getTime() - TimeUnit.DAYS.toMillis(1)));
        lineModel.getAxes().put(AxisType.X, axis);
        logger.warn("Daily Orders diagram was requested by employee " + employeeBean.getEmployee().getName() + " with ID:" + employeeBean.getEmployee().getId() + " and role as " + employeeBean.getEmployee().getRole().getName() + ".");
    }

    private void createHBarModel() {
        hBarModel = new HorizontalBarChartModel();
        reportBean.refreshDishesReport();
        Collections.sort(reportBean.getReportDishes());
        ChartSeries series = new ChartSeries();
        series.setLabel("Dishes");
        List<ReportDish> reportDishes = reportBean.getReportDishes();
        for (ReportDish reportDish : reportDishes) {
            series.set(reportDish.getDishName(), reportDish.getTotal());
        }

        hBarModel.addSeries(series);
        hBarModel.setTitle("Dishes diagram");
        hBarModel.setStacked(true);

        Axis xAxis = hBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Income, USD");
        Axis yAxis = hBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Dish");
        logger.warn("Dishes diagram was requested by employee " + employeeBean.getEmployee().getName() + " with ID:" + employeeBean.getEmployee().getId() + " and role as " + employeeBean.getEmployee().getRole().getName() + ".");
    }
}

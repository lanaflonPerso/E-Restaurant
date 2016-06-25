package com.bionic.edu.bean;

import com.bionic.edu.util.ReportCategory;
import com.bionic.edu.util.ReportTotal;
import org.primefaces.model.chart.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Named
@Scope("session")
public class ChartViewBean implements Serializable {
    private static final long serialVersionUID = 7288379413976890050L;

    private PieChartModel pieModel;
    private LineChartModel lineModel;
    @Inject
    private ReportBean reportBean;

    @PostConstruct
    public void init() {
        createModels();
    }

    private void createModels() {
        createPieModel();
        createLineModel();
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public LineChartModel getLineModel() {
        return lineModel;
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
    }

    // TODO: 6/23/16 - update charts - see http://stackoverflow.com/questions/12929125/primefaces-charts-not-updating-when-submit-a-new-request
    private void createLineModel() {
        lineModel = new LineChartModel();
        LineChartSeries series = new LineChartSeries();
        reportBean.refreshTotalReport();
        List<ReportTotal> reportTotals = reportBean.getReportTotals();
        for (ReportTotal reportTotal : reportTotals) {
            series.set(reportTotal.getDate().toString(), reportTotal.getTotal());
        }
        lineModel.addSeries(series);
        lineModel.setTitle("Order's Total diagram");
        lineModel.setAnimate(true);
        lineModel.getAxis(AxisType.Y).setLabel("Income, USD");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickFormat("%b %#d, %y");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        axis.setMax(df.format(reportBean.getEndDate()));
        axis.setMin(df.format(reportBean.getStartDate()));
        lineModel.getAxes().put(AxisType.X, axis);
    }
}

/**************************
 * Project: ePoverty
 * Filename: BarGraph.java
 * Description: A customizable, resizable bar graph
 * Name: Bunna Veth
 * Date: Mar 29, 2012
 **************************/
package epoverty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BarGraph extends JPanel
{
    //Fonts and Colors
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 12);
    private final Color BACKGROUND_COLOR = Color.BLACK;
    private final Color BAR_COLOR = new Color(255, 255, 255, 200); //transparency allows grid to bleed through
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color GRID_COLOR = Color.GRAY;

    //Default Panel Size (used when no size is specified)
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 300;

    //Margins (spacing around the chart area)
    private final int TOP_MARGIN = 30;
    private final int LEFT_MARGIN = 50;
    private final int BOTTOM_MARGIN = 30;
    private final int RIGHT_MARGIN = 10;
    private final int GRIDLINES = 5; //includes the x-axis

    //Data Fields
    private String title;          //chart title
    private Object[] xLabels;      //labels along the x-axis
    private String[] yLabels;      //labels along the y-axis
    private Object[] dataPoints;   //actual data-points
    private double[] normalValues; //normalized data-points (between 0.0 - 1.0)

    //Dimension Fields
    private int panelWidth;     //component size
    private int panelHeight;
    private double chartWidth;  //area the bars are drawn in
    private double chartHeight;
    private double cellWidth;   //a cell is the general area where each bar is drawn
    private double barWidth;    //width of each bar (a percentage of the cell width)
    private double gridSpacing; //spacing between horizontal grid-lines

    //Constructor (default size) *******************************************************
    public BarGraph(String title, Object[] xData, Object[] yData)
    {
        this(title, xData, yData, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    //Constructor (specified size)
    //Object[] types provide the flexibility to handle Strings, doubles, ints, or any other numeric types.
    //xData = preferably Strings
    //yData = preferably doubles
    public BarGraph(String title, Object[] xData, Object[] yData, int width, int height)
    {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        this.title = title;
        this.xLabels = xData;
        this.dataPoints = yData;

        normalizeData();
    }

    //Compute widths based on the panel size
    private void computeWidths(int width, int height)
    {
        panelWidth = width;
        panelHeight = height;

        chartWidth = width - LEFT_MARGIN - RIGHT_MARGIN;
        chartHeight = height - TOP_MARGIN - BOTTOM_MARGIN;

        cellWidth = chartWidth / dataPoints.length;
        barWidth = 0.6 * cellWidth;
        gridSpacing = chartHeight / (GRIDLINES - 1);
    }

    //Normalize data so it can fit inside the panel
    private void normalizeData()
    {
        //find largest data-point
        double maxValue = Double.parseDouble(dataPoints[0].toString());
        for (Object y : dataPoints)
        {
            double value = Double.parseDouble(y.toString());
            if (value > maxValue)
                maxValue = value;
        }

        //round up to nearest 100 or 1000 (used to get clean divisions)
        if (maxValue < 2000)
            maxValue = 100 * Math.ceil(maxValue / 100);
        else
            maxValue = 1000 * Math.ceil(maxValue / 1000);

        //represent every point as a percentage of the maximum value
        normalValues = new double[dataPoints.length];
        for (int i = 0; i < dataPoints.length; i++)
            normalValues[i] = Double.parseDouble(dataPoints[i].toString()) / maxValue;

        //y-labels (top-to-bottom)
        yLabels = new String[GRIDLINES];
        for (int i = 0; i < GRIDLINES; i++)
        {
            yLabels[i] = String.format("%.0f", (GRIDLINES - i - 1.0) / (GRIDLINES - 1.0) * maxValue);
        }
    }

    //Draw Bar Graph *****************************************************************
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        computeWidths(getWidth(), getHeight()); //dynamically sizes graph if window is resized
        FontMetrics fontMeter; //measures font dimensions

        //title
        g.setColor(TEXT_COLOR);
        g.setFont(TITLE_FONT);
        fontMeter = g.getFontMetrics();
        String titleLabel = title;
        int titleX = (int) (panelWidth/2.0 - fontMeter.stringWidth(titleLabel)/2.0); //centers text
        int titleY = (int) fontMeter.getHeight();
        g.drawString(titleLabel, titleX, titleY);

        //y-axis
        g.setFont(TEXT_FONT);
        fontMeter = g.getFontMetrics();
        int gridX1 = LEFT_MARGIN;
        int gridX2 = (int) (LEFT_MARGIN + chartWidth);

        for (int i = 0; i < GRIDLINES; i++)
        {
            //grid
            g.setColor(GRID_COLOR);
            int gridY = (int) (i * gridSpacing) + TOP_MARGIN;
            g.drawLine(gridX1, gridY, gridX2, gridY);

            //label
            g.setColor(TEXT_COLOR);
            String label = yLabels[i];
            int labelX = LEFT_MARGIN - fontMeter.stringWidth(label) - 5; //right-aligns text
            int labelY = gridY + 5;
            g.drawString(label, labelX, labelY);
        }

        //x-axis
        double barOffset = cellWidth/2.0 - barWidth/2.0 + LEFT_MARGIN; //centers bar within cell

        for (int i = 0; i < dataPoints.length; i++)
        {
            //bar
            g.setColor(BAR_COLOR);
            int barX = (int) (i * cellWidth + barOffset);
            int barY = (int) (chartHeight - chartHeight * normalValues[i]) + TOP_MARGIN;
            int barHeight = (int) (chartHeight - barY) + TOP_MARGIN;
            g.fillRect(barX, barY, (int) barWidth, barHeight);

            //label
            g.setColor(TEXT_COLOR);
            String label = xLabels[i].toString();
            if(fontMeter.stringWidth(label) > cellWidth)
                label = label.substring(0, 1); //only display the first character if view is too small

            int labelX = (int) (i * cellWidth + cellWidth/2.0 - fontMeter.stringWidth(label)/2.0) + LEFT_MARGIN; //centers text
            int labelY = (int) (TOP_MARGIN + chartHeight) + 15;

            g.drawString(label, labelX, labelY);
        }
    }

    //Test Method (delete before release) *************************************************
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Bar Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Example Usage
        Object[] x = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Object[] y = {110, 210, 230, 100, 150, 120, 200, 250, 340, 200, 300, 350};
        BarGraph graph = new BarGraph("Monthly Raise Amount", x, y, 500, 300);

        frame.add(graph);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//end main

}//end class
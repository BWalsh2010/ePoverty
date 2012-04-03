package epoverty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AccountsSummary extends JPanel
{
   AccountsSummary(JTabbedPane tabs)
   {
      BackgroundPanel accountsSummaryTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      Dimension access = new Dimension(super.getWidth(), super.getHeight()-15);
      accountsSummaryTab.setPreferredSize(access);
      GradientPaint paintAccess = new GradientPaint(0, 0, Color.GREEN, 100, 100, Color.BLACK);
      accountsSummaryTab.setPaint(paintAccess);
      
      tabs.add("", accountsSummaryTab);
   }   

}
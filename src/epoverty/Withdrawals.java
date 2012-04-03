package epoverty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Withdrawals extends JPanel
{
   Withdrawals(JTabbedPane tabs)
   {
      BackgroundPanel transactionsTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      Dimension access = new Dimension(super.getWidth(), super.getHeight()-15);
      transactionsTab.setPreferredSize(access);
      GradientPaint paintAccess = new GradientPaint(0, 0, Color.MAGENTA, 100, 100, Color.BLACK);
      transactionsTab.setPaint(paintAccess);
      
      tabs.add("", transactionsTab);
   }   

}
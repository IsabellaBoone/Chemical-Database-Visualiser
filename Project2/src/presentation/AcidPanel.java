package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import model.Acid;
import model.AcidDataMapper;
import model.DomainModelException;
import model.Acid;

public class AcidPanel extends JPanel{

  JScrollPane acids = new JScrollPane(); 
  GridBagConstraints gbc = new GridBagConstraints(); 
  JButton addButton = new JButton("Add");
  JButton deleteButton = new JButton("Delete");
  JButton filterButton = new JButton("Filter");
  JButton detailsButton = new JButton("Details");
  JLabel selected = null;
  Acid selectedAcid = null;
  Color labelColor = new Color(30,30,30);
  List<Acid> acidList = new ArrayList<Acid>();
  String filter;
  
  public AcidPanel() {
      this.setLayout(new GridBagLayout());
      addScrollPane();
      setButtons();
  }
  
  private JLabel Labels() {
      JLabel label = new JLabel();
      label.setBackground(Color.WHITE);
      label.setOpaque(true);
      label.setPreferredSize(new Dimension(100,20));
      return label;
  }
  
  private void addScrollPane() {
      acids.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      acids.add(acids.createVerticalScrollBar());
      
      acids.setViewportView(buildLabels());
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1;
      gbc.weighty = Integer.MAX_VALUE;
      gbc.fill = GridBagConstraints.BOTH;
      add(acids,gbc);
  }
  
  private void setButtons() {
      addButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
              addAcid();
            }
          });
      deleteButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
              deleteAcid();
            }
          });
      filterButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
              filterAcid();
            }
          });
      detailsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
              getDetailsAcid();
            }
          });
      JPanel buttons = new JPanel(new GridBagLayout());
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 0;
      gbc.gridx = 0;
      gbc.gridy = 0;
      buttons.add(addButton, gbc);
      
      gbc.gridx = 1;
      buttons.add(deleteButton, gbc);
      
      gbc.gridx = 2;
      buttons.add(filterButton, gbc);
      
      gbc.gridx = 3;
      buttons.add(detailsButton, gbc);
      
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.anchor = GridBagConstraints.SOUTHWEST;
      gbc.weighty = 1;
      buttons.setBackground(Color.GRAY);
      
      add(buttons, gbc);

  }
  
  private void addAcid() {
      new AddAcidFrame().addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent arg0) {
              acids.setViewportView(buildLabels());
          }
      });
  }
  
  private void deleteAcid() {
      if(selected != null) {
          //deletes selected acid
      }
  }
  
  private void filterAcid() {
          //brings up new window acidd on selected acid
      FilterAcidFrame fbf = new FilterAcidFrame();
          fbf.addWindowListener(new WindowAdapter() {
              @Override
              public void windowClosed(WindowEvent arg0) {
                  filter = fbf.getFilter();
                  System.out.println(filter);
              }
          });
  }
  
  private void getDetailsAcid() {
      if(selected == null) {
          return;
      }
  }
  
  private void removeSelectedBackground() {
      if(selected != null)
        selected.setBackground(labelColor);
    }
  
  private JPanel buildLabels() {
      JPanel labels = new JPanel();
  
      try {
        acidList = new AcidDataMapper().getAll();
      } catch (DomainModelException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      labels.setLayout(new GridLayout(acidList.size(), 1));
      
      for(int i = 0; i < acidList.size(); i++) {
            final int x = i;
            JLabel label = new JLabel(buildHtml(acidList.get(i)));
            label.setOpaque(true);
            label.setBackground(new Color(30, 30, 30));
            label.addMouseListener( new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    removeSelectedBackground();
                    label.setBackground(new Color(234, 201, 55));
                    selected = label;
                    selectedAcid = acidList.get(x);
                }
            }); 
            labels.add(label, i, 0);
          }

      return labels;
  }
  
  private String buildHtml(Acid acid) {
      return "<html><p style=\"color:white;\">" + acid.getName() + "</p></html>";
  }

}

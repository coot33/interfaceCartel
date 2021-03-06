/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacecartel;

import databaseConnection.CustomSQLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Al
 */
public class HistoriquePrixPanel extends JPanel {

    final ButtonGroup groupTime = new ButtonGroup();

    final ButtonGroup groupPrice = new ButtonGroup();
    
    private JButton year;
    private JButton asc;
    private JButton dsc;
    /* JToggleButton hour = new JToggleButton("Hour");
    JToggleButton min = new JToggleButton("Min");
    JToggleButton sec = new JToggleButton("sec");*/
    private JToggleButton average;
    private JToggleButton last;
    private JToggleButton first;
    private JTable tbl;
    private JScrollPane tblContainer;
    private CustomSQLConnection msq;

    public HistoriquePrixPanel(CustomSQLConnection msq) {
        super();
        this.msq = msq;
        createbutton();
    }

    public JTable getTbl() {
        return tbl;
    }

    private void createbutton() {
        year = new JButton("Yearly Average");
        asc = new JButton("Oldest First");
        dsc = new JButton("Newest First");
        /* JToggleButton hour = new JToggleButton("Hour");
    JToggleButton min = new JToggleButton("Min");
    JToggleButton sec = new JToggleButton("sec");*/
        tbl = new JTable();
        tblContainer = new JScrollPane(tbl);
        tbl.setPreferredSize(new Dimension(1000, 300));
        msq = new CustomSQLConnection();



        groupTime.add(year);
        groupTime.add(asc);
        groupTime.add(dsc);

        this.setLayout(new BorderLayout());

        JPanel pnlNorth = new JPanel();
        JPanel pnlNorthWest = new JPanel();
        JPanel pnlNorthEast = new JPanel();
        JPanel pnlcenter = new JPanel();

        pnlNorthWest.add(year);
        pnlNorthWest.add(asc);
        pnlNorthWest.add(dsc);

        pnlNorth.setLayout(new BorderLayout());
        pnlNorth.add(pnlNorthWest, BorderLayout.WEST);
        pnlNorth.add(pnlNorthEast, BorderLayout.EAST);
        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(pnlcenter, BorderLayout.CENTER);
        pnlcenter.setLayout(new GridBagLayout());
        Box cartelBox = Box.createVerticalBox();
        cartelBox.setPreferredSize(new Dimension(1000, 300));
        cartelBox.add(tblContainer);

        tblContainer.setBorder(BorderFactory.createEmptyBorder());
        tblContainer.setViewportBorder(null);

        this.year.addActionListener(new AverageListener(1));
        this.asc.addActionListener(new AverageListener(2));
        this.dsc.addActionListener(new AverageListener(3));

        dsc.doClick();

        this.add(cartelBox);       

    }

    public class AverageListener implements ActionListener {

        private final int type;

        public AverageListener(int type){
            this.type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (type == 1){
                JButton jtb = (JButton) e.getSource();
                HistoriquePrixPanel hpp = (HistoriquePrixPanel) jtb.getParent().getParent().getParent();
                msq.send_request("SELECT annee, AVG(valeur) AS prixMoyen FROM (SELECT EXTRACT(YEAR FROM dateFin) AS annee,valeur FROM historiqueprix) AS t1 GROUP BY annee;", hpp.getTbl());

            } else if (type == 2){
                JButton jtb = (JButton) e.getSource();
                HistoriquePrixPanel hpp = (HistoriquePrixPanel) jtb.getParent().getParent().getParent();
                msq.send_request("SELECT * FROM historiqueprix ORDER BY dateFIN ASC",hpp.getTbl());
            } else if (type == 3){
                JButton jtb = (JButton) e.getSource();
                HistoriquePrixPanel hpp = (HistoriquePrixPanel) jtb.getParent().getParent().getParent();
                msq.send_request("SELECT * FROM historiqueprix ORDER BY dateFIN DESC",hpp.getTbl());
            }


        }
    }

    public JButton getDsc() {
        return dsc;
    }
}

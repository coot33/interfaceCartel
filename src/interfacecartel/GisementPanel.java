/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacecartel;

import databaseConnection.CustomSQLConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Al
 */
public class GisementPanel extends JPanel {

    CustomSQLConnection msq;
    private SelectionPanel gisementPanel;
    private AffichageGisement affGisPan;
    JTable table1;

    public GisementPanel(CustomSQLConnection msq) {
        this.msq = msq;
        addPanel();
    }

    private void addPanel() {
        JPanel compagnie = new JPanel();
        compagnie.setLayout(new GridBagLayout());

        String[] gisListe = {"Liste des gisements"};

        Object[][] gisData = new Object[0][0];

        table1 = new JTable(gisData, gisListe);

        JScrollPane gisTable = new JScrollPane(table1);

        gisTable.setPreferredSize(new Dimension(300, 200));
        this.add(gisTable);

        gisementPanel = new SelectionPanel("Gisement");
        //gisementPanel = new SelectionPanel("Compagnie");
        gisementPanel.btnSearch.addActionListener(new SearchEvent());
        ListSelectionModel lMdl = table1.getSelectionModel();
        lMdl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lMdl.addListSelectionListener(new SelectEvent());

        affGisPan = new AffichageGisement();

        this.add(gisementPanel);
        this.add(affGisPan);
        //this.gisementPanel.btnSearch.doClick();
    }

    private class SearchEvent implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            JTextField jtxt = gisementPanel.getJTxt();
            String rq = "SELECT nom FROM gisement WHERE nom like '%" + jtxt.getText() + "%';";
            table1.clearSelection();
            msq.send_request(rq, table1);

        }

    }

    private class SelectEvent implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent lse) {
            String s = (String) table1.getValueAt(table1.getSelectedRow(), 0);
            String reqInfo = "SELECT * FROM gisement WHERE nom = \"" + s + "\";";
            String reqCpny = "SELECT * FROM compagnie WHERE nom = ANY(SELECT nomCompagnie FROM exploitationgisement WHERE idGisement =(SELECT id FROM gisement WHERE nom = \"" + s + "\"))";
            String reqProd = "SELECT * FROM production WHERE idGisement = (SELECT id FROM gisement WHERE nom = \"" + s + "\");";

            msq.send_request(reqProd, affGisPan.tableProd);

            msq.send_request(reqCpny, affGisPan.tableCies);

            msq.send_request(reqInfo, affGisPan.tableInfo);

        }

    }

    public SelectionPanel getGisementPanel() {
        return gisementPanel;
    }
}

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import  java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppUtama extends JFrame {
    private PanelKeuangan panelKeuangan;
    private PanelTabungan panelTabungan;
    private PanelCatatan panelCatatan;
    private JLabel lblStatus;

    public AppUtama() {
        super("Smart Wallet, Savings & Priority Planner");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(680, 500);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();

        lblStatus = new JLabel(" Status: Anggaran awal Rp 1.000.000 & Target Tabungan Rp 500.000 siap digunakan");
        lblStatus.setPreferredSize(new Dimension(getWidth(), 28));

        panelKeuangan = new PanelKeuangan(this, lblStatus);
        panelTabungan = new PanelTabungan(this, panelKeuangan, lblStatus);
        panelCatatan = new PanelCatatan();

        tabbedPane.addTab("Pengelola Keuangan & Prioritas", panelKeuangan);
        tabbedPane.addTab("Target & Tabungan", panelTabungan);
        tabbedPane.addTab("Catatan Finansial", panelCatatan);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(lblStatus, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override 
            public void windowClosing(WindowEvent e) {
                int konfirmasi = JOptionPane.showConfirmDialog(
                    AppUtama.this,
                    "Apakah Anda tyakin ingin keluar dari Aplikasi?",
                    "konfirmasi keluar",
                    JOptionPane.YES_NO_OPTION
                );
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppUtama app = new AppUtama();
            app.setVisible(true);
        });
    }
}

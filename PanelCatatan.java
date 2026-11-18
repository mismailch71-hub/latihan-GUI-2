import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelCatatan extends JPanel implements ActionListener {
    private JTextArea txtCatatan;
    private JButton btnResetCatatan;

    public PanelCatatan() {
        setLayout(new BorderLayout(5, 5));

        txtCatatan = new JTextArea();
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        setTeksDefault();

        JScrollPane scrollCatatan = new JScrollPane(txtCatatan);

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnResetCatatan = new JButton("Reset Catatan Default");
        btnResetCatatan.addActionListener(this);
        panelBawah.add(btnResetCatatan);

        add(scrollCatatan, BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);
    }

    private void setTeksDefault() {
        txtCatatan.setText("--- CATATAN & EVALUASI FINANSIAL BULANAN ---\n" +
                           "1. Target Tabungan: Rp 500.000\n" +
                           "2. Prioritaskan belanja kebutuhan dapur dan kos.\n" +
                           "3. Hindari belanja impulsif di luar kategori [Tinggi] Primer."); 
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnResetCatatan) {
            setTeksDefault();
        }
    }
    
}

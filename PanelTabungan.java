import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PanelTabungan extends JPanel implements ActionListener, KeyListener {
    
    private JTextField txtKeteranganTabungan, txtNominalTabungan;
    private JButton btnSetor, btnTarik, btnUbahTarget;
    private JLabel lblTotalTabungan, lblInfoTarget;
    private JProgressBar progressTabungan;
    private DefaultListModel<String> modelRiwayatTabungan;
    private JList<String> listRiwayatTabungan;

    private JFrame parentFrame;
    private PanelKeuangan panelKeuangan;
    private JLabel lblStatus;

    private int totalTabungan = 0;
    private int targetTabungan = 500000;

    public PanelTabungan(JFrame parentFrame, PanelKeuangan panelKeuangan, JLabel lblStatus) {
        this.parentFrame = parentFrame;
        this.panelKeuangan = panelKeuangan;
        this.lblStatus = lblStatus;
            
        setLayout(new BorderLayout(8, 8));

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 5, 5));
        panelForm.add(new JLabel("Tujuan / Keterangan Tabungan:"));
        txtKeteranganTabungan = new JTextField();
        panelForm.add(txtKeteranganTabungan);

        panelForm.add(new JLabel(" Nominal Setor Tabungan (Rp):"));
        txtNominalTabungan = new JTextField();
        panelForm.add(txtNominalTabungan);

        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));

        JPanel panelHeaderTabungan = new JPanel(new GridLayout(3, 1, 3, 3));
        lblTotalTabungan = new JLabel("Total Tabungan Terkumpul: Rp 0", SwingConstants.CENTER);
        lblTotalTabungan.setFont(new Font("Times New Roman", Font.BOLD, 15));
        lblTotalTabungan.setForeground(new Color(0, 102, 204));

        lblInfoTarget = new JLabel("Target Tabungan Bulanan: Rp 500.000", SwingConstants.CENTER);

        progressTabungan = new JProgressBar(0, targetTabungan);
        progressTabungan.setValue(0);
        progressTabungan.setStringPainted(true);

        panelHeaderTabungan.add(lblTotalTabungan);
        panelHeaderTabungan.add(lblInfoTarget);
        panelHeaderTabungan.add(progressTabungan);

        modelRiwayatTabungan = new DefaultListModel<>();
        listRiwayatTabungan = new JList<>(modelRiwayatTabungan);

        JScrollPane scrollTabungan = new JScrollPane(listRiwayatTabungan);

        panelTengah.add(panelHeaderTabungan, BorderLayout.NORTH);
        panelTengah.add(scrollTabungan, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSetor = new JButton("Setor dari Budget");
        btnTarik = new JButton("Tarik ke Budget");
        btnUbahTarget = new JButton("Ubah Target");

        panelTombol.add(btnSetor);
        panelTombol.add(btnTarik);
        panelTombol.add(btnUbahTarget);

        add(panelForm, BorderLayout.NORTH);
        add(panelTengah, BorderLayout.CENTER);
        add(panelTombol, BorderLayout.SOUTH);

        btnSetor.addActionListener(this);
        btnTarik.addActionListener(this);
        btnUbahTarget.addActionListener(this);
        txtNominalTabungan.addKeyListener(this);
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSetor) {
            setorTabungan();
        } else if (e.getSource() == btnTarik) {
            tarikTabungan();
        } else if (e.getSource() == btnUbahTarget) {
            btnUbahTarget();
        }
    }

    @Override 
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            setorTabungan();
        }
    }

    @Override 
    public void keyTyped(KeyEvent e) {}

    @Override 
    public void keyReleased(KeyEvent e) {
        lblStatus.setText(" Status: Mengetik nominal tabungan Rp " + txtNominalTabungan.getText() + "...");
    }

    private void setorTabungan() {
        String keterangan = txtKeteranganTabungan.getText().trim();
        String strNominal = txtNominalTabungan.getText().trim();

        if (keterangan.isEmpty() || strNominal.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame,
                "Keterangan dan nominal tabungan tidak boleh kosong!",
                "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int nominal = Integer.parseInt(strNominal);
            if (nominal <= 0) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Nominal tabungan harus lebih dari 0!",
                    "Error Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!panelKeuangan.potongBudgetUntukTabungan(nominal, keterangan)) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Sisa budget bulanan tidak mencukupi untuk ditabung!",
                    "Peringatan Budget", JOptionPane.WARNING_MESSAGE);
                return;
            }

            totalTabungan += nominal;
            modelRiwayatTabungan.addElement("[Setor] " + keterangan + " (Rp " + nominal + ")");
            updateTampilanTabungan();

            txtKeteranganTabungan.setText("");
            txtNominalTabungan.setText("");
            lblStatus.setText(" Status: Berhasil menabung " + keterangan + " sebesar Rp " + String.format("%,d", nominal));

            if (totalTabungan >= targetTabungan) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Selamat! Target tabungan bulanan Anda telah tercapai!",
                    "Target Tercapai", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame,
                "Nominal tabungan harus berupa angka!",
                "Error Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tarikTabungan() {
        int selectedIndex = listRiwayatTabungan.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(parentFrame,
                "Pilih riwayat setoran tabungan yang ingin ditarik kembali ke budget!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String item = modelRiwayatTabungan.getElementAt(selectedIndex);
        try {
            String strNominal = item.substring(item.lastIndexOf("Rp ") + 3, item.length() - 1);
            int nominalTarik = Integer.parseInt(strNominal);

            totalTabungan -= nominalTarik;
            panelKeuangan.kembaliBudgetDariTabungan(nominalTarik);
            modelRiwayatTabungan.remove(selectedIndex);

            updateTampilanTabungan();
            lblStatus.setText(" Status: Dana tabungan Rp " + String.format("%,d", nominalTarik) + " dikembalikan ke budget.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Gagal memproses penarikan tabungan.");
        }
    }

    private void ubahTargetTabungan() {
        String input = JOptionPane.showInputDialog(parentFrame,
            "Masukkan target tabungan bulanan baru (Rp):",
            targetTabungan);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int targetBaru = Integer.parseInt(input.trim());
                if(targetBaru > 0) {
                    targetTabungan = targetBaru;
                    progressTabungan.setMaximum(targetTabungan);
                    updateTampilanTabungan();
                    lblStatus.setText(" Status: Target tabungan diperbarui menjadi Rp " + String.format("%,d", targetTabungan));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Target harus berupa angka valid!",
                    "Error Format", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTampilanTabungan() {
        lblTotalTabungan.setText("Total Tabungan Terkumpul: Rp " + String.format("%,d", totalTabungan));
        lblInfoTarget.setText("Target Tabungan Bulanan: Rp " + String.format("%,d", targetTabungan));
        progressTabungan.setValue(Math.min(totalTabungan, targetTabungan));
    }
}



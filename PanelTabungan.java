import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PanelTabungan extends JPanel implements ActionListener, KeyListener {

    private JTextField txtSumberTabungan, txtNominalTabungan;
    private JButton btnSetor, btnHapusTabungan, btnUbahTarget;
    private JLabel lblTotalTabungan, lblInfoTarget;
    private JProgressBar progressTabungan;
    private DefaultListModel<String> modelRiwayatTabungan;
    private JList<String> listRiwayatTabungan;

    private JFrame parentFrame;
    private JLabel lblStatus;

    private int totalTabungan = 0;
    private int targetTabungan = 1000000;

    public PanelTabungan(JFrame parentFrame, JLabel lblStatus) {
        this.parentFrame = parentFrame;
        this.lblStatus = lblStatus;

        setLayout(new BorderLayout(8, 8));

        // Form Input
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 5, 5));
        panelForm.add(new JLabel(" Sumber Dana / Keterangan:"));
        txtSumberTabungan = new JTextField();
        panelForm.add(txtSumberTabungan);

        panelForm.add(new JLabel(" Nominal Tabungan (Rp):"));
        txtNominalTabungan = new JTextField();
        panelForm.add(txtNominalTabungan);

        // Panel Tengah
        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));

        JPanel panelHeaderTabungan = new JPanel(new GridLayout(3, 1, 3, 3));
        lblTotalTabungan = new JLabel("Total Tabungan Terkumpul: Rp 0", SwingConstants.CENTER);
        lblTotalTabungan.setFont(new Font("Times New Roman", Font.BOLD, 15));
        lblTotalTabungan.setForeground(new Color(0, 102, 204));

        lblInfoTarget = new JLabel("Target Tabungan: Rp 1.000.000", SwingConstants.CENTER);

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

        // Panel Tombol
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSetor = new JButton("Tambah Tabungan");
        btnHapusTabungan = new JButton("Hapus Riwayat");
        btnUbahTarget = new JButton("Ubah Target");

        panelTombol.add(btnSetor);
        panelTombol.add(btnHapusTabungan);
        panelTombol.add(btnUbahTarget);

        add(panelForm, BorderLayout.NORTH);
        add(panelTengah, BorderLayout.CENTER);
        add(panelTombol, BorderLayout.SOUTH);

        btnSetor.addActionListener(this);
        btnHapusTabungan.addActionListener(this);
        btnUbahTarget.addActionListener(this);
        txtNominalTabungan.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSetor) {
            tambahTabungan();
        } else if (e.getSource() == btnHapusTabungan) {
            hapusTabungan();
        } else if (e.getSource() == btnUbahTarget) {
            ubahTargetTabungan();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            tambahTabungan();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        lblStatus.setText(" Status: Mengetik nominal tabungan Rp " + txtNominalTabungan.getText() + "...");
    }

    private void tambahTabungan() {
        String sumber = txtSumberTabungan.getText().trim();
        String strNominal = txtNominalTabungan.getText().trim();

        if (sumber.isEmpty() || strNominal.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame,
                "Sumber dana dan nominal tabungan tidak boleh kosong!",
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

            // Langsung menambah total tabungan tanpa memotong budget pengeluaran
            totalTabungan += nominal;
            modelRiwayatTabungan.addElement("[Masuk] " + sumber + " (+Rp " + nominal + ")");
            updateTampilanTabungan();

            txtSumberTabungan.setText("");
            txtNominalTabungan.setText("");
            lblStatus.setText(" Status: Tabungan bertambah Rp " + String.format("%,d", nominal) + " dari " + sumber);

            if (totalTabungan >= targetTabungan) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Selamat! Target tabungan Anda telah tercapai!",
                    "Target Tercapai", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame,
                "Nominal tabungan harus berupa angka!",
                "Error Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusTabungan() {
        int selectedIndex = listRiwayatTabungan.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(parentFrame,
                "Pilih riwayat tabungan yang ingin dihapus!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String item = modelRiwayatTabungan.getElementAt(selectedIndex);
        try {
            String strNominal = item.substring(item.lastIndexOf("Rp ") + 3, item.length() - 1);
            int nominalHapus = Integer.parseInt(strNominal);

            totalTabungan -= nominalHapus;
            modelRiwayatTabungan.remove(selectedIndex);

            updateTampilanTabungan();
            lblStatus.setText(" Status: Riwayat tabungan dihapus, total dikurangi Rp " + String.format("%,d", nominalHapus));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Gagal menghapus riwayat tabungan.");
        }
    }

    private void ubahTargetTabungan() {
        String input = JOptionPane.showInputDialog(parentFrame,
            "Masukkan target tabungan baru (Rp):",
            targetTabungan);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int targetBaru = Integer.parseInt(input.trim());
                if (targetBaru > 0) {
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
        lblInfoTarget.setText("Target Tabungan: Rp " + String.format("%,d", targetTabungan));
        progressTabungan.setValue(Math.min(totalTabungan, targetTabungan));
    }
}
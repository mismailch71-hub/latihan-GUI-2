import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class PanelKeuangan extends JPanel implements ActionListener, KeyListener {
    private JTextField txtPengeluaran, txtNominal;
    private JComboBox<String> cmbPrioritas;
    private JButton btnTambah, btnHapus, btnInfo;
    private DefaultListModel<String> modelList;
    private JList<String> listPengeluaran;
    private JLabel lblSisaBudget;

    private JFrame parentFrame;
    private JLabel lblStatus;

    private int sisaBudget = 1000000;

    public PanelKeuangan(JFrame parentFrame, JLabel lblStatus) {
        this.parentFrame = parentFrame;
        this.lblStatus = lblStatus;

        setLayout(new BorderLayout(8, 8));

        JPanel panelInput = new JPanel(new GridLayout(3, 2, 5, 5));

        JLabel lblNama = new JLabel(" Nama Pengeluaran:");
        txtPengeluaran = new JTextField();

        JLabel lblNominal = new JLabel(" Nominal (Rp):");
        txtNominal = new JTextField();

        JLabel lblPrioritas = new JLabel(" Tingkat Prioritas:");
        String[] opsiPrioritas = {"[Tinggi] Primer", "[Sedang] Sekunder", "[Rendah] Tersier"};
        cmbPrioritas = new JComboBox<>(opsiPrioritas);

        panelInput.add(lblNama);
        panelInput.add(txtPengeluaran);
        panelInput.add(lblNominal);
        panelInput.add(txtNominal);
        panelInput.add(lblPrioritas);
        panelInput.add(cmbPrioritas);

        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));

        lblSisaBudget = new JLabel("Sisa Budget Bulan ini: Rp 1.000.000", SwingConstants.CENTER);
        lblSisaBudget.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lblSisaBudget.setForeground(new Color(0, 128, 0));

        modelList = new DefaultListModel<>();
        listPengeluaran = new JList<>(modelList);

        JScrollPane scrollList = new JScrollPane(listPengeluaran);

        panelTengah.add(lblSisaBudget, BorderLayout.NORTH);
        panelTengah.add(scrollList, BorderLayout.CENTER);

        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnTambah = new JButton("Tambah");
        btnHapus = new JButton("Hapus");
        btnInfo = new JButton("Info Budget");

        panelAksi.add(btnTambah);
        panelAksi.add(btnHapus);
        panelAksi.add(btnInfo);

        add(panelInput, BorderLayout.NORTH);
        add(panelTengah, BorderLayout.CENTER);
        add(panelAksi, BorderLayout.SOUTH);

        btnTambah.addActionListener(this);
        btnHapus.addActionListener(this);
        btnInfo.addActionListener(this);
        txtNominal.addKeyListener(this);

        listPengeluaran.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && listPengeluaran.getSelectedIndex() != -1) {
                    String itemTerpilih = listPengeluaran.getSelectedValue();
                    JOptionPane.showMessageDialog(
                        parentFrame,
                        "Detail Transaksi:\n" + itemTerpilih,
                        "Detail Pengeluaran",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
    }

    public boolean potongBudgetUntukTabungan(int nominal, String keterangan) {
        if (nominal > sisaBudget) {
            return false;
        }
        sisaBudget -= nominal;
        modelList.addElement("[Tabungan] " + keterangan + " (Rp " + nominal + ")");
        updateTampilanBudget();
        return true;
    }

    public void kembaliBudgetDariTabungan(int nominal) {
        sisaBudget += nominal;
        updateTampilanBudget();
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTambah) {
            tambahPengeluaran();
        } else if (e.getSource() == btnHapus) {
            hapusPengeluaran();
        } else if (e.getSource() == btnInfo) {
            JOptionPane.showMessageDialog(parentFrame,
                "Sistem Pengelola Keuangan & Tabungan Bulanan\n" +
                "- Budget Awal: Rp 1.000.000\n" +
                "- Gunakan tab 'Target & Tabungan' untuk menyisihkan dana tabungan.\n" +
                "- Klik 2x pada item daftar untuk melihat detail transaksi.",
                "Informasi Keuangan", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override 
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            tambahPengeluaran();
        }
    }

    @Override 
    public void keyTyped(KeyEvent e) {}

    @Override 
    public void keyReleased(KeyEvent e) {
        lblStatus.setText(" Status: Mengetik nominal Rp " + txtNominal.getText() + "...");
    }

    private void tambahPengeluaran() {
        String nama = txtPengeluaran.getText().trim();
        String strNominal = txtNominal.getText().trim();
        String prioritas = (String) cmbPrioritas.getSelectedItem();

        if (nama.isEmpty() || strNominal.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame,
                "Nama Pengeluaran dan nominal tidak boleh kosong!",
                "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int nominal = Integer.parseInt(strNominal);

            if (nominal <= 0) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Nominal harus lebih besar dari 0!",
                    "Error Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nominal > sisaBudget) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Pengeluaran melebihi sisa budget bulan ini!",
                    "Peringatan Anggaran", JOptionPane.WARNING_MESSAGE);
                return;
            }

            sisaBudget -= nominal;
            String item = prioritas + " - " + nama + " (Rp " + nominal + ")";
            modelList.addElement(item);

            updateTampilanBudget();
            txtPengeluaran.setText("");
            txtNominal.setText("");
            lblStatus.setText(" Status: Berhasil menambah " + nama + " sebesar Rp " + nominal);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame,
                "Nominal pengeluaran harus berupa angka!",
                "Error Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusPengeluaran() {
        int selectedIndex = listPengeluaran.getSelectedIndex();
        if (selectedIndex != -1) {
            String item = modelList.getElementAt(selectedIndex);

            if (item.startsWith("[Tabungan]")) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Item tabungan hanya dapat ditarik melalui tab 'Target & Tabungan'!",
                    "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                
            }
        }
    }
    
}

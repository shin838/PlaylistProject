package main;

import java.awt.*;
import java.sql.Connection;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.MusicDao;
import dao.PlaylistDao;
import dao.PlaylistMusicDao;
import dto.MusicDto;
import dto.PlaylistDto;
import util.DBUtil;

public class Main {
    private static CardLayout cardLayout;
    private static JPanel mainCardPanel;

    public static void main(String[] args) {
        try {
            Connection conn = DBUtil.getInstance().getConnection();
            System.out.println("DB 연결 성공! 화면을 불러옵니다.");
            DBUtil.getInstance().close(null, conn); 
        } catch (Exception e) {
            System.out.println("DB 연결 실패! DB 설정을 확인해주세요.");
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("🎵 Playlist Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650); 
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);

        mainCardPanel.add(createHomePanel(), "HOME");
        mainCardPanel.add(createMusicPanel(), "MUSIC");
        mainCardPanel.add(createCombinedPlaylistPanel(), "PLAYLIST");
        mainCardPanel.add(createPlaceholderPanel("👤 회원 관리 화면 준비 중..."), "USER");

        frame.add(mainCardPanel);
        frame.setVisible(true);
    }

    // ==========================================
    // 1. [홈 화면] 패널 생성
    // ==========================================
    private static JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(245, 246, 250));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        JLabel titleLabel = new JLabel("플레이리스트 관리 프로그램");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        menuPanel.setOpaque(false);

        JButton btnMusic = createButton("🎧 음악 관리");
        JButton btnPlaylist = createButton("📁 플레이리스트 관리");
        JButton btnUser = createButton("👤 회원 관리");

        btnMusic.addActionListener(e -> cardLayout.show(mainCardPanel, "MUSIC"));
        btnPlaylist.addActionListener(e -> cardLayout.show(mainCardPanel, "PLAYLIST"));
        btnUser.addActionListener(e -> cardLayout.show(mainCardPanel, "USER"));

        menuPanel.add(btnMusic);
        menuPanel.add(btnPlaylist);
        menuPanel.add(btnUser);
        menuPanel.add(new JPanel());

        homePanel.add(titlePanel, BorderLayout.NORTH);
        homePanel.add(menuPanel, BorderLayout.CENTER);

        return homePanel;
    }

    // ==========================================
    // 2. [음악 관리] 패널 생성 (CRUD 연동 완료)
    // ==========================================
    private static JPanel createMusicPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        MusicDao musicDao = new MusicDao();

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("🎧 음악 관리 (CRUD)");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        JButton btnHome = new JButton("🏠 홈으로");
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnHome, BorderLayout.EAST);

        String[] cols = {"Music ID", "제목", "아티스트", "장르", "재생시간"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField txtId = new JTextField(3); txtId.setEnabled(false); // PK 수정 불가
        JTextField txtTitle = new JTextField(10);
        JTextField txtArtist = new JTextField(10);
        JTextField txtGenre = new JTextField(8);
        JTextField txtTime = new JTextField(5);
        
        inputPanel.add(new JLabel("ID:")); inputPanel.add(txtId);
        inputPanel.add(new JLabel("제목:")); inputPanel.add(txtTitle);
        inputPanel.add(new JLabel("아티스트:")); inputPanel.add(txtArtist);
        inputPanel.add(new JLabel("장르:")); inputPanel.add(txtGenre);
        inputPanel.add(new JLabel("재생시간:")); inputPanel.add(txtTime);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnSelect = new JButton("전체조회");
        JButton btnAdd = new JButton("추가");
        JButton btnUpdate = new JButton("수정");
        JButton btnDelete = new JButton("삭제");

        // 테이블 행 클릭 시 입력칸으로 데이터 복사
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (!e.getValueIsAdjusting() && row != -1) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtTitle.setText(tableModel.getValueAt(row, 1).toString());
                txtArtist.setText(tableModel.getValueAt(row, 2).toString());
                txtGenre.setText(tableModel.getValueAt(row, 3).toString());
                txtTime.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        btnSelect.addActionListener(e -> {
            try {
                List<MusicDto> list = musicDao.searchAllMusic();
                tableModel.setRowCount(0);
                for (MusicDto m : list) {
                    tableModel.addRow(new Object[]{m.getMusicId(), m.getTitle(), m.getArtist(), m.getGenre(), m.getPlayTime()});
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnAdd.addActionListener(e -> {
            try {
                MusicDto m = new MusicDto();
                m.setTitle(txtTitle.getText()); m.setArtist(txtArtist.getText());
                m.setGenre(txtGenre.getText()); m.setPlayTime(txtTime.getText());
                musicDao.addMusic(m);
                JOptionPane.showMessageDialog(panel, "추가 완료");
                btnSelect.doClick(); // 새로고침
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                if(txtId.getText().isEmpty()) return;
                MusicDto m = new MusicDto();
                m.setMusicId(Integer.parseInt(txtId.getText()));
                m.setTitle(txtTitle.getText()); m.setArtist(txtArtist.getText());
                m.setGenre(txtGenre.getText()); m.setPlayTime(txtTime.getText());
                musicDao.updateMusic(m);
                JOptionPane.showMessageDialog(panel, "수정 완료");
                btnSelect.doClick();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnDelete.addActionListener(e -> {
            try {
                if(txtId.getText().isEmpty()) return;
                int id = Integer.parseInt(txtId.getText());
                musicDao.removeMusic(id);
                JOptionPane.showMessageDialog(panel, "삭제 완료");
                txtId.setText(""); txtTitle.setText(""); txtArtist.setText(""); txtGenre.setText(""); txtTime.setText("");
                btnSelect.doClick();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnPanel.add(btnSelect); btnPanel.add(btnAdd); 
        btnPanel.add(btnUpdate); btnPanel.add(btnDelete);

        bottomPanel.add(inputPanel); bottomPanel.add(btnPanel);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ==========================================
    // 3. [플레이리스트 통합 관리] 패널 생성 (연동 완료)
    // ==========================================
    private static JPanel createCombinedPlaylistPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        PlaylistDao playlistDao = new PlaylistDao();
        PlaylistMusicDao pmDao = new PlaylistMusicDao();

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("📁 플레이리스트 상세 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        JButton btnHome = new JButton("🏠 홈으로");
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnHome, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        String[] plCols = {"PL ID", "플레이리스트 이름"};
        DefaultTableModel plTableModel = new DefaultTableModel(plCols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable plTable = new JTable(plTableModel);
        JScrollPane plScroll = new JScrollPane(plTable);
        plScroll.setBorder(BorderFactory.createTitledBorder("이메일의 플레이리스트 목록"));

        String[] musicCols = {"Music ID", "제목", "아티스트", "재생시간"};
        DefaultTableModel musicTableModel = new DefaultTableModel(musicCols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable musicTable = new JTable(musicTableModel);
        JScrollPane musicScroll = new JScrollPane(musicTable);
        musicScroll.setBorder(BorderFactory.createTitledBorder("선택된 플레이리스트의 노래"));

        splitPane.setLeftComponent(plScroll);
        splitPane.setRightComponent(musicScroll);
        splitPane.setDividerLocation(350); 

        plTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && plTable.getSelectedRow() != -1) {
                int playlistId = (int) plTableModel.getValueAt(plTable.getSelectedRow(), 0);
                try {
                    List<MusicDto> musicList = pmDao.getMusicInPlaylist(playlistId);
                    musicTableModel.setRowCount(0);
                    for (MusicDto m : musicList) {
                        musicTableModel.addRow(new Object[]{m.getMusicId(), m.getTitle(), m.getArtist(), m.getPlayTime()});
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtEmail = new JTextField(15);
        JButton btnSearchEmail = new JButton("이메일로 조회");
        searchPanel.add(new JLabel("이메일 검색:")); searchPanel.add(txtEmail); searchPanel.add(btnSearchEmail);

        btnSearchEmail.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            if (email.isEmpty()) return;
            try {
                List<PlaylistDto> plList = playlistDao.getPlaylistsByEmail(email);
                plTableModel.setRowCount(0); musicTableModel.setRowCount(0);
                for (PlaylistDto p : plList) {
                    plTableModel.addRow(new Object[]{p.getPlaylistId(), p.getPlaylistName()});
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(panel, ex.getMessage()); }
        });

        JPanel plControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtPlName = new JTextField(12);
        JButton btnAddPl = new JButton("플레이리스트 생성");
        JButton btnDelPl = new JButton("선택한 플레이리스트 삭제");
        plControlPanel.add(new JLabel("새 이름:")); plControlPanel.add(txtPlName);
        plControlPanel.add(btnAddPl); plControlPanel.add(btnDelPl);

        btnAddPl.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String plName = txtPlName.getText().trim();
            if (email.isEmpty() || plName.isEmpty()) return;
            try {
                playlistDao.addPlaylist(email, plName);
                txtPlName.setText(""); btnSearchEmail.doClick();
            } catch (Exception ex) { JOptionPane.showMessageDialog(panel, ex.getMessage()); }
        });

        btnDelPl.addActionListener(e -> {
            int row = plTable.getSelectedRow();
            if (row == -1) return;
            try {
                playlistDao.deletePlaylist((int) plTableModel.getValueAt(row, 0));
                btnSearchEmail.doClick();
            } catch (Exception ex) { JOptionPane.showMessageDialog(panel, ex.getMessage()); }
        });

        JPanel musicControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtAddMusicId = new JTextField(8);
        JButton btnAddMusic = new JButton("노래 추가 (Music ID)");
        JButton btnDelMusic = new JButton("선택한 노래 삭제");
        musicControlPanel.add(new JLabel("추가할 Music ID:")); musicControlPanel.add(txtAddMusicId);
        musicControlPanel.add(btnAddMusic); musicControlPanel.add(btnDelMusic);

        btnAddMusic.addActionListener(e -> {
            int row = plTable.getSelectedRow();
            if (row == -1 || txtAddMusicId.getText().trim().isEmpty()) return;
            try {
                pmDao.addMusicToPlaylist((int) plTableModel.getValueAt(row, 0), Integer.parseInt(txtAddMusicId.getText().trim()));
                txtAddMusicId.setText("");
                int tempRow = plTable.getSelectedRow();
                plTable.clearSelection(); plTable.setRowSelectionInterval(tempRow, tempRow);
            } catch (Exception ex) { JOptionPane.showMessageDialog(panel, "곡 추가 실패(ID 확인 혹은 중복)"); }
        });

        btnDelMusic.addActionListener(e -> {
            int plRow = plTable.getSelectedRow();
            int mRow = musicTable.getSelectedRow();
            if (plRow == -1 || mRow == -1) return;
            try {
                pmDao.removeMusicFromPlaylist((int) plTableModel.getValueAt(plRow, 0), (int) musicTableModel.getValueAt(mRow, 0));
                int tempRow = plTable.getSelectedRow();
                plTable.clearSelection(); plTable.setRowSelectionInterval(tempRow, tempRow);
            } catch (Exception ex) { JOptionPane.showMessageDialog(panel, ex.getMessage()); }
        });

        bottomPanel.add(searchPanel); bottomPanel.add(plControlPanel); bottomPanel.add(musicControlPanel);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        JButton btnHome = new JButton("🏠 메인 메뉴로 돌아가기");
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));
        JPanel btnPanel = new JPanel(); btnPanel.add(btnHome);
        panel.add(label, BorderLayout.CENTER); panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(44, 62, 80)); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
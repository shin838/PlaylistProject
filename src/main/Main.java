package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import dto.UserDto;
import service.UserService;
import service.UserServiceImp;
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
            // OS 기본 디자인 적용
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("🎵 Playlist Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650); 
        frame.setLocationRelativeTo(null);
        // 메인 프레임 배경색을 다크 톤으로 설정
        frame.getContentPane().setBackground(new Color(18, 18, 18));

        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);

        // 생성한 패널들을 CardLayout에 등록
        mainCardPanel.add(createHomePanel(), "HOME");
        mainCardPanel.add(createMusicPanel(), "MUSIC");
        mainCardPanel.add(createCombinedPlaylistPanel(), "PLAYLIST");
        mainCardPanel.add(createUserPanel(), "USER");
        mainCardPanel.add(createHot5Panel(), "HOT5");

        frame.add(mainCardPanel);
        frame.setVisible(true);
    }

 // ==========================================
    // 1. [홈 화면] 패널 생성 (화이트 아이콘 적용)
    // ==========================================
    private static JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(18, 18, 18)); // 스포티파이 느낌의 다크 배경

        // [상단] 타이틀 배너 구역
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(18, 18, 18));
        titlePanel.setBorder(new EmptyBorder(60, 0, 30, 0));

        JLabel subLabel = new JLabel("Your Personal Music Studio");
        subLabel.setFont(new Font("Arial", Font.BOLD, 14));
        subLabel.setForeground(new Color(179, 179, 179)); // 연한 회색
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("🎵 Playlist Management");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(subLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(titleLabel);

        // [중앙] 카드형 메뉴 버튼 구역
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        menuPanel.setBorder(new EmptyBorder(10, 80, 80, 80));
        menuPanel.setOpaque(false);

        // 이모지 대신 색상 변경이 가능한 특수기호 사용
        JButton btnMusic = createMenuCard("♫", "음악 관리", "새로운 곡을 추가하고 수정하세요");
        JButton btnPlaylist = createMenuCard("▤", "플레이리스트", "나만의 취향을 담은 리스트 관리");
        JButton btnUser = createMenuCard("☻", "회원 관리", "이용자 정보를 등록하고 관리합니다");
        JButton btnHot5 = createMenuCard("★", "HOT 5", "가장 인기있는 트랙 랭킹 TOP 5");

        // 화면 이동 이벤트
        btnMusic.addActionListener(e -> cardLayout.show(mainCardPanel, "MUSIC"));
        btnPlaylist.addActionListener(e -> cardLayout.show(mainCardPanel, "PLAYLIST"));
        btnUser.addActionListener(e -> cardLayout.show(mainCardPanel, "USER"));
        btnHot5.addActionListener(e -> cardLayout.show(mainCardPanel, "HOT5")); 

        menuPanel.add(btnMusic);
        menuPanel.add(btnPlaylist);
        menuPanel.add(btnUser);
        menuPanel.add(btnHot5);

        homePanel.add(titlePanel, BorderLayout.NORTH);
        homePanel.add(menuPanel, BorderLayout.CENTER);

        return homePanel;
    }

    // ==========================================
    // [UI 헬퍼] 카드 형태의 고급스러운 버튼을 만드는 메서드 (아이콘 화이트 설정)
    // ==========================================
    private static JButton createMenuCard(String icon, String title, String desc) {
        // 아이콘(<p> 태그) 부분에 color: white; 를 강제로 부여했습니다.
        String html = "<html><div style='text-align: center; padding: 10px;'>"
                    + "<p style='font-size: 36px; margin-bottom: 5px; color: white;'>" + icon + "</p>"
                    + "<p style='font-size: 18px; font-family: 맑은 고딕; font-weight: bold; color: white; margin: 0;'>" + title + "</p>"
                    + "<p style='font-size: 11px; font-family: 맑은 고딕; color: #B3B3B3; margin-top: 8px;'>" + desc + "</p>"
                    + "</div></html>";
        
        JButton button = new JButton(html);
        button.setBackground(new Color(35, 35, 35)); // 다크 그레이 카드
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 마우스 호버(Hover) 효과 - 올리면 밝아짐
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(55, 55, 55)); 
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(35, 35, 35));
            }
        });
        
        return button;
    }


    /* =========================================================================
       여기서부터 아래는 기존에 정상 작동하던 사용자 작성 코드입니다. 그대로 유지했습니다. 
       ========================================================================= */

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
                btnSelect.doClick(); 
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
    // 3. [플레이리스트 통합 관리] 패널 생성
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

    // ==========================================
    // 4. [회원 관리] 패널 생성
    // ==========================================
    private static JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        UserService userService = new UserServiceImp();

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("회원 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        JButton btnHome = new JButton("홈으로");
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnHome, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtUserId = new JTextField(15);
        JTextField txtEmail = new JTextField(20);
        JTextField txtName = new JTextField(20);

        txtUserId.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("회원 ID:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(txtUserId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("이메일:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("이름:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(txtName, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("회원등록");
        JButton btnSearch = new JButton("이메일 조회");
        JButton btnUpdate = new JButton("회원수정");
        JButton btnDelete = new JButton("회원삭제");
        JButton btnClear = new JButton("입력 초기화");

        btnPanel.add(btnAdd);
        btnPanel.add(btnSearch);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        btnAdd.addActionListener(e -> {
            try {
                UserDto user = new UserDto();
                user.setEmail(txtEmail.getText());
                user.setName(txtName.getText());
                userService.add(user);
                JOptionPane.showMessageDialog(panel, "회원등록 완료");
                UserDto savedUser = userService.searchByEmail(user.getEmail());
                txtUserId.setText(String.valueOf(savedUser.getUserId()));
                txtEmail.setText(savedUser.getEmail());
                txtName.setText(savedUser.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage());
            }
        });

        btnSearch.addActionListener(e -> {
            try {
                UserDto user = userService.searchByEmail(txtEmail.getText());
                txtUserId.setText(String.valueOf(user.getUserId()));
                txtEmail.setText(user.getEmail());
                txtName.setText(user.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                if (txtUserId.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "먼저 이메일로 회원을 조회하세요.");
                    return;
                }
                UserDto user = new UserDto();
                user.setUserId(Integer.parseInt(txtUserId.getText()));
                user.setEmail(txtEmail.getText());
                user.setName(txtName.getText());
                userService.update(user);
                JOptionPane.showMessageDialog(panel, "회원수정 완료");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                if (txtUserId.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "먼저 이메일로 회원을 조회하세요.");
                    return;
                }
                int result = JOptionPane.showConfirmDialog(panel, "회원정보를 삭제하시겠습니까?", "회원삭제", JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) return;
                userService.remove(Integer.parseInt(txtUserId.getText()));
                txtUserId.setText(""); txtEmail.setText(""); txtName.setText("");
                JOptionPane.showMessageDialog(panel, "회원삭제 완료");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage());
            }
        });

        btnClear.addActionListener(e -> {
            txtUserId.setText(""); txtEmail.setText(""); txtName.setText("");
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ==========================================
    // 5. [HOT 5] 패널 생성
    // ==========================================
    private static JPanel createHot5Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        PlaylistMusicDao pmDao = new PlaylistMusicDao();

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("HOT 5");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        JButton btnHome = new JButton("홈으로");
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnHome, BorderLayout.EAST);

        String[] cols = {"순위", "제목", "가수", "장르"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton btnRefresh = new JButton("새로고침");
        bottomPanel.add(btnRefresh);

        Runnable loadHot5 = () -> {
            try {
                List<MusicDto> hotList = pmDao.getHot5Music();

                tableModel.setRowCount(0);

                int rank = 1;
                for (MusicDto m : hotList) {
                    tableModel.addRow(new Object[]{
                        rank++,
                        m.getTitle(),
                        m.getArtist(),
                        m.getGenre()
                    });
                }

                if (hotList.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "HOT 5 데이터가 없습니다.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage());
            }
        };

        btnRefresh.addActionListener(e -> loadHot5.run());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadHot5.run();

        return panel;
    }
    
    // ==========================================
    // [기타] 준비 중 화면을 위한 공통 패널
    // ==========================================
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
}
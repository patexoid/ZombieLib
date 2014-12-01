package lib.front;

import lib.back.DummyData;
import lib.back.dataobj.Author;
import lib.back.dataobj.HibernateUtils;
import lib.back.dataobj.mysqldumpparser.Parser;
import lib.back.dataobj.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by alex on 26.01.14.
 */
@org.springframework.stereotype.Component
public class MainJFrame extends JFrame implements ActionListener {

    private JTable _authorTable;
    private AuthorTableModel _authorTableModel;
    private JTreeTable _bookTreeTable;
    private AuthorTreeTableModel _bookTreeTableModel;
    private JButton _updateDB;

//    @Autowired
    private Parser _parser;

    @Autowired(required = true)
    private AuthorRepository _authorRepository;

    public MainJFrame() throws HeadlessException {
    }

    public void start(){
        add(createMainPanel());
        addListeners();
        setVisible(true);
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    private void addListeners() {
        _authorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mc();
            }

            private void mc() {
                int selectedRow = _authorTable.getSelectedRow();
                Author rowObject = _authorTableModel.getRowObject(selectedRow);
                _bookTreeTableModel.setAuthor(rowObject);
                _bookTreeTableModel.fireTreeStructureChanged(this,new Object[]{rowObject}, new int[]{},new Object[]{});
            }
        });
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createButtonPanel(), BorderLayout.NORTH);
        panel.add(createDataPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDataPanel() {
        JPanel jPanel = new JPanel();
        jPanel.add(createTable());
        jPanel.add(createTreeTable());
        return jPanel;
    }

    private Component createTable() {
        _authorTableModel = new AuthorTableModel();
//        _authorTableModel.setAuthorList(HibernateUtils.loadObjects(Author.class));
        Iterable<Author> all = _authorRepository.findAll();
        _authorTableModel.setAuthorList(new ArrayList<Author>((Collection<? extends Author>) all));
        _authorTable = new JTable(_authorTableModel);
        return new JScrollPane(_authorTable);
    }


    private JScrollPane createTreeTable() {
        _bookTreeTableModel = new AuthorTreeTableModel(DummyData.getDummy().get(0));
        _bookTreeTable = new JTreeTable(_bookTreeTableModel);
        return new JScrollPane(_bookTreeTable);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        _updateDB = new JButton("updateDB");
        _updateDB.addActionListener(this);
        panel.add(_updateDB);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==_updateDB){
            new Thread(){
                @Override
                public void run() {
                    try {
                        _parser.parseAll("..\\dump");
                        JOptionPane optPane=new JOptionPane("boook DB was updated", JOptionPane.INFORMATION_MESSAGE);
                        optPane.createDialog(MainJFrame.this,"boook DB was updated").setVisible(true);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        JOptionPane optPane=new JOptionPane("boook DB was updated with errors", JOptionPane.ERROR_MESSAGE);
                        optPane.createDialog(MainJFrame.this,"boook DB was updated with errors").setVisible(true);
                    }
                }
            }.start();
        }

    }

    private static class AuthorTableModel extends AbstractTableModel {
        List<Author> _authorList = new ArrayList<Author>();

        @Override
        public int getRowCount() {
            return _authorList.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return _authorList.get(rowIndex).getFirstName();
        }

        public void setAuthorList(List<Author> authorList) {
            _authorList = authorList;
        }

        public Author getRowObject(int selectedRow) {
            return _authorList.get(selectedRow);
        }
    }
}

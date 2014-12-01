package lib.front;

import lib.back.dataobj.Author;
import lib.back.dataobj.Book;
import lib.back.dataobj.BookSequence;
import lib.back.dataobj.Sequence;

/**
 * Created by alex on 26.01.14.
 */
public class AuthorTreeTableModel extends AbstractTreeTableModel {
    private static String[] columns = new String[]{"name", "size"};

    public AuthorTreeTableModel(Object root) {
        super(root);
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof Sequence) {
            if (column == 0) {
                return ((Sequence) node).getSeqName();
            }
        } else if (node instanceof BookSequence) {
            return ((BookSequence) node).getBook();
        } else if (node instanceof Book) {
            return node;
        }
        return null;
        }

        @Override
        public Object getChild (Object parent,int index){
            if (parent instanceof Author) {
                Author author = (Author) parent;
                if (author.getSeries().size() > index) {
                    return author.getSeries().toArray()[index];
                } else {
                    return author.getBooks().get(index - author.getSeries().size());
                }
            } else if (parent instanceof Sequence) {
                return ((Sequence) parent).getBooks().get(index);
            }
            return 0;
        }

        @Override
        public int getChildCount (Object parent){
            if (parent instanceof Author) {
                return ((Author) parent).getBooks().size() + ((Author) parent).getSeries().size();
            } else if (parent instanceof Sequence) {
                return ((Sequence) parent).getBooks().size();
            }
            return 0;
        }

        @Override
        public Class getColumnClass ( int column){
            if (column == 0) {
                return TreeTableModel.class;
            } else {
                return String.class;
            }
        }

    public void setAuthor(Author author) {
        root = author;
    }


}

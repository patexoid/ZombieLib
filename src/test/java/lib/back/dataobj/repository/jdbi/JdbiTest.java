package lib.back.dataobj.repository.jdbi;


  import lib.back.dataobj.Book;
import lib.back.dataobj.BookSequence;
import lib.back.dataobj.repository.jdbi.BestBindBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Binder;

/**
 * Unit test for simple App.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({SQLStatement.class, StatementContext.class})
public class JdbiTest {


    @SuppressWarnings("unchecked")
    @Test
    public void testBestBindBean() {
        Book book = new Book();
        book.setBookId(1l);
        BookSequence bookSequence = new BookSequence();
        bookSequence.setBook(book);
        BestBindBean.BestBeanBindFactory bestBeanBindFactory = new BestBindBean.BestBeanBindFactory();
        Binder<BestBindBean, Object> build = bestBeanBindFactory.build(null);
        SQLStatement emptyStatemet = getSQLStatement("SELECT * FROM AbstractTable");
        BestBindBean bestBeanBind = PowerMockito.mock(BestBindBean.class);
        PowerMockito.when(bestBeanBind.value()).thenReturn("b");
        build.bind(emptyStatemet, bestBeanBind, book);
        //no fields in SQL
        Mockito.verify(emptyStatemet, Mockito.never()).dynamicBind(Matchers.<Class<?>>any(), Matchers.<String>any(), Matchers.any());
        SQLStatement statement = getSQLStatement("SELECT * FROM AbstractTable WHERE someField=:b.bookId");
        build.bind(statement, bestBeanBind, book);

        //Single simple field
        Mockito.verify(statement, Mockito.times(1)).dynamicBind(Matchers.<Class<?>>any(), Matchers.<String>any(), Matchers.any());
        Mockito.verify(statement, Mockito.times(1)).dynamicBind(Matchers.eq(Long.class), Matchers.eq("b.bookId"), Matchers.eq(1l));

        statement = getSQLStatement("SELECT * FROM AbstractTable WHERE someField=:b.book.bookId");
        build.bind(statement, bestBeanBind, bookSequence);

        //Single simple field
        Mockito.verify(statement, Mockito.times(1)).dynamicBind(Matchers.<Class>any(), Matchers.<String>any(), Matchers.any());
        Mockito.verify(statement, Mockito.times(1)).dynamicBind(Matchers.eq(Long.class), Matchers.eq("b.book.bookId"), Matchers.eq(1l));

    }

    private SQLStatement getSQLStatement(String sql) {
        SQLStatement statement = PowerMockito.mock(SQLStatement.class);
        StatementContext context = PowerMockito.mock(StatementContext.class);
        PowerMockito.when(context.getRawSql()).thenReturn(sql);
        PowerMockito.when(statement.getContext()).thenReturn(context);
        return statement;
    }
}

package lib.back.mysqldumpparser.desc;

import lib.back.mysqldumpparser.SortOrder;

/**
 * Created by Alexey on 03.01.2016.
 */
public class AuthorBookRelationDesc {
        @SortOrder(0)
        Long author;

        @SortOrder(1)
        Long book;

        public Long getAuthor() {
                return author;
        }

        public void setAuthor(Long author) {
                this.author = author;
        }

        public Long getBook() {
                return book;
        }

        public void setBook(Long book) {
                this.book = book;
        }
}

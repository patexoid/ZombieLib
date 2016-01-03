package lib.back.mysqldumpparser.desc;

import lib.back.mysqldumpparser.SortOrder;

import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Created by Alexey on 03.01.2016.
 */
public class BookAnnotationDesc {

    @SortOrder(0)
    private Long id;

    @SortOrder(1)
    String title;

    @SortOrder(2)
    String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

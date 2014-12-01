package lib.back.dataobj;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Created by apotekhin on 9/2/2014.
 */
@Entity
public class BookAnnotation {

    @Id
    private Long id;

    String title;

    @Lob
    String body;

    public BookAnnotation() {
    }


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

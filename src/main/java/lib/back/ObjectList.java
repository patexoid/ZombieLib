package lib.back;

import lib.back.dataobj.HibernateUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 01.02.14.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObjectList<E> extends AbstractList<E> {
    private final Class _class;
    private final List<Long> indexes;


    public ObjectList(Class clazz) {
        _class = clazz;
        indexes = (List<Long>) new ArrayList(HibernateUtils.getSession().createQuery("Select " + HibernateUtils.getId(clazz) + " from " + _class.getName()).list());

    }

    @Override
    public E get(int index) {
        return (E) HibernateUtils.get(_class, indexes.get(index));
    }

    @Override
    public int size() {
        return indexes.size();
    }
}

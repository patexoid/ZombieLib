package lib.back.mysqldumpparser;

/**
 * Created by apotekhin on 10/7/2014.
 */
public interface Validator<T> {

    boolean isValid(T t);
}

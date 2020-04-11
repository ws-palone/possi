package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.Color;
import org.hibernate.Session;

public class ColorDAOImpl extends AbstractHibernateDAO implements ColorDAO {
    @Override
    public Color findById(Integer id) {
        Session session = getNewSession();
        Color color = (Color)session.get(Color.class, id);
        session.close();
        return color;
    }
}
